package healing.pet.view;

import healing.pet.ui.Theme.LightTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.ui.content.ContentPanel;
import healing.pet.ui.sidebar.SidebarPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private SidebarPanel sidebarPanel;
    private ContentPanel contentPanel;
    private Theme currentTheme;
    // 新增：管理员权限状态（任务要求：身份切换后更新）
    private boolean isAdmin = false;


    /**
     * 动态切换主题（多态应用，原有方法保留）
     */

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
        contentPanel.setAdminMode(isAdmin); // 设置初始权限状态
        add(contentPanel, BorderLayout.CENTER);

        // 2. 左侧导航栏
        sidebarPanel = new SidebarPanel(contentPanel, this);
        add(sidebarPanel, BorderLayout.WEST);

        setVisible(true);
    }


    /**
     * 动态切换主题（多态应用，原有方法保留）
     */
    public void applyTheme(Theme newTheme) {
        this.currentTheme = newTheme;
        SwingUtilities.invokeLater(() -> {
            try {
                getContentPane().removeAll();
                newTheme.applyTheme();

                // 重新装配组件
                contentPanel = new ContentPanel(this);
                contentPanel.setAdminMode(isAdmin); // 保持当前权限状态
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

    // ====================== 【任务要求：身份切换后更新权限】 ======================
    /**
     * 更新管理员权限（SettingPanel 身份切换后调用）
     * @param isAdmin true=管理员，false=普通用户
     */
    public void updateAdminPermission(boolean isAdmin) {
        this.isAdmin = isAdmin;
        // 通知内容面板更新权限（显示/隐藏管理员功能）
        if (contentPanel != null) {
            contentPanel.setAdminMode(isAdmin);
        }
        // 通知侧边栏更新权限（可选，根据你的需求）
        if (sidebarPanel != null) {
            sidebarPanel.updateAdminMode(isAdmin);
        }
        // 刷新界面
        revalidate();
        repaint();
    }
    // ============================================================================

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * 项目启动入口
     */
    public static void main(String[] args) {
        try {
            // 初始化FlatLaf皮肤（原有代码保留）
            com.formdev.flatlaf.FlatLightLaf.setup();
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