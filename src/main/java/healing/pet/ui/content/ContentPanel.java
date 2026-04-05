package healing.pet.ui.content;

import healing.pet.ui.utils.HealingPetMatching;
import healing.pet.view.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * 内容面板：基于 CardLayout 实现页面切换
 * 组长控制中心：整合首页、匹配、设置，以及管理员专用的管理与审批页面
 */
public class ContentPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentCards;
    private MainFrame mainFrame;
    private boolean isAdmin;

    public ContentPanel(MainFrame mainFrame) throws SQLException {
        this.mainFrame = mainFrame;
        this.isAdmin = false;

        initCardLayout(); // 1. 先初始化布局容器
        addAllPages();    // 2. 再把所有零件（Panel）塞进去
        applyTheme();     // 3. 最后应用皮肤颜色
    }

    public void applyTheme() {
        if (mainFrame != null && mainFrame.getCurrentTheme() != null) {
            Color bg = mainFrame.getCurrentTheme().getContentBackgroundColor();
            this.setBackground(bg);
            if (contentCards != null) {
                contentCards.setBackground(bg);
            }
        }
    }

    private void initCardLayout() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false); // 设为透明，显示父容器背景
        add(contentCards, BorderLayout.CENTER);
    }

    /**
     * 【组长核心改动】在这里把管理员的新页面注册进来
     */
    private void addAllPages() throws SQLException {
        contentCards.add(new HomePanel(mainFrame), "home");
        contentCards.add(new MatchPanel(mainFrame), "match");
        contentCards.add(new SettingPanel(mainFrame), "setting");

        // 💡 修复：这里应该添加 AdminPetPanel（容器），而不是 AdminPetRowPanel（单行组件）
        contentCards.add(new AdminPetPanel(mainFrame), "admin_pet");
        contentCards.add(new AdminAuditPanel(mainFrame), "admin_audit");
    }

    /**
     * 页面切换（侧边栏点击后调用的唯一接口）
     */
    public void showPage(String pageName) {
        // 安全拦截：如果是管理员页面但当前不是管理员权限，跳提示（可选）
        if (pageName.startsWith("admin_") && !isAdmin) {
            JOptionPane.showMessageDialog(mainFrame, "⚠️ 权限不足，请先验证管理员身份。");
            return;
        }
        cardLayout.show(contentCards, pageName);
        
        // 💡 修复：切换回首页时，异步重置滚动条到顶部
        if ("home".equals(pageName)) {
            SwingUtilities.invokeLater(() -> {
                for (Component comp : contentCards.getComponents()) {
                    if (comp instanceof HomePanel) {
                        ((HomePanel) comp).resetScrollToTop();
                        break;
                    }
                }
            });
        }
    }

    /**
     * 管理员权限切换
     */
    public void setAdminMode(boolean isAdmin) {
        this.isAdmin = isAdmin;

        // 💡 组长贴心设计：切换成功后自动跳转，提升交互感
        if (isAdmin) {
            showPage("admin_pet"); // 变成管理员后，自动跳到宠物管理页
        } else {
            showPage("home");      // 退出管理员后，跳回首页
        }

        revalidate();
        repaint();
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}