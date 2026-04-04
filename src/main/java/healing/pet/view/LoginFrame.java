package healing.pet.view;

import healing.pet.model.User;
import healing.pet.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeBox;
    private AuthService authService = new AuthService();

    private static final Color PRIMARY_COLOR = new Color(255, 154, 162);
    private static final Color HOVER_COLOR = new Color(255, 130, 140);
    private static final Color BG_COLOR = new Color(255, 252, 245);

    public LoginFrame() {
        setTitle("宠物领养系统 - 登录");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JLabel titleLabel = new JLabel("🐾 萌友速配", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        titleLabel.setForeground(new Color(70, 70, 70));
        titleLabel.setBounds(0, 60, 450, 50);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("温暖归宿平台", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setBounds(0, 110, 450, 30);
        mainPanel.add(subtitleLabel);

        JPanel formPanel = createFormPanel();
        formPanel.setBounds(50, 180, 350, 200);
        mainPanel.add(formPanel);

        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBounds(50, 390, 350, 100);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("微软雅黑", Font.BOLD, 20));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(255, 100, 100));
        closeBtn.setBorder(null);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setBounds(410, 10, 30, 30);
        closeBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(closeBtn);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setOpaque(false);

        JLabel userIdLabel = new JLabel("账号");
        userIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userIdLabel.setForeground(new Color(100, 100, 100));
        userIdLabel.setBounds(0, 10, 60, 25);
        panel.add(userIdLabel);

        userIdField = new JTextField(20);
        userIdField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        userIdField.setBounds(0, 35, 350, 40);
        panel.add(userIdField);

        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(100, 100, 100));
        passwordLabel.setBounds(0, 85, 60, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setBounds(0, 110, 350, 40);
        panel.add(passwordField);

        rememberMeBox = new JCheckBox("记住密码");
        rememberMeBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        rememberMeBox.setForeground(new Color(120, 120, 120));
        rememberMeBox.setOpaque(false);
        rememberMeBox.setFocusPainted(false);
        rememberMeBox.setBounds(0, 160, 100, 25);
        panel.add(rememberMeBox);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 0, 12));
        panel.setOpaque(false);

        JButton loginBtn = createStyledButton("登 录", PRIMARY_COLOR);
        panel.add(loginBtn);

        JButton registerBtn = createStyledButton("注 册", new Color(100, 180, 255));
        panel.add(registerBtn);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
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

    private void performLogin() {
        User user = new User();
        user.setUserId(userIdField.getText().trim());
        user.setPassword(new String(passwordField.getPassword()));

        String result = authService.login(user);
        JOptionPane.showMessageDialog(this, result, "登录提示",
                result.contains("成功") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);

        if (result.contains("成功")) {
            dispose();
            try {
                MainFrame mainFrame = new MainFrame();
                mainFrame.updateAdminPermission(user.isAdmin());
                mainFrame.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "启动主界面失败：" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
