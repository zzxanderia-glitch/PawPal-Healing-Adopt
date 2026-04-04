package healing.pet.ui.sidebar;

import healing.pet.ui.Theme.Theme;
import healing.pet.view.MainFrame;
import healing.pet.ui.content.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {

    private ContentPanel contentPanel;
    private MainFrame mainFrame;
    private JButton homeButton;
    private JButton matchButton;
    private JButton settingButton;

    public SidebarPanel(ContentPanel contentPanel, MainFrame mainFrame) {
        this.contentPanel = contentPanel;
        this.mainFrame = mainFrame;

        applyTheme();
    }

    /**
     * 应用主题到侧边栏
     */
    public void applyTheme() {
        Theme theme = mainFrame.getCurrentTheme();

        setPreferredSize(new Dimension(theme.getSidebarWidth(), 0));
        setBackground(theme.getSidebarBackgroundColor());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 标题
        JLabel titleLabel = new JLabel("菜单");
        titleLabel.setForeground(theme.getSidebarTextColor());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setFont(theme.getMenuFont());
        add(titleLabel);

        // 首页按钮
        homeButton = createStyledButton("首页", "home");
        add(homeButton);

        // 匹配按钮
        matchButton = createStyledButton("智能匹配", "match");
        add(matchButton);

        // 设置按钮
        settingButton = createStyledButton("设置", "setting");
        add(settingButton);

        // 默认选中首页
        setActiveButton(homeButton);
    }

    private JButton createStyledButton(String text, String pageName) {
        Theme theme = mainFrame.getCurrentTheme();

        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(theme.getButtonMaxWidth(), theme.getButtonHeight()));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(theme.createButtonBorder());
        button.setFocusPainted(false);
        button.setBackground(theme.getSidebarButtonBackground());
        button.setForeground(theme.getSidebarTextColor());
        button.setFont(theme.getButtonFont());
        button.setCursor(theme.getButtonCursor());

        // 添加点击监听
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换页面
                if (contentPanel != null) {
                    contentPanel.showPage(pageName);
                }

                // 更新按钮状态
                setActiveButton(button);
            }
        });

        return button;
    }

    private void setActiveButton(JButton activeButton) {
        Theme theme = mainFrame.getCurrentTheme();
        JButton[] buttons = {homeButton, matchButton, settingButton};
        for (JButton btn : buttons) {
            if (btn == activeButton) {
                btn.setBackground(theme.getSidebarButtonActiveBackground());
            } else {
                btn.setBackground(theme.getSidebarButtonBackground());
            }
        }
    }

    /**
     * 更新管理员模式（根据权限显示/隐藏管理员功能）
     * @param isAdmin true=管理员，false=普通用户
     */
    public void updateAdminMode(boolean isAdmin) {
        // 这里可以根据管理员权限显示或隐藏特定按钮
        // 例如：管理员可能有额外的管理页面入口
        // 目前先保留基础结构，后续可扩展
        revalidate();
        repaint();
    }
}
