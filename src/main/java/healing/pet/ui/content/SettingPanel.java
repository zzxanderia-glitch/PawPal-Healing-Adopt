package healing.pet.ui.content;

import healing.pet.ui.Theme.DarkTheme;
import healing.pet.ui.Theme.LightTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.util.UserContext;
import healing.pet.view.MainFrame;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * 治愈系设置中心 —— 增强白板卡片版
 */
public class SettingPanel extends JPanel {

    private MainFrame mainFrame;
    private JComboBox<String> themeComboBox;
    private JButton switchRoleBtn;

    public SettingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 1. 设置主背景（跟随主题）
        setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 2. 创建"中央白板卡片"
        JPanel whiteboard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 💡 修复：根据当前主题判断卡片颜色
                boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
                Color cardBg = isDark ? new Color(45, 45, 48) : Color.WHITE; // 暗色用浅灰，亮色用白
                Color borderColor = isDark ? new Color(80, 80, 85) : new Color(0, 0, 0, 30);

                // 绘制背景
                g2.setColor(cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                // 绘制边框
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                g2.dispose();
            }
        };
        whiteboard.setOpaque(false);
        whiteboard.setPreferredSize(new Dimension(450, 580));
        whiteboard.setLayout(new GridBagLayout());
        whiteboard.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 模块 A：圆形头像区域 ---
        gbc.gridy = 0;
        JPanel avatarContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 💡 修复：暗色模式下调整头像底圆圈颜色，使其在灰卡上可见
                boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
                g2.setColor(isDark ? new Color(60, 60, 65) : new Color(255, 230, 235));
                
                g2.fill(new Ellipse2D.Double(0, 0, 100, 100));
                try {
                    FlatSVGIcon catIcon = new FlatSVGIcon("icon/homepage.svg", 60, 60);
                    // 💡 修复：暗色模式下图标颜色反白
                    if (isDark) {
                        catIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> Color.WHITE));
                    }
                    catIcon.paintIcon(this, g2, 20, 20);
                } catch (Exception e) {}
                g2.dispose();
            }
        };
        avatarContainer.setPreferredSize(new Dimension(100, 100));
        avatarContainer.setOpaque(false);
        whiteboard.add(avatarContainer, gbc);

        // --- 模块 B：用户名与领养统计 ---
        gbc.gridy = 1;
        boolean isAdmin = UserContext.getInstance().isAdmin();
        JLabel nameLabel = new JLabel(isAdmin ? "管理员：Admin" : "用户：治愈喵友", JLabel.CENTER);
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        nameLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        // 💡 修复：文字颜色跟随主题
        nameLabel.setForeground(mainFrame.getCurrentTheme().getContentTextColor());
        whiteboard.add(nameLabel, gbc);

        gbc.gridy = 2;
        JLabel statsLabel = new JLabel("🐾 已成功领养宠物：0 只", JLabel.CENTER);
        // 💡 修复：文字颜色跟随主题（暗色模式下变亮）
        statsLabel.setForeground(isAdmin ? mainFrame.getCurrentTheme().getContentTextColor() : new Color(120, 120, 120));
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        whiteboard.add(statsLabel, gbc);

        // 分割线
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 20, 0);
        whiteboard.add(new JSeparator(), gbc);
        gbc.insets = new Insets(10, 0, 10, 0);

        // --- 模块 C：设置表单 ---
        gbc.gridy = 4;
        JLabel themeLabel = new JLabel("界面主题风格：");
        themeLabel.setForeground(mainFrame.getCurrentTheme().getContentTextColor());
        whiteboard.add(themeLabel, gbc);

        gbc.gridy = 5;
        String[] themes = {"浅色奶油 (治愈模式)", "深邃暗色 (深夜模式)"};
        themeComboBox = new JComboBox<>(themes);
        themeComboBox.setPreferredSize(new Dimension(0, 35));
        themeComboBox.addActionListener(e -> changeTheme());
        whiteboard.add(themeComboBox, gbc);

        // --- 模块 D：身份切换按钮 ---
        gbc.gridy = 6;
        gbc.insets = new Insets(40, 0, 0, 0);
        switchRoleBtn = new JButton(isAdmin ? "退出管理员权限" : "验证身份切换管理员");
        switchRoleBtn.setPreferredSize(new Dimension(0, 45));
        switchRoleBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        switchRoleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        switchRoleBtn.setBackground(isAdmin ? new Color(255, 100, 100) : new Color(110, 170, 240));
        switchRoleBtn.setForeground(Color.WHITE);

        switchRoleBtn.addActionListener(e -> handleRoleSwitch());
        whiteboard.add(switchRoleBtn, gbc);

        // 将白板添加进页面
        add(whiteboard);
    }

    private void handleRoleSwitch() {
        boolean currentlyAdmin = UserContext.getInstance().isAdmin();
        if (!currentlyAdmin) {
            JPasswordField pf = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(this, pf, "请输入管理员密钥",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                if ("123456".equals(new String(pf.getPassword()))) {
                    mainFrame.updateAdminPermission(true);
                } else {
                    JOptionPane.showMessageDialog(this, "密钥错误！", "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            int confirm = JOptionPane.showConfirmDialog(this, "确定退出管理模式并回到普通用户视角吗？", "提示", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.updateAdminPermission(false);
            }
        }
    }

    private void changeTheme() {
        int index = themeComboBox.getSelectedIndex();
        Theme newTheme = (index == 0) ? new LightTheme() : new DarkTheme();
        mainFrame.applyTheme(newTheme);
    }
}