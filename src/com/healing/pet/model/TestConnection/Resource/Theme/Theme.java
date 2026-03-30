package com.healing.pet.model.TestConnection.Resource.Theme;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class Theme {

    // ========== 颜色配置 ==========

    // 侧边栏颜色
    protected Color sidebarBackgroundColor;
    protected Color sidebarTextColor;
    protected Color sidebarButtonBackground;
    protected Color sidebarButtonActiveBackground;
    protected Color sidebarButtonHoverBackground;

    // 内容区域颜色
    protected Color contentBackgroundColor;
    protected Color contentTextColor;
    protected Color contentTitleColor;

    // 主框架颜色
    protected Color frameBackgroundColor;
    protected Color frameTitleColor;

    // 按钮颜色
    protected Color buttonBackground;
    protected Color buttonForeground;
    protected Color buttonActiveBackground;

    // 边框颜色
    protected Color borderColor;
    protected Color separatorColor;

    // ========== 字体配置 ==========

    // 字体名称
    protected String fontName;

    // 字体大小
    protected int titleFontSize;
    protected int subtitleFontSize;
    protected int buttonFontSize;
    protected int contentFontSize;
    protected int menuFontSize;

    // 字体样式
    protected int titleFontStyle;
    protected int subtitleFontStyle;
    protected int buttonFontStyle;
    protected int contentFontStyle;

    // ========== 尺寸配置 ==========

    // 窗口尺寸
    protected int windowWidth;
    protected int windowHeight;

    // 侧边栏尺寸
    protected int sidebarWidth;
    protected int buttonHeight;
    protected int buttonMaxWidth;

    // 边距配置
    protected int titleMarginTop;
    protected int titleMarginBottom;
    protected int contentMargin;
    protected int buttonMarginTop;
    protected int buttonMarginLeft;
    protected int buttonMarginRight;

    // ========== 光标配置 ==========

    protected Cursor buttonCursor;

    // ========== 构造函数 ==========

    public Theme() {
        initializeDefaults();
    }

    /**
     * 初始化默认配置
     */
    protected void initializeDefaults() {
        // 颜色默认值 - 粉色主题
        sidebarBackgroundColor = new Color(0xD799C2);
        sidebarTextColor = Color.WHITE;
        sidebarButtonBackground = new Color(255, 250, 205);
        sidebarButtonActiveBackground = new Color(255, 215, 0);
        sidebarButtonHoverBackground = new Color(255, 239, 150);

        contentBackgroundColor = Color.WHITE;
        contentTextColor = Color.BLACK;
        contentTitleColor = Color.DARK_GRAY;

        frameBackgroundColor = Color.WHITE;
        frameTitleColor = Color.DARK_GRAY;

        buttonBackground = new Color(255, 250, 205);
        buttonForeground = Color.WHITE;
        buttonActiveBackground = new Color(255, 215, 0);

        borderColor = new Color(200, 200, 200);
        separatorColor = new Color(220, 220, 220);

        // 字体默认值
        fontName = "Microsoft YaHei";

        titleFontSize = 28;
        subtitleFontSize = 18;
        buttonFontSize = 14;
        contentFontSize = 16;
        menuFontSize = 14;

        titleFontStyle = Font.BOLD;
        subtitleFontStyle = Font.BOLD;
        buttonFontStyle = Font.PLAIN;
        contentFontStyle = Font.PLAIN;

        // 尺寸默认值
        windowWidth = 1200;
        windowHeight = 800;

        sidebarWidth = 250;
        buttonHeight = 45;
        buttonMaxWidth = 200;

        titleMarginTop = 50;
        titleMarginBottom = 20;
        contentMargin = 30;
        buttonMarginTop = 10;
        buttonMarginLeft = 20;
        buttonMarginRight = 20;

        // 光标默认值
        buttonCursor = new Cursor(Cursor.HAND_CURSOR);
    }

    /**
     * 应用主题到 UI 组件
     */
    public abstract void applyTheme();

    /**
     * 获取侧边栏背景颜色
     */
    public Color getSidebarBackgroundColor() {
        return sidebarBackgroundColor;
    }

    /**
     * 获取侧边栏文字颜色
     */
    public Color getSidebarTextColor() {
        return sidebarTextColor;
    }

    /**
     * 获取侧边栏按钮背景颜色
     */
    public Color getSidebarButtonBackground() {
        return sidebarButtonBackground;
    }

    /**
     * 获取侧边栏激活按钮背景颜色
     */
    public Color getSidebarButtonActiveBackground() {
        return sidebarButtonActiveBackground;
    }

    /**
     * 获取内容区域背景颜色
     */
    public Color getContentBackgroundColor() {
        return contentBackgroundColor;
    }

    /**
     * 获取内容区域文字颜色
     */
    public Color getContentTextColor() {
        return contentTextColor;
    }

    /**
     * 获取标题字体
     */
    public Font getTitleFont() {
        return new Font(fontName, titleFontStyle, titleFontSize);
    }

    /**
     * 获取副标题字体
     */
    public Font getSubtitleFont() {
        return new Font(fontName, subtitleFontStyle, subtitleFontSize);
    }

    /**
     * 获取按钮字体
     */
    public Font getButtonFont() {
        return new Font(fontName, buttonFontStyle, buttonFontSize);
    }

    /**
     * 获取内容字体
     */
    public Font getContentFont() {
        return new Font(fontName, contentFontStyle, contentFontSize);
    }

    /**
     * 获取菜单字体
     */
    public Font getMenuFont() {
        return new Font(fontName, Font.PLAIN, menuFontSize);
    }

    /**
     * 获取窗口宽度
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * 获取窗口高度
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * 获取侧边栏宽度
     */
    public int getSidebarWidth() {
        return sidebarWidth;
    }

    /**
     * 获取按钮高度
     */
    public int getButtonHeight() {
        return buttonHeight;
    }

    /**
     * 获取按钮最大宽度
     */
    public int getButtonMaxWidth() {
        return buttonMaxWidth;
    }

    /**
     * 获取按钮光标
     */
    public Cursor getButtonCursor() {
        return buttonCursor;
    }

    /**
     * 获取边框颜色
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * 获取分隔线颜色
     */
    public Color getSeparatorColor() {
        return separatorColor;
    }

    /**
     * 创建空边框
     */
    public Border createEmptyBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    /**
     * 创建标题边框
     */
    public Border createTitleBorder() {
        return BorderFactory.createEmptyBorder(titleMarginTop, 0, titleMarginBottom, 0);
    }

    /**
     * 创建内容边框
     */
    public Border createContentBorder() {
        return BorderFactory.createEmptyBorder(contentMargin, contentMargin, contentMargin, contentMargin);
    }

    /**
     * 创建按钮边框
     */
    public Border createButtonBorder() {
        return BorderFactory.createEmptyBorder(buttonMarginTop, buttonMarginLeft, buttonMarginTop, buttonMarginRight);
    }
}
