package com.healing.pet.ui.content;



import com.healing.pet.ui.Theme.DarkTheme;
import com.healing.pet.ui.Theme.LightTheme;
import com.healing.pet.ui.Theme.Theme;
import com.healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SettingPannel extends JPanel {

    private MainFrame mainFrame;
    private JComboBox<String> themeComboBox;

    public SettingPannel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("设置", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // 示例设置项
        JLabel label1 = new JLabel("用户名：");
        label1.setFont(mainFrame.getCurrentTheme().getContentFont());
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(label1, gbc);

        JTextField textField1 = new JTextField("admin");
        textField1.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        formPanel.add(textField1, gbc);

        // 主题选择
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

        // 应用按钮
        /*JButton applyButton = new JButton("应用主题");
        applyButton.setFont(mainFrame.getCurrentTheme().getButtonFont());
        applyButton.setPreferredSize(new Dimension(150, 35));
        applyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        formPanel.add(applyButton, gbc);*/

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        scrollPane.setBorder(BorderFactory.createLineBorder(
            mainFrame.getCurrentTheme().getBorderColor()));
        add(scrollPane, BorderLayout.CENTER);
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

        // 调用 MainFrame 的方法应用新主题
        if (mainFrame != null) {
            mainFrame.applyTheme(newTheme);
        }
    }

    public JComboBox<String> getThemeComboBox() {
        return themeComboBox;
    }
}
