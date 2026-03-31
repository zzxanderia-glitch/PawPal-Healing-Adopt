package com.healing.pet.ui.Theme;
import javax.swing.*;
import java.awt.*;

public class DarkTheme extends Theme {

    public DarkTheme() {
        super();
        initializeDarkTheme();
    }

    /**
     * 初始化暗色主题配置
     */
    private void initializeDarkTheme() {
        // 侧边栏颜色 - 深灰色背景
        sidebarBackgroundColor = new Color(45, 45, 48);
        sidebarTextColor = new Color(220, 220, 220);
        sidebarButtonBackground = new Color(60, 60, 65);
        sidebarButtonActiveBackground = new Color(100, 150, 200);
        sidebarButtonHoverBackground = new Color(75, 75, 80);

        // 内容区域颜色 - 深色背景
        contentBackgroundColor = new Color(30, 30, 30);
        contentTextColor = new Color(220, 220, 220);
        contentTitleColor = new Color(180, 180, 180);

        // 主框架颜色
        frameBackgroundColor = new Color(30, 30, 30);
        frameTitleColor = new Color(200, 200, 200);

        // 按钮颜色
        buttonBackground = new Color(60, 60, 65);
        buttonForeground = new Color(220, 220, 220);
        buttonActiveBackground = new Color(100, 150, 200);

        // 边框颜色 - 较浅的灰色
        borderColor = new Color(80, 80, 85);
        separatorColor = new Color(70, 70, 75);

        // 字体设置 - 使用浅色文字
        fontName = "Microsoft YaHei";

        titleFontStyle = Font.BOLD;
        subtitleFontStyle = Font.BOLD;
        buttonFontStyle = Font.PLAIN;
        contentFontStyle = Font.PLAIN;
    }

    @Override
    public void applyTheme() {
        try {
            // 设置 FlatLaf 暗色主题
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());

            // 自定义 UI 默认值
            UIManager.put("Panel.background", contentBackgroundColor);
            UIManager.put("Label.foreground", contentTextColor);
            UIManager.put("Button.background", buttonBackground);
            UIManager.put("Button.foreground", buttonForeground);
            UIManager.put("TextField.background", new Color(50, 50, 55));
            UIManager.put("TextField.foreground", contentTextColor);
            UIManager.put("TextArea.background", new Color(50, 50, 55));
            UIManager.put("TextArea.foreground", contentTextColor);
            UIManager.put("Table.background", contentBackgroundColor);
            UIManager.put("Table.foreground", contentTextColor);
            UIManager.put("TableHeader.background", new Color(50, 50, 55));
            UIManager.put("TableHeader.foreground", contentTextColor);
            UIManager.put("ScrollBar.background", new Color(50, 50, 55));
            UIManager.put("ComboBox.background", new Color(50, 50, 55));
            UIManager.put("ComboBox.foreground", contentTextColor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
