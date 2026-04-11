package healing.pet.view.components;

import healing.pet.model.Feedback;
import healing.pet.service.FeedbackService;
import healing.pet.ui.Theme.DarkTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.util.UserContext;
import healing.pet.view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserFeedbackPanel extends JPanel {
    private MainFrame mainFrame;
    private FeedbackService feedbackService;
    private JPanel contentPanel;

    public UserFeedbackPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.feedbackService = new FeedbackService();
        this.contentPanel = new JPanel();

        initUI();
        loadFeedbacks();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titleLabel = new JLabel(" 💬 意见反馈");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        header.add(titleLabel, BorderLayout.WEST);

        JButton submitBtn = new JButton("✍️ 提交反馈");
        submitBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        submitBtn.addActionListener(e -> showSubmitDialog());
        header.add(submitBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadFeedbacks() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        try {
            String userId = UserContext.getInstance().getCurrentUser().getUserId();
            List<Feedback> feedbacks = feedbackService.getUserFeedbacks(userId);

            if (feedbacks.isEmpty()) {
                JLabel emptyLabel = new JLabel("暂无反馈记录", JLabel.CENTER);
                emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(Box.createVerticalGlue());
                contentPanel.add(emptyLabel);
                contentPanel.add(Box.createVerticalGlue());
            } else {
                for (Feedback feedback : feedbacks) {
                    contentPanel.add(createFeedbackCard(feedback));
                    contentPanel.add(Box.createVerticalStrut(15));
                }
            }
        } catch (SQLException e) {
            showError("加载数据失败：" + e.getMessage());
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createFeedbackCard(Feedback feedback) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Theme theme = mainFrame.getCurrentTheme();
                Color cardBg = theme instanceof DarkTheme ? new Color(45, 45, 48) : Color.WHITE;
                Color borderColor = theme instanceof DarkTheme ? new Color(80, 80, 85) : new Color(200, 180, 200);

                g2.setColor(cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };

        card.setLayout(new BorderLayout(15, 15));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setPreferredSize(new Dimension(getWidth() - 40, 180));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        Theme theme = mainFrame.getCurrentTheme();
        boolean isDark = theme instanceof DarkTheme;
        Color textColor = isDark ? new Color(220, 220, 220) : new Color(60, 60, 60);
        Color subTextColor = isDark ? new Color(180, 180, 180) : new Color(120, 120, 120);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        String timeStr = feedback.getSubmitTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        JLabel timeLabel = new JLabel("📅 " + timeStr);
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        timeLabel.setForeground(subTextColor);

        JLabel statusLabel = new JLabel(feedback.getStatus());
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        statusLabel.setForeground("已回复".equals(feedback.getStatus()) ?
                new Color(100, 200, 150) : new Color(255, 180, 50));

        topPanel.add(timeLabel, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.EAST);

        JTextArea contentArea = new JTextArea(feedback.getContent());
        contentArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentArea.setForeground(textColor);
        contentArea.setOpaque(false);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setRows(3);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        if (feedback.getReplyContent() != null && !feedback.getReplyContent().isEmpty()) {
            JPanel replyPanel = new JPanel(new BorderLayout(5, 5));
            replyPanel.setOpaque(false);
            replyPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(isDark ? new Color(80, 80, 85) : new Color(220, 220, 220)),
                    "管理员回复",
                    0, 0,
                    new Font("微软雅黑", Font.BOLD, 13),
                    new Color(100, 150, 200)
            ));

            JTextArea replyArea = new JTextArea(feedback.getReplyContent());
            replyArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            replyArea.setForeground(subTextColor);
            replyArea.setOpaque(false);
            replyArea.setEditable(false);
            replyArea.setLineWrap(true);
            replyArea.setWrapStyleWord(true);
            replyArea.setRows(2);

            replyPanel.add(replyArea, BorderLayout.CENTER);
            bottomPanel.add(replyPanel, BorderLayout.CENTER);
        }

        card.add(topPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showSubmitDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "提交反馈", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(false);

        Theme theme = mainFrame.getCurrentTheme();
        boolean isDark = theme instanceof DarkTheme;
        Color textColor = isDark ? new Color(220, 220, 220) : new Color(60, 60, 60);

        JLabel tipLabel = new JLabel("💡 请详细描述您遇到的问题或建议，我们会尽快处理");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        tipLabel.setForeground(new Color(255, 140, 0));

        JTextArea contentArea = new JTextArea(8, 20);
        contentArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("反馈内容"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton submitBtn = new JButton("提交");
        submitBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        submitBtn.setBackground(new Color(100, 150, 200));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        cancelBtn.setBackground(isDark ? new Color(70, 70, 75) : new Color(200, 200, 200));
        cancelBtn.setForeground(textColor);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancelBtn.addActionListener(e -> dialog.dispose());

        submitBtn.addActionListener(e -> {
            String content = contentArea.getText().trim();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请填写反馈内容！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String userId = UserContext.getInstance().getCurrentUser().getUserId();
                String username = UserContext.getInstance().getCurrentUser().getUsername();
                feedbackService.submitFeedback(userId, username, content);
                JOptionPane.showMessageDialog(dialog, "反馈提交成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadFeedbacks();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(submitBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(tipLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
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
        loadFeedbacks();
    }
}
