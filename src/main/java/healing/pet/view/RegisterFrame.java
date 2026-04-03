package healing.pet.view;

import healing.pet.model.User;
import healing.pet.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private AuthService authService = new AuthService();

    public RegisterFrame() {
        setTitle("宠物领养系统 - 注册");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JButton registerBtn = new JButton("注册");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        add(panel);

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User();
                user.setUserId(userIdField.getText());
                user.setPassword(new String(passwordField.getPassword()));

                String result = authService.register(user);
                JOptionPane.showMessageDialog(null, result);

                if ("注册成功".equals(result)) {
                    dispose();
                }
            }
        });
    }
}