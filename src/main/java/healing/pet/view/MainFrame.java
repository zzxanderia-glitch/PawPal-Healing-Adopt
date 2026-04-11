package healing.pet.view;

import healing.pet.ui.Theme.LightTheme;
import healing.pet.ui.Theme.Theme;
import healing.pet.ui.content.ContentPanel;
import healing.pet.ui.sidebar.SidebarPanel;
import healing.pet.util.UserContext; // 确保导入了单例类

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * 🐾 萌友速配 - 核心主框架
 * 组长全局控制版：实现权限物理分流与主题动态适配
 */
public class MainFrame extends JFrame {

    private SidebarPanel sidebarPanel;
    private ContentPanel contentPanel;
    private Theme currentTheme;

    public MainFrame() throws SQLException {
        // 1. 初始化治愈系主题
        currentTheme = new LightTheme();
        currentTheme.applyTheme();

        // 2. 窗口基础设置
        setTitle("萌友速配 - 温暖归宿平台");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(currentTheme.getWindowWidth(), currentTheme.getWindowHeight());
        setLocationRelativeTo(null); // 居中
        setLayout(new BorderLayout());

        // 3. 首次加载布局
        initLayout();

        setVisible(true);
    }

    /**
     * 【组长核心方法】初始化/物理重建布局
     * 无论身份切换还是主题切换，调用此方法即可刷新全身
     */
    public void initLayout() {
        // A. 移除当前窗口内所有组件（侧边栏、中心区、状态栏等）
        getContentPane().removeAll();

        // B. 获取全局最新身份
        boolean isAdmin = UserContext.getInstance().isAdmin();

        try {
            // C. 重新创建并装配组件
            // 此时 SidebarPanel 构造时会检查 UserContext，从而决定显示哪些按钮
            contentPanel = new ContentPanel(this);
            contentPanel.setAdminMode(isAdmin);

            sidebarPanel = new SidebarPanel(contentPanel, this);
            sidebarPanel.updateAdminMode(isAdmin);

            // D. 重新塞回窗口
            add(sidebarPanel, BorderLayout.WEST);
            add(contentPanel, BorderLayout.CENTER);

        } catch (SQLException e) {
            System.err.println("刷新布局时数据库连接异常: " + e.getMessage());
        }

        // E. 强制通知 Swing 重新计算布局并重绘
        revalidate();
        repaint();
    }

    /**
     * 【任务要求：权限切换核心入口】
     * 场景：用户在 SettingPanel 点击“身份切换”并验证成功后调用
     */
    public void updateAdminPermission(boolean isAdmin) {
        // 1. 同步修改全局状态机（大脑）
        UserContext.getInstance().setAdminMode(isAdmin);

        // 2. 异步刷新 UI，防止界面卡死
        SwingUtilities.invokeLater(() -> {
            initLayout();

            // 3. 权限切换后的引导逻辑
            if (isAdmin) {
                contentPanel.showPage("admin_pet"); // 自动跳到管理后台
                JOptionPane.showMessageDialog(this, "✨ 欢迎进入管理系统，已为您开启最高权限。");
            } else {
                contentPanel.showPage("home");      // 自动跳回首页
                JOptionPane.showMessageDialog(this, "🌱 已回到用户模式，请尽情挑选有缘的萌友吧。");
            }
        });
    }

    /**
     * 动态切换主题
     */
    public void applyTheme(Theme newTheme) {
        this.currentTheme = newTheme;
        SwingUtilities.invokeLater(() -> {
            try {
                // 应用 UI 渲染
                newTheme.applyTheme();
                // 彻底刷新布局（背景色、按钮色等）
                initLayout();
                // 更新窗口尺寸
                setSize(newTheme.getWindowWidth(), newTheme.getWindowHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Theme getCurrentTheme() {
        return currentTheme;
    }
    public void refreshContent() {
        if (contentPanel != null) {
            contentPanel.refresh();
        }
    }

    // 这里不再使用本地 isAdmin 变量，始终从 UserContext 获取，保证一致性
    public boolean isAdmin() {
        return UserContext.getInstance().isAdmin();
    }

    /**
     * 🚀 项目发动机
     */
    public static void main(String[] args) {
        try {
            // 设置皮肤（如果 pom 刷新过，这里不报错）
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("FlatLaf 加载失败");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "数据库启动失败，请检查连接！");
            }
        });
    }
}