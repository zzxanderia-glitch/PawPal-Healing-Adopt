package healing.pet.view;
import healing.pet.model.User;

import healing.pet.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class RegisterFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private AuthService authService = new AuthService();

    private static final Color PRIMARY_COLOR = new Color(100, 180, 255);
    private static final Color BG_COLOR = new Color(255, 252, 245);

    public RegisterFrame() {
        setTitle("宠物领养系统 - 注册");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(BG_COLOR);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 2, getHeight() - 2, 20, 20));

                g2d.dispose();
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("✨ 用户注册", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(70, 70, 70));
        titleLabel.setBounds(0, 50, 450, 45);
        mainPanel.add(titleLabel);

        JPanel formPanel = createFormPanel();
        formPanel.setBounds(50, 130, 350, 280);
        mainPanel.add(formPanel);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBounds(50, 430, 350, 110);
        mainPanel.add(buttonPanel);

        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("微软雅黑", Font.BOLD, 20));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(255, 100, 100));
        closeBtn.setBorder(null);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setBounds(410, 10, 30, 30);
        closeBtn.addActionListener(e -> dispose());
        mainPanel.add(closeBtn);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setOpaque(false);

        JLabel userIdLabel = new JLabel("账号");
        userIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userIdLabel.setForeground(new Color(100, 100, 100));
        userIdLabel.setBounds(0, 10, 300, 25);
        panel.add(userIdLabel);

        userIdField = new JTextField(20);
        userIdField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        userIdField.setBounds(0, 35, 350, 40);
        panel.add(userIdField);

        JLabel hintLabel = new JLabel("<html><span style='color: #999;'>普通用户：6位数字 | 管理员：G+6位数字</span></html>");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        hintLabel.setBounds(0, 78, 350, 20);
        panel.add(hintLabel);

        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(100, 100, 100));
        passwordLabel.setBounds(0, 105, 300, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setBounds(0, 130, 350, 40);
        panel.add(passwordField);

        JLabel confirmLabel = new JLabel("确认密码");
        confirmLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmLabel.setForeground(new Color(100, 100, 100));
        confirmLabel.setBounds(0, 175, 300, 25);
        panel.add(confirmLabel);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        confirmPasswordField.setBounds(0, 200, 350, 40);
        panel.add(confirmPasswordField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 0, 12));
        panel.setOpaque(false);

        JButton registerBtn = createStyledButton("立即注册", PRIMARY_COLOR);
        panel.add(registerBtn);

        JButton backBtn = createStyledButton("返回登录", new Color(180, 180, 180));
        panel.add(backBtn);

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegister();
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("微软雅黑", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void performRegister() {
        String userId = userIdField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致！", "注册失败", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        String result = authService.register(user);
        JOptionPane.showMessageDialog(this, result, "注册提示",
                result.contains("成功") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);

        if ("注册成功！欢迎加入宠物领养系统。".equals(result)) {
            dispose();
        }
    }
}
