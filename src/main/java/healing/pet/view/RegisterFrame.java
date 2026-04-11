package healing.pet.view;

import healing.pet.model.User;
import healing.pet.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private AuthService authService = new AuthService();

    // 💡 配色方案
    private static final Color PRIMARY_BLUE = new Color(110, 190, 255);
    private static final Color HOVER_BLUE = new Color(90, 170, 235);
    private static final Color BG_COLOR = new Color(255, 252, 240);
    private static final Color TEXT_COLOR = new Color(60, 60, 70);
    private static final Color ACCENT_PINK = new Color(255, 170, 190);

    public RegisterFrame() {
        setTitle("萌友速配 - 用户注册");
        setSize(460, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // 主面板（带圆角和阴影）
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 外层阴影
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fill(new RoundRectangle2D.Double(6, 6, getWidth() - 12, getHeight() - 12, 25, 25));

                // 背景渐变
                GradientPaint gradient = new GradientPaint(0, 0, BG_COLOR, 0, getHeight(), new Color(255, 245, 225));
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

                g2d.dispose();
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        setContentPane(mainPanel);

        // 1. 顶部装饰区域（猫咪图标 + 标题）
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 绘制可爱的猫爪印装饰
                g2d.setColor(new Color(255, 200, 210, 80));
                g2d.fillOval(30, 20, 40, 40);
                g2d.fillOval(390, 30, 35, 35);
                g2d.fillOval(200, 10, 50, 50);

                g2d.dispose();
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBounds(0, 0, 460, 130);
        mainPanel.add(headerPanel);

        // 标题
        JLabel titleLabel = new JLabel("加入萌友大家庭");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(0, 35, 460, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("找到属于你的治愈小伙伴");
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(140, 140, 150));
        subtitleLabel.setBounds(0, 75, 460, 20);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(subtitleLabel);

        // 2. 输入区域容器（白色圆角卡片）
        JPanel inputCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        inputCard.setBounds(40, 145, 380, 280);
        inputCard.setLayout(null);
        inputCard.setOpaque(false);
        mainPanel.add(inputCard);

        // 账号输入
        JLabel userIdLabel = createStyledLabel("账号");
        userIdLabel.setBounds(30, 25, 100, 25);
        inputCard.add(userIdLabel);

        userIdField = createStyledTextField();
        userIdField.setBounds(30, 50, 320, 42);
        inputCard.add(userIdField);

        JLabel tipLabel = new JLabel("普通用户：6位数字（如 123456）");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        tipLabel.setForeground(new Color(150, 150, 160));
        tipLabel.setBounds(30, 95, 320, 15);
        inputCard.add(tipLabel);

        // 密码输入
        JLabel passwordLabel = createStyledLabel("密码");
        passwordLabel.setBounds(30, 120, 100, 25);
        inputCard.add(passwordLabel);

        passwordField = createStyledPasswordField();
        passwordField.setBounds(30, 145, 320, 42);
        inputCard.add(passwordField);

        // 确认密码
        JLabel confirmLabel = createStyledLabel("确认密码");
        confirmLabel.setBounds(30, 195, 120, 25);
        inputCard.add(confirmLabel);

        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setBounds(30, 220, 320, 42);
        inputCard.add(confirmPasswordField);

        // 3. 按钮区域
        // 注册按钮（渐变蓝色）
        JButton registerBtn = new JButton("立即注册") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_BLUE, 0, getHeight(), new Color(90, 160, 240));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                super.paintComponent(g);
                g2d.dispose();
            }
        };
        registerBtn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBackground(PRIMARY_BLUE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setOpaque(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setBounds(40, 445, 380, 50);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(registerBtn);

        registerBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerBtn.setBackground(HOVER_BLUE);
                registerBtn.setFont(new Font("微软雅黑", Font.BOLD, 17));
            }
            public void mouseExited(MouseEvent e) {
                registerBtn.setBackground(PRIMARY_BLUE);
                registerBtn.setFont(new Font("微软雅黑", Font.BOLD, 16));
            }
        });

        // 返回登录按钮（白色背景，蓝色边框）
        JButton backBtn = new JButton("返回登录");
        backBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 2));
        backBtn.setOpaque(true);
        backBtn.setBounds(40, 510, 380, 45);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(backBtn);

        backBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { backBtn.setBackground(new Color(245, 250, 255)); }
            public void mouseExited(MouseEvent e) { backBtn.setBackground(Color.WHITE); }
        });

        // 4. 关闭按钮（右上角）
        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(ACCENT_PINK);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setOpaque(true);
        closeBtn.setBounds(415, 12, 32, 32);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        mainPanel.add(closeBtn);

        // 5. 事件绑定
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (userId.startsWith("G")) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "温馨提示：\n普通注册通道仅支持用户账号！\n管理员账号请联系系统管理员开通。",
                            "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "两次输入的密码不一致哦！\n请重新确认密码。",
                            "注册失败", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                User user = new User();
                user.setUserId(userId);
                user.setPassword(password);
                user.setUsername("新用户");

                String result = authService.register(user);

                if (result.contains("成功")) {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            result + "\n快去登录开始你的萌宠之旅吧！",
                            "注册成功", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                             result,
                            "注册提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        backBtn.addActionListener(e -> dispose());

        confirmPasswordField.addActionListener(e -> registerBtn.doClick());

        setVisible(true);
    }

    // 创建带样式的标签
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    // 创建带样式的文本框
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 225), 1),
                new EmptyBorder(5, 12, 5, 12)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    // 创建带样式的密码框
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 225), 1),
                new EmptyBorder(5, 12, 5, 12)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }
}
