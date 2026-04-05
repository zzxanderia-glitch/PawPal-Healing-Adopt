package healing.pet.view;

import healing.pet.model.User;
import healing.pet.service.AuthService;
import healing.pet.util.UserContext;

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

    // 💡 配色方案
    private static final Color PRIMARY_BLUE = new Color(100, 180, 255);
    private static final Color HOVER_BLUE = new Color(80, 160, 235);
    private static final Color BG_COLOR = new Color(255, 250, 235);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);

    public LoginFrame() {
        setTitle("萌友速配 - 登录");
        setSize(420, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // 主面板
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 阴影
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fill(new RoundRectangle2D.Double(5, 5, getWidth() - 10, getHeight() - 10, 20, 20));

                // 背景
                g2d.setColor(BG_COLOR);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        setContentPane(mainPanel);

        // 1. 标题
        JLabel titleLabel = new JLabel("萌友速配");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(0, 45, 420, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("温暖归宿平台");
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setBounds(0, 85, 420, 20);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);

        // 2. 输入区域
        JLabel userIdLabel = new JLabel("账号");
        userIdLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        userIdLabel.setForeground(TEXT_COLOR);
        userIdLabel.setBounds(50, 145, 60, 25);
        mainPanel.add(userIdLabel);

        userIdField = new JTextField();
        userIdField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        userIdField.setBounds(50, 170, 320, 42);
        userIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(userIdField);

        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setBounds(50, 230, 60, 25);
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        passwordField.setBounds(50, 255, 320, 42);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(passwordField);

        // 3. 记住密码
        rememberMeBox = new JCheckBox("记住密码");
        rememberMeBox.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        rememberMeBox.setForeground(new Color(80, 80, 80));
        rememberMeBox.setOpaque(false);
        rememberMeBox.setBounds(50, 310, 100, 25);
        mainPanel.add(rememberMeBox);

        // 4. 按钮
        JButton loginBtn = new JButton("登 录");
        loginBtn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(PRIMARY_BLUE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setBounds(50, 355, 320, 45);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(loginBtn);
        
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { loginBtn.setBackground(HOVER_BLUE); }
            public void mouseExited(MouseEvent e) { loginBtn.setBackground(PRIMARY_BLUE); }
        });

        JButton registerBtn = new JButton("注 册");
        registerBtn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        registerBtn.setForeground(PRIMARY_BLUE);
        registerBtn.setBackground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 1));
        registerBtn.setOpaque(true);
        registerBtn.setBounds(50, 415, 320, 45);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(registerBtn);

        registerBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { registerBtn.setBackground(new Color(240, 248, 255)); }
            public void mouseExited(MouseEvent e) { registerBtn.setBackground(Color.WHITE); }
        });

        // 5. 关闭按钮
        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(255, 100, 100));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setOpaque(true);
        closeBtn.setBounds(380, 12, 30, 30);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(closeBtn);

        // 6. 事件绑定
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText();
                String password = new String(passwordField.getPassword());

                User loggedInUser = authService.login(userId, password);

                if (loggedInUser != null) {
                    UserContext.getInstance().setUser(loggedInUser);
                    JOptionPane.showMessageDialog(LoginFrame.this, "登录成功！欢迎 " + (loggedInUser.getUsername() != null ? loggedInUser.getUsername() : "用户"));
                    dispose();
                    try {
                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "账号或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
        });
        
        passwordField.addActionListener(e -> loginBtn.doClick());
        
        // 💡 确保窗口可见
        setVisible(true);
    }

    public static void main(String[] args) {
        // 使用 FlatLaf 主题（可选）
        // com.formdev.flatlaf.FlatLightLaf.setup();
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
