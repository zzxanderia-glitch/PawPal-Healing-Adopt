package healing.pet.view; // 1. 修正包名，属于 view 层

// 2. 修正后的干净导入（前提是你已经按照我之前的建议移动了文件）
import com.formdev.flatlaf.FlatLightLaf;
import healing.pet.ui.Theme.LightTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.ui.content.ContentPanel;
import healing.pet.ui.sidebar.SidebarPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * 🐾 萌友速配 - 项目启动主入口
 */
public class MainFrame extends JFrame {

    private SidebarPanel sidebarPanel;
    private ContentPanel contentPanel;
    private Theme currentTheme;

    public MainFrame() throws SQLException {
        // --- 初始化主题 ---
        currentTheme = new LightTheme();
        currentTheme.applyTheme();

        // --- 设置窗口基本属性 ---
        setTitle("萌友速配 - 温暖归宿平台");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(currentTheme.getWindowWidth(), currentTheme.getWindowHeight());
        setLocationRelativeTo(null); // 居中展示
        setLayout(new BorderLayout());

        // --- 初始化核心组件 ---
        // 1. 中央内容区
        contentPanel = new ContentPanel(this);
        add(contentPanel, BorderLayout.CENTER);

        // 2. 左侧导航栏 (注意：修正了 sidebar 的拼写)
        sidebarPanel = new SidebarPanel(contentPanel, this);
        add(sidebarPanel, BorderLayout.WEST);

        setVisible(true);
    }

    /**
     * 动态切换主题（高分 OOP 点：多态的应用）
     */
    public void applyTheme(Theme newTheme) {
        this.currentTheme = newTheme;
        SwingUtilities.invokeLater(() -> {
            try {
                getContentPane().removeAll();
                newTheme.applyTheme();

                // 重新装配
                contentPanel = new ContentPanel(this);
                add(contentPanel, BorderLayout.CENTER);
                sidebarPanel = new SidebarPanel(contentPanel, this);
                add(sidebarPanel, BorderLayout.WEST);

                setSize(newTheme.getWindowWidth(), newTheme.getWindowHeight());
                revalidate();
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * 🚀 项目点火开关
     */
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("皮肤加载失败");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}