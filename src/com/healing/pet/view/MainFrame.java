package com.healing.pet.model.TestConnection.Theme.ui;

import com.healing.pet.model.TestConnection.Theme.Theme.LightTheme;
import com.healing.pet.model.TestConnection.Theme.Theme.Theme;
import com.healing.pet.model.TestConnection.Theme.ui.content.ContentPanel;
import com.healing.pet.model.TestConnection.Theme.ui.siderbar.SidebarPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private SidebarPanel sidebarPanel;
    private ContentPanel contentPanel;
    private Theme currentTheme;

    public MainFrame() {
        super("Java Swing Application");

        // 初始化默认主题
        currentTheme = new LightTheme();
        currentTheme.applyTheme();

        // 设置窗口基本属性
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(currentTheme.getWindowWidth(), currentTheme.getWindowHeight());
        setLocationRelativeTo(null);

        // 使用 BorderLayout 布局
        setLayout(new BorderLayout());

        // 初始化内容面板（放在中央）
        contentPanel = new ContentPanel(this);
        add(contentPanel, BorderLayout.CENTER);

        // 初始化侧边栏面板（放在西侧，即左侧）- 需要传入 contentPanel
        sidebarPanel = new SidebarPanel(contentPanel, this);
        add(sidebarPanel, BorderLayout.WEST);

        setVisible(true);
    }

    /**
     * 应用新主题
     */
    public void applyTheme(Theme newTheme) {
        this.currentTheme = newTheme;
        SwingUtilities.invokeLater(() -> {
            try {
                // 重新创建窗口内容
                getContentPane().removeAll();

                // 应用新主题
                newTheme.applyTheme();

                // 重新初始化组件
                contentPanel = new ContentPanel(this);
                add(contentPanel, BorderLayout.CENTER);

                sidebarPanel = new SidebarPanel(contentPanel, this);
                add(sidebarPanel, BorderLayout.WEST);

                // 更新窗口尺寸
                setSize(newTheme.getWindowWidth(), newTheme.getWindowHeight());

                revalidate();
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取当前主题
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
