package healing.pet.ui.Theme;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class LightTheme extends Theme {

    public LightTheme() {
        super();
        initializeLightTheme();
    }

    /**
     * 初始化亮色主题配置
     */
    private void initializeLightTheme() {
        // 侧边栏颜色 - 粉色主题（原项目配色）
        sidebarBackgroundColor = new Color(0xD799C2);
        sidebarTextColor = Color.white;
        sidebarButtonBackground = new Color(215, 153, 194);
        sidebarButtonActiveBackground = new Color(206, 118, 178);
        sidebarButtonHoverBackground = new Color(210, 142, 187, 255);

        // 内容区域颜色
        contentBackgroundColor =new Color(220, 178, 207);
        contentTextColor = Color.black;
        contentTitleColor = new Color(60, 60, 60);

        // 主框架颜色
        frameBackgroundColor = Color.WHITE;
        frameTitleColor = new Color(40, 40, 40);

        // 按钮颜色
        buttonBackground = new Color(255, 250, 205);
        buttonForeground = new Color(80, 80, 80);
        buttonActiveBackground = new Color(255, 215, 0);

        // 边框颜色 - 浅灰色
        borderColor = new Color(220, 220, 220);
        separatorColor = new Color(240, 240, 240);

        // 字体设置
        fontName = "Microsoft YaHei";

        titleFontStyle = Font.BOLD;
        subtitleFontStyle = Font.BOLD;
        buttonFontStyle = Font.PLAIN;
        contentFontStyle = Font.PLAIN;
    }

    @Override
    public void applyTheme() {
        try {
            // 设置 FlatLaf 亮色主题
            UIManager.setLookAndFeel(new FlatLightLaf());

            // 自定义 UI 默认值
            UIManager.put("Panel.background", contentBackgroundColor);
            UIManager.put("Label.foreground", contentTextColor);
            UIManager.put("Button.background", buttonBackground);
            UIManager.put("Button.foreground", buttonForeground);
            UIManager.put("TextField.background", new Color(250, 250, 250));
            UIManager.put("TextField.foreground", contentTextColor);
            UIManager.put("TextArea.background", new Color(250, 250, 250));
            UIManager.put("TextArea.foreground", contentTextColor);
            UIManager.put("Table.background", contentBackgroundColor);
            UIManager.put("Table.foreground", contentTextColor);
            UIManager.put("TableHeader.background", new Color(245, 245, 245));
            UIManager.put("TableHeader.foreground", contentTextColor);
            UIManager.put("ScrollBar.background", new Color(245, 245, 245));
            UIManager.put("ComboBox.background", new Color(250, 250, 250));
            UIManager.put("ComboBox.foreground", contentTextColor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
