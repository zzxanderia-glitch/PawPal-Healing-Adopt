package healing.pet.ui.content;

import healing.pet.model.Feedback;
import healing.pet.service.FeedbackService;
import healing.pet.ui.Theme.DarkTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminFeedbackPanel extends JPanel {
    private MainFrame mainFrame;
    private FeedbackService feedbackService;
    private JPanel listPanel;

    public AdminFeedbackPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.feedbackService = new FeedbackService();
        this.listPanel = new JPanel();

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("📨 用户反馈管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> {
            try {
                loadFeedbacks();
            } catch (SQLException ex) {
                showError("刷新失败：" + ex.getMessage());
            }
        });
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        try {
            loadFeedbacks();
        } catch (SQLException e) {
            showError("加载数据失败：" + e.getMessage());
        }
    }

    private void loadFeedbacks() throws SQLException {
        listPanel.removeAll();

        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();

        if (feedbacks.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无用户反馈", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(150, 150, 150));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
            listPanel.add(Box.createVerticalGlue());
        } else {
            for (int i = 0; i < feedbacks.size(); i++) {
                Feedback feedback = feedbacks.get(i);
                listPanel.add(new FeedbackRowPanel(feedback));

                if (i < feedbacks.size() - 1) {
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

    class FeedbackRowPanel extends JPanel {
        public FeedbackRowPanel(Feedback feedback) {
            setLayout(new BorderLayout(20, 0));
            setOpaque(false);

            Theme theme = mainFrame.getCurrentTheme();
            boolean isDark = theme instanceof DarkTheme;
            Color borderColor = isDark ? new Color(80, 80, 85) : new Color(220, 220, 220);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));

            setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
            setMinimumSize(new Dimension(0, 220));
            setPreferredSize(new Dimension(0, 220));

            Color textColor = isDark ? new Color(220, 220, 220) : new Color(60, 60, 60);
            Color labelColor = isDark ? new Color(180, 180, 180) : new Color(100, 100, 100);

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setOpaque(false);
            leftPanel.setPreferredSize(new Dimension(200, 190));

            JLabel userLabel = new JLabel("用户：");
            userLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
            userLabel.setForeground(labelColor);
            userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(userLabel);

            JLabel userValueLabel = new JLabel(feedback.getUsername() != null ? feedback.getUsername() : feedback.getUserId());
            userValueLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            userValueLabel.setForeground(textColor);
            userValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(userValueLabel);

            leftPanel.add(Box.createVerticalStrut(10));

            String timeStr = feedback.getSubmitTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            JLabel timeLabel = new JLabel("提交时间：");
            timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
            timeLabel.setForeground(labelColor);
            timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(timeLabel);

            JLabel timeValueLabel = new JLabel(timeStr);
            timeValueLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            timeValueLabel.setForeground(textColor);
            timeValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(timeValueLabel);

            leftPanel.add(Box.createVerticalStrut(10));

            JLabel statusLabel = new JLabel("状态：" + feedback.getStatus());
            statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
            statusLabel.setForeground("已回复".equals(feedback.getStatus()) ?
                    new Color(100, 200, 150) : new Color(255, 180, 50));
            statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(statusLabel);

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);
            rightPanel.setBorder(new EmptyBorder(0, 20, 0, 0));

            JLabel contentLabel = new JLabel("反馈内容：");
            contentLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
            contentLabel.setForeground(labelColor);
            rightPanel.add(contentLabel);

            rightPanel.add(Box.createVerticalStrut(5));

            JTextArea contentArea = new JTextArea(feedback.getContent());
            contentArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            contentArea.setForeground(textColor);
            contentArea.setBackground(isDark ? new Color(50, 50, 55) : new Color(245, 245, 245));
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            contentArea.setEditable(false);
            contentArea.setPreferredSize(new Dimension(0, 60));

            JScrollPane contentScroll = new JScrollPane(contentArea);
            contentScroll.setBorder(BorderFactory.createLineBorder(borderColor));
            contentScroll.setPreferredSize(new Dimension(0, 65));
            contentScroll.setOpaque(false);
            contentScroll.getViewport().setOpaque(false);
            rightPanel.add(contentScroll);

            rightPanel.add(Box.createVerticalStrut(10));

            if (feedback.getReplyContent() != null && !feedback.getReplyContent().isEmpty()) {
                JLabel replyLabel = new JLabel("回复内容：");
                replyLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
                replyLabel.setForeground(labelColor);
                rightPanel.add(replyLabel);

                rightPanel.add(Box.createVerticalStrut(5));

                JTextArea replyArea = new JTextArea(feedback.getReplyContent());
                replyArea.setFont(new Font("微软雅黑", Font.PLAIN, 13));
                replyArea.setForeground(new Color(100, 150, 200));
                replyArea.setBackground(isDark ? new Color(40, 50, 60) : new Color(230, 240, 250));
                replyArea.setLineWrap(true);
                replyArea.setWrapStyleWord(true);
                replyArea.setEditable(false);
                replyArea.setPreferredSize(new Dimension(0, 50));

                JScrollPane replyScroll = new JScrollPane(replyArea);
                replyScroll.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200)));
                replyScroll.setPreferredSize(new Dimension(0, 55));
                replyScroll.setOpaque(false);
                replyScroll.getViewport().setOpaque(false);
                rightPanel.add(replyScroll);
            }

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);
            buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

            JButton replyBtn = new JButton("回复");
            replyBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
            replyBtn.setBackground(new Color(100, 150, 200));
            replyBtn.setForeground(Color.WHITE);
            replyBtn.setFocusPainted(false);
            replyBtn.setBorderPainted(false);
            replyBtn.setOpaque(true);
            replyBtn.setPreferredSize(new Dimension(90, 35));
            replyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            replyBtn.addActionListener(e -> showReplyDialog(feedback));
            buttonPanel.add(replyBtn);

            rightPanel.add(Box.createVerticalGlue());
            rightPanel.add(buttonPanel);

            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.CENTER);
        }

        private void showReplyDialog(Feedback feedback) {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(AdminFeedbackPanel.this),
                    "回复反馈", true);
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(AdminFeedbackPanel.this);

            Theme theme = mainFrame.getCurrentTheme();
            boolean isDark = theme instanceof DarkTheme;
            Color textColor = isDark ? new Color(220, 220, 220) : new Color(60, 60, 60);

            JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            mainPanel.setOpaque(false);

            JLabel userLabel = new JLabel("用户：" + (feedback.getUsername() != null ? feedback.getUsername() : feedback.getUserId()));
            userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            userLabel.setForeground(textColor);

            JTextArea replyArea = new JTextArea(6, 20);
            replyArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            replyArea.setLineWrap(true);
            replyArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(replyArea);
            scrollPane.setBorder(BorderFactory.createTitledBorder("回复内容"));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);

            JButton submitBtn = new JButton("提交回复");
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
                String replyContent = replyArea.getText().trim();
                if (replyContent.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "请填写回复内容！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    feedbackService.replyFeedback(feedback.getFeedbackId(), replyContent);
                    JOptionPane.showMessageDialog(dialog, "回复成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadFeedbacks();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            buttonPanel.add(submitBtn);
            buttonPanel.add(cancelBtn);

            mainPanel.add(userLabel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(mainPanel);
            dialog.setVisible(true);
        }
    }

    public void refresh() {
        try {
            loadFeedbacks();
        } catch (SQLException e) {
            showError("刷新失败：" + e.getMessage());
        }
    }
}
