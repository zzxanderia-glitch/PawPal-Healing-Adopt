package healing.pet.ui.content;

import healing.pet.model.AdoptionRequest;
import healing.pet.service.AdoptionService;
import healing.pet.view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminAuditPanel extends JPanel {
    private MainFrame mainFrame;
    private AdoptionService adoptionService;
    private JPanel listPanel;
    private JScrollPane scrollPane;

    public AdminAuditPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.adoptionService = new AdoptionService();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("领养申请审批中心");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> {
            try {
                loadAuditRequests();
            } catch (SQLException ex) {
                showError("刷新失败：" + ex.getMessage());
            }
        });
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        try {
            loadAuditRequests();
        } catch (SQLException e) {
            showError("加载数据失败：" + e.getMessage());
        }
    }

    private void loadAuditRequests() throws SQLException {
        listPanel.removeAll();

        List<AdoptionRequest> pendingRequests = adoptionService.getPendingRequests();

        if (pendingRequests.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无待审批的申请", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(150, 150, 150));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
            listPanel.add(Box.createVerticalGlue());
        } else {
            for (int i = 0; i < pendingRequests.size(); i++) {
                AdoptionRequest request = pendingRequests.get(i);
                listPanel.add(new AuditRequestRowPanel(request));

                if (i < pendingRequests.size() - 1) {
                    listPanel.add(Box.createVerticalStrut(15));
                }
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void showError(String message) {
        listPanel.removeAll();
        JLabel errorLabel = new JLabel(message);
        errorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        listPanel.add(errorLabel);
        listPanel.revalidate();
        listPanel.repaint();
    }

    class AuditRequestRowPanel extends JPanel {
        public AuditRequestRowPanel(AdoptionRequest request) {
            setLayout(new BorderLayout(20, 0));
            setOpaque(false);

            boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
            Color borderColor = isDark ? new Color(80, 80, 85) : new Color(220, 220, 220);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));

            setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            setMinimumSize(new Dimension(0, 200));
            setPreferredSize(new Dimension(0, 200));

            Color textColor = isDark ? new Color(220, 220, 220) : new Color(60, 60, 60);
            Color labelColor = isDark ? new Color(180, 180, 180) : new Color(100, 100, 100);

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setOpaque(false);
            leftPanel.setPreferredSize(new Dimension(200, 170));

            JLabel petNameLabel = new JLabel("宠物：");
            petNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
            petNameLabel.setForeground(labelColor);
            petNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(petNameLabel);

            JLabel petValueLabel = new JLabel(request.getPet() != null ? request.getPet().getName() : "未知宠物");
            petValueLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            petValueLabel.setForeground(textColor);
            petValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(petValueLabel);

            leftPanel.add(Box.createVerticalStrut(10));

            JLabel applicantLabel = new JLabel("申请人：");
            applicantLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
            applicantLabel.setForeground(labelColor);
            applicantLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(applicantLabel);

            JLabel applicantValueLabel = new JLabel(request.getApplicantName());
            applicantValueLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            applicantValueLabel.setForeground(textColor);
            applicantValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(applicantValueLabel);

            leftPanel.add(Box.createVerticalStrut(10));

            JLabel timeLabel = new JLabel("申请时间：");
            timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
            timeLabel.setForeground(labelColor);
            timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(timeLabel);

            String timeStr = request.getApplyTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            JLabel timeValueLabel = new JLabel(timeStr);
            timeValueLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            timeValueLabel.setForeground(textColor);
            timeValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(timeValueLabel);

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);
            rightPanel.setBorder(new EmptyBorder(0, 20, 0, 0));

            JPanel contactPanel = new JPanel(new GridLayout(2, 2, 15, 10));
            contactPanel.setOpaque(false);

            JLabel phoneLabel = new JLabel("电话：");
            phoneLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
            phoneLabel.setForeground(labelColor);

            JLabel phoneValueLabel = new JLabel(request.getApplicantPhone());
            phoneValueLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            phoneValueLabel.setForeground(textColor);

            JLabel addressLabel = new JLabel("地址：");
            addressLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
            addressLabel.setForeground(labelColor);

            JLabel addressValueLabel = new JLabel(request.getApplicantAddress());
            addressValueLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            addressValueLabel.setForeground(textColor);

            contactPanel.add(phoneLabel);
            contactPanel.add(phoneValueLabel);
            contactPanel.add(addressLabel);
            contactPanel.add(addressValueLabel);
            rightPanel.add(contactPanel);

            rightPanel.add(Box.createVerticalStrut(10));

            JLabel reasonLabel = new JLabel("申请理由：");
            reasonLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
            reasonLabel.setForeground(labelColor);
            rightPanel.add(reasonLabel);

            rightPanel.add(Box.createVerticalStrut(5));

            JTextArea reasonArea = new JTextArea(request.getApplyReason());
            reasonArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            reasonArea.setForeground(textColor);
            reasonArea.setBackground(isDark ? new Color(50, 50, 55) : new Color(245, 245, 245));
            reasonArea.setLineWrap(true);
            reasonArea.setWrapStyleWord(true);
            reasonArea.setEditable(false);
            reasonArea.setPreferredSize(new Dimension(0, 60));

            JScrollPane reasonScroll = new JScrollPane(reasonArea);
            reasonScroll.setBorder(BorderFactory.createLineBorder(borderColor));
            reasonScroll.setPreferredSize(new Dimension(0, 65));
            reasonScroll.setOpaque(false);
            reasonScroll.getViewport().setOpaque(false);
            rightPanel.add(reasonScroll);

            rightPanel.add(Box.createVerticalStrut(10));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);

            JButton approveBtn = new JButton("通过");
            approveBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
            approveBtn.setBackground(new Color(100, 200, 150));
            approveBtn.setForeground(Color.WHITE);
            approveBtn.setFocusPainted(false);
            approveBtn.setBorderPainted(false);
            approveBtn.setOpaque(true);
            approveBtn.setPreferredSize(new Dimension(90, 35));
            approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            approveBtn.addActionListener(e -> {
                int result = JOptionPane.showConfirmDialog(
                        AdminAuditPanel.this,
                        "确认通过 " + request.getApplicantName() + " 对 " +
                                (request.getPet() != null ? request.getPet().getName() : "该宠物") + " 的领养申请？",
                        "确认通过",
                        JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        adoptionService.reviewAdoption(request.getRequestId(), "已通过");
                        JOptionPane.showMessageDialog(AdminAuditPanel.this, "申请已通过！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        loadAuditRequests();
                        mainFrame.refreshContent();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(AdminAuditPanel.this, "操作失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(approveBtn);

            JButton rejectBtn = new JButton("拒绝");
            rejectBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
            rejectBtn.setBackground(new Color(255, 100, 100));
            rejectBtn.setForeground(Color.WHITE);
            rejectBtn.setFocusPainted(false);
            rejectBtn.setBorderPainted(false);
            rejectBtn.setOpaque(true);
            rejectBtn.setPreferredSize(new Dimension(90, 35));
            rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rejectBtn.addActionListener(e -> {
                int result = JOptionPane.showConfirmDialog(
                        AdminAuditPanel.this,
                        "确认拒绝 " + request.getApplicantName() + " 对 " +
                                (request.getPet() != null ? request.getPet().getName() : "该宠物") + " 的领养申请？",
                        "确认拒绝",
                        JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        adoptionService.reviewAdoption(request.getRequestId(), "未通过");
                        JOptionPane.showMessageDialog(AdminAuditPanel.this, "申请已拒绝！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        loadAuditRequests();
                        mainFrame.refreshContent();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(AdminAuditPanel.this, "操作失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(rejectBtn);

            rightPanel.add(Box.createVerticalGlue());
            rightPanel.add(buttonPanel);

            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.CENTER);
        }
    }
}
