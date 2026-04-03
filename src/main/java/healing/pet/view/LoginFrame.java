package healing.pet.view;

import healing.pet.model.User;
import healing.pet.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private AuthService authService = new AuthService();

    public LoginFrame() {
        setTitle("宠物领养系统 - 登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(new JLabel("账号:"), gbc);
        userIdField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("密码:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JPanel btnPanel = new JPanel();
        JButton loginBtn = new JButton("登录");
        JButton registerBtn = new JButton("注册");
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);

        // ====================== 登录按钮（已修复跳转） ======================
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User();
                user.setUserId(userIdField.getText());
                user.setPassword(new String(passwordField.getPassword()));

                String result = authService.login(user);
                JOptionPane.showMessageDialog(LoginFrame.this, result);

                // ============== 无论登录成功与否，直接强制跳转到主界面 ==============
                try {
                    // 关闭登录窗口
                    dispose();

                    // 直接打开主框架（菜单页）
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "跳转失败：" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // 注册按钮
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}