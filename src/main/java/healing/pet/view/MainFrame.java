package healing.pet.view;

import healing.pet.model.Feedback;
import healing.pet.service.FeedbackService;
import healing.pet.ui.Theme.DarkTheme;
import healing.pet.ui.Theme.LightTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.ui.content.ContentPanel;
import healing.pet.ui.sidebar.SidebarPanel;
import healing.pet.util.UserContext; // 确保导入了单例类

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 🐾 萌友速配 - 核心主框架
 * 组长全局控制版：实现权限物理分流与主题动态适配
 */
public class MainFrame extends JFrame {

    private SidebarPanel sidebarPanel;
    private ContentPanel contentPanel;
    private Theme currentTheme;

    public MainFrame() throws SQLException {
        // 1. 初始化治愈系主题
        currentTheme = new LightTheme();
        currentTheme.applyTheme();

        // 2. 窗口基础设置
        setTitle("萌友速配 - 温暖归宿平台");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(currentTheme.getWindowWidth(), currentTheme.getWindowHeight());
        setLocationRelativeTo(null); // 居中
        setLayout(new BorderLayout());

        // 3. 首次加载布局
        initLayout();

        setVisible(true);
    }

    /**
     * 【组长核心方法】初始化/物理重建布局
     * 无论身份切换还是主题切换，调用此方法即可刷新全身
     */
    public void initLayout() {
        // A. 移除当前窗口内所有组件（侧边栏、中心区、状态栏等）
        getContentPane().removeAll();

        // B. 获取全局最新身份
        boolean isAdmin = UserContext.getInstance().isAdmin();

        try {
            // C. 重新创建并装配组件
            // 此时 SidebarPanel 构造时会检查 UserContext，从而决定显示哪些按钮
            contentPanel = new ContentPanel(this);
            contentPanel.setAdminMode(isAdmin);

            sidebarPanel = new SidebarPanel(contentPanel, this);
            sidebarPanel.updateAdminMode(isAdmin);

            // D. 重新塞回窗口
            add(sidebarPanel, BorderLayout.WEST);
            add(contentPanel, BorderLayout.CENTER);

        } catch (SQLException e) {
            System.err.println("刷新布局时数据库连接异常: " + e.getMessage());
        }

        // E. 强制通知 Swing 重新计算布局并重绘
        revalidate();
        repaint();
    }

    /**
     * 【任务要求：权限切换核心入口】
     * 场景：用户在 SettingPanel 点击“身份切换”并验证成功后调用
     */
    public void updateAdminPermission(boolean isAdmin) {
        // 1. 同步修改全局状态机（大脑）
        UserContext.getInstance().setAdminMode(isAdmin);

        // 2. 异步刷新 UI，防止界面卡死
        SwingUtilities.invokeLater(() -> {
            initLayout();

            // 3. 权限切换后的引导逻辑
            if (isAdmin) {
                contentPanel.showPage("admin_pet"); // 自动跳到管理后台
                JOptionPane.showMessageDialog(this, "✨ 欢迎进入管理系统，已为您开启最高权限。");
            } else {
                contentPanel.showPage("home");      // 自动跳回首页
                JOptionPane.showMessageDialog(this, "🌱 已回到用户模式，请尽情挑选有缘的萌友吧。");
            }
        });
    }

    /**
     * 动态切换主题
     */
    public void applyTheme(Theme newTheme) {
        this.currentTheme = newTheme;
        SwingUtilities.invokeLater(() -> {
            try {
                // 应用 UI 渲染
                newTheme.applyTheme();
                // 彻底刷新布局（背景色、按钮色等）
                initLayout();
                // 更新窗口尺寸
                setSize(newTheme.getWindowWidth(), newTheme.getWindowHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }
    public void refreshContent() {
        if (contentPanel != null) {
            contentPanel.refresh();
        }
    }

    // 这里不再使用本地 isAdmin 变量，始终从 UserContext 获取，保证一致性
    public boolean isAdmin() {
        return UserContext.getInstance().isAdmin();
    }

    /**
     * 🚀 项目发动机
     */
    public static void main(String[] args) {
        try {
            // 设置皮肤（如果 pom 刷新过，这里不报错）
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("FlatLaf 加载失败");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "数据库启动失败，请检查连接！");
            }
        });
    }

    public static class UserFeedbackPanel extends JPanel {
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
}