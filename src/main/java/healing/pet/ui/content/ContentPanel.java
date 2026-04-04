package healing.pet.ui.content;

import healing.pet.view.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * 内容面板：基于CardLayout实现页面切换，整合首页/匹配/设置
 */
public class ContentPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentCards;
    private MainFrame mainFrame;
    private boolean isAdmin; // 管理员权限标识

    // 构造方法：接收主窗口实例，初始化所有页面
    public ContentPanel(MainFrame mainFrame) throws SQLException {
        this.mainFrame = mainFrame;
        this.isAdmin = false; // 默认普通用户
        applyTheme();
        initCardLayout();
        addAllPages();
    }

    // 应用主题样式
    public void applyTheme() {
        if (mainFrame != null) {
            setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        } else {
            setBackground(new Color(245, 245, 245));
        }
    }

    // 初始化卡片布局（核心：页面切换）
    private void initCardLayout() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        add(contentCards, BorderLayout.CENTER);
    }



    // 添加所有业务页面
    private void addAllPages() throws SQLException {
        HomePanel homePanel = new HomePanel(mainFrame);
        MatchPanel matchPanel = new MatchPanel(mainFrame);
        SettingPannel settingPanel = new SettingPannel(mainFrame);

        contentCards.add(homePanel, "home");
        contentCards.add(matchPanel, "match");
        contentCards.add(settingPanel, "setting");
    }


    // 对外提供页面切换方法（供侧边栏调用）
    public void showPage(String pageName) {
        cardLayout.show(contentCards, pageName);
    }

    // 管理员权限切换（供SettingPanel调用）
    public void setAdminMode(boolean isAdmin) {
        this.isAdmin = isAdmin;
        revalidate();
        repaint();
    }

    // 获取当前管理员状态
    public boolean isAdmin() {
        return isAdmin;
    }
}