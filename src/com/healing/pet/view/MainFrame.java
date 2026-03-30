package com.healing.pet.view;

import javax.swing.*;
import java.awt.*;

/**
 * 项目主窗体 —— 由组长搭建框架
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        // 1. 设置窗口基本属性
        setTitle("🐾 萌友速配 - 温暖归宿平台");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中

        // 2. 设置布局
        setLayout(new BorderLayout());

        // 3. 顶部：欢迎语（治愈系文案）
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(255, 245, 225)); // 奶黄色背景
        JLabel welcomeLabel = new JLabel("每一个生命都值得被温柔以待 ✨");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        // 4. 中间：内容展示区（以后放宠物卡片）
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        centerPanel.setBackground(Color.WHITE);

        // 临时放一个按钮测试一下
        JButton testBtn = new JButton("点我开启缘分匹配");
        centerPanel.add(testBtn);

        add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        // 5. 底部：状态栏
        JLabel statusLabel = new JLabel("  当前数据库状态：已连接 ❤️");
        statusLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        add(statusLabel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // 组长温馨提示：这里以后要切换皮肤（FlatLaf）
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}