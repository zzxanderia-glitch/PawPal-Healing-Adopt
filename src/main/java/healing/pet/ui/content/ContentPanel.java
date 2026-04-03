package healing.pet.ui.content;

import healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ContentPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel contentCards;
    private MainFrame mainFrame;

    public ContentPanel(MainFrame mainFrame) throws SQLException {
        this.mainFrame = mainFrame;

        applyTheme();
        setLayout(new BorderLayout());

        // 使用 CardLayout 实现页面切换
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);

        // 添加不同的面板
        HomePanel homePanel = new HomePanel(mainFrame);
        MatchPanel matchPanel = new MatchPanel(mainFrame);
        SettingPannel settingPanel = new SettingPannel(mainFrame);

        contentCards.add(homePanel, "home");
        contentCards.add(matchPanel, "match");
        contentCards.add(settingPanel, "setting");

        add(contentCards, BorderLayout.CENTER);
    }

    /**
     * 应用主题到内容面板
     */
    public void applyTheme() {
        if (mainFrame != null) {
            setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        } else {
            setBackground(new Color(245, 245, 245));
        }
    }

    public void showPage(String pageName) {
        cardLayout.show(contentCards, pageName);
    }
}
