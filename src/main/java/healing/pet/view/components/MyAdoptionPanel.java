package healing.pet.view.components;

import healing.pet.model.AdoptionRequest;
import healing.pet.service.AdoptionService;
import healing.pet.view.MainFrame;
import healing.pet.view.components.PetDetailDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * 我的领养页面 - 用户查看自己的领养申请和审核进度
 */
public class MyAdoptionPanel extends JPanel {
    private MainFrame mainFrame;
    private AdoptionService adoptionService;
    private JPanel contentPanel;

    public MyAdoptionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adoptionService = new AdoptionService();
        this.contentPanel = new JPanel();

        initUI();
        loadMyAdoptions();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 顶部标题
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titleLabel = new JLabel(" 我的领养中心");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        header.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("🔄 刷新");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> loadMyAdoptions());
        header.add(refreshBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // 中间内容区（带滚动）
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadMyAdoptions() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        try {
            String userId = healing.pet.util.UserContext.getInstance().getCurrentUser().getUserId();
            java.util.List<AdoptionRequest> requests = adoptionService.getUserRequests(userId);

            if (requests.isEmpty()) {
                JLabel emptyLabel = new JLabel("暂无领养申请记录", JLabel.CENTER);
                emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(Box.createVerticalGlue());
                contentPanel.add(emptyLabel);
                contentPanel.add(Box.createVerticalGlue());
            } else {
                for (AdoptionRequest request : requests) {
                    contentPanel.add(createRequestCard(request));
                    contentPanel.add(Box.createVerticalStrut(15));
                }
            }
        } catch (SQLException e) {
            showError("加载数据失败：" + e.getMessage());
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createRequestCard(AdoptionRequest request) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
                Color cardBg = isDark ? new Color(45, 45, 48) : Color.WHITE;
                Color borderColor = isDark ? new Color(80, 80, 85) : new Color(200, 180, 200);

                g2.setColor(cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };

        card.setLayout(new BorderLayout(15, 15));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setPreferredSize(new Dimension(getWidth() - 40, 150));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
        Color textColor = isDark ? new Color(220, 220, 220) : new Color(60, 60, 60);

        // 左侧：宠物信息
        JPanel petInfo = new JPanel(new GridLayout(2, 1, 0, 10));
        petInfo.setOpaque(false);

        JLabel petNameLabel = new JLabel("🐾 " +
                (request.getPet() != null ? request.getPet().getName() : "未知宠物"));
        petNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        petNameLabel.setForeground(textColor);

        JLabel breedLabel = new JLabel(request.getPet() != null ? request.getPet().getBreed() : "未知品种");
        breedLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        breedLabel.setForeground(isDark ? new Color(180, 180, 180) : new Color(120, 120, 120));

        petInfo.add(petNameLabel);
        petInfo.add(breedLabel);

        // 中间：状态和时间
        JPanel statusInfo = new JPanel(new GridLayout(2, 1, 0, 10));
        statusInfo.setOpaque(false);

        JLabel statusLabel = new JLabel(request.getStatusDesc());
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        statusLabel.setForeground(request.getStatusColor());

        String timeStr = request.getApplyTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        JLabel timeLabel = new JLabel("申请时间：" + timeStr);
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        timeLabel.setForeground(isDark ? new Color(180, 180, 180) : new Color(120, 120, 120));

        statusInfo.add(statusLabel);
        statusInfo.add(timeLabel);

        // 右侧：查看详情按钮
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setOpaque(false);

        JButton detailBtn = new JButton("查看详情");
        detailBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        detailBtn.setBackground(isDark ? new Color(70, 70, 75) : new Color(240, 230, 240));
        detailBtn.setForeground(textColor);
        detailBtn.setFocusPainted(false);
        detailBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailBtn.addActionListener(e -> {
            if (request.getPet() != null) {
                new PetDetailDialog(mainFrame, request.getPet()).setVisible(true);
            }
        });

        actionPanel.add(detailBtn);

        card.add(petInfo, BorderLayout.WEST);
        card.add(statusInfo, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    private void showError(String message) {
        contentPanel.removeAll();
        JLabel errorLabel = new JLabel("❌ " + message);
        errorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(errorLabel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void refresh() {
        loadMyAdoptions();
    }
}
