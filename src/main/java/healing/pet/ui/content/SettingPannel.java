package healing.pet.ui.content;

import healing.pet.ui.Theme.DarkTheme;
import healing.pet.ui.Theme.LightTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * 设置页：主题切换 + 身份切换
 * 修复文件名/类名不匹配、继承类错误、类型不兼容问题
 */
public class SettingPannel extends JPanel {  // 修正：继承JPanel，而非RoundedPanel

    private MainFrame mainFrame;
    private JComboBox<String> themeComboBox;
    private JButton switchRoleBtn;

    public SettingPannel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 应用主题背景
        setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        setLayout(new BorderLayout());

        // 标题
        JLabel titleLabel = new JLabel("设置", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // 1. 用户名
        JLabel label1 = new JLabel("用户名：");
        label1.setFont(mainFrame.getCurrentTheme().getContentFont());
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(label1, gbc);

        JTextField textField1 = new JTextField("admin");
        textField1.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        formPanel.add(textField1, gbc);

        // 2. 主题选择
        JLabel label2 = new JLabel("主题：");
        label2.setFont(mainFrame.getCurrentTheme().getContentFont());
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(label2, gbc);

        String[] themes = {"浅色主题", "暗色主题"};
        themeComboBox = new JComboBox<>(themes);
        themeComboBox.setPreferredSize(new Dimension(200, 30));
        themeComboBox.setFont(mainFrame.getCurrentTheme().getContentFont());
        themeComboBox.addActionListener(e -> changeTheme());
        gbc.gridx = 1;
        formPanel.add(themeComboBox, gbc);

        // 3. 身份切换（任务要求）
        JLabel roleLabel = new JLabel("身份切换：");
        roleLabel.setFont(mainFrame.getCurrentTheme().getContentFont());
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(roleLabel, gbc);

        switchRoleBtn = new JButton("切换至管理员");
        switchRoleBtn.setFont(mainFrame.getCurrentTheme().getButtonFont());
        switchRoleBtn.setPreferredSize(new Dimension(200, 35));
        switchRoleBtn.addActionListener(e -> switchRole());
        gbc.gridx = 1;
        formPanel.add(switchRoleBtn, gbc);

        // 滚动面板
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        scrollPane.setBorder(BorderFactory.createLineBorder(mainFrame.getCurrentTheme().getBorderColor()));
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 身份切换逻辑
     */
    private void switchRole() {
        if (switchRoleBtn.getText().equals("切换至管理员")) {
            JPasswordField pf = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                    this,
                    pf,
                    "请输入管理员密码",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                String pwd = new String(pf.getPassword());
                if ("123456".equals(pwd)) {
                    switchRoleBtn.setText("切换至普通用户");
                    JOptionPane.showMessageDialog(this, "✅ 已切换为管理员身份", "成功", JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.updateAdminPermission(true);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ 密码错误，切换失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            switchRoleBtn.setText("切换至管理员");
            JOptionPane.showMessageDialog(this, "✅ 已切换为普通用户身份", "成功", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.updateAdminPermission(false);
        }
    }

    /**
     * 切换主题
     */
    private void changeTheme() {
        int selectedIndex = themeComboBox.getSelectedIndex();
        Theme newTheme;

        if (selectedIndex == 0) {
            newTheme = new LightTheme();
        } else {
            newTheme = new DarkTheme();
        }

        if (mainFrame != null) {
            mainFrame.applyTheme(newTheme);
        }
    }

    // Getter
    public JComboBox<String> getThemeComboBox() {
        return themeComboBox;
    }
}