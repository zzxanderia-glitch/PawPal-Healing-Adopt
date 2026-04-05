package healing.pet.ui.sidebar;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import healing.pet.ui.Theme.Theme;
import healing.pet.view.MainFrame;
import healing.pet.ui.content.ContentPanel;
import healing.pet.util.UserContext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 导航侧边栏 —— 具备身份动态反馈变色功能
 */
public class SidebarPanel extends JPanel {

    private ContentPanel contentPanel;
    private MainFrame mainFrame;
    private List<JButton> allMenuButtons = new ArrayList<>();

    public SidebarPanel(ContentPanel contentPanel, MainFrame mainFrame) {
        this.contentPanel = contentPanel;
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        this.removeAll();
        allMenuButtons.clear();

        Theme theme = mainFrame.getCurrentTheme();
        boolean isAdmin = UserContext.getInstance().isAdmin();

        // 💡 修复：无论是否管理员，侧边栏颜色都严格跟随主题设置
        Color bgColor = theme.getSidebarBackgroundColor();
        Color textColor = theme.getSidebarTextColor();
        Color activeBg = theme.getSidebarButtonActiveBackground();

        setBackground(bgColor);
        setPreferredSize(new Dimension(theme.getSidebarWidth(), 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 顶部留白
        add(Box.createVerticalStrut(30));

        // 标题
        JLabel titleLabel = new JLabel("导航菜单");
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        add(titleLabel);

        add(Box.createVerticalStrut(30));

        // --- 按钮分流加载 ---
        // 💡 管理员不显示首页，只显示管理工具
        if (!isAdmin) {
            JButton homeBtn = createStyledButton("首页中心", "home", "homepage.svg", isAdmin);
            add(homeBtn);
            allMenuButtons.add(homeBtn);
            add(Box.createVerticalStrut(10));
        }

        if (isAdmin) {
            JLabel adminTag = new JLabel("管理工具");
            adminTag.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            adminTag.setForeground(new Color(150, 160, 170));
            adminTag.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(adminTag);
            add(Box.createVerticalStrut(10));

            JButton manageBtn = createStyledButton("宠物管理", "admin_pet", "bone.svg", isAdmin);
            JButton auditBtn = createStyledButton("领养审批", "admin_audit", "footprint.svg", isAdmin);
            add(manageBtn); allMenuButtons.add(manageBtn);
            add(Box.createVerticalStrut(10));
            add(auditBtn); allMenuButtons.add(auditBtn);
            add(Box.createVerticalStrut(10));
        } else {
            JButton matchBtn = createStyledButton("智能匹配", "match", "footprint.svg", isAdmin);
            add(matchBtn);
            allMenuButtons.add(matchBtn);
            add(Box.createVerticalStrut(10));
        }

        // 底部设置
        add(Box.createVerticalGlue());
        JButton settingBtn = createStyledButton("系统设置", "setting", "setting.svg", isAdmin);
        add(settingBtn);
        allMenuButtons.add(settingBtn);
        add(Box.createVerticalStrut(20));

        // 💡 修复：根据身份动态设置默认选中项
        if (isAdmin) {
            for (JButton btn : allMenuButtons) {
                if (btn.getText().contains("宠物管理")) {
                    setActiveButton(btn);
                    break;
                }
            }
        } else {
            for (JButton btn : allMenuButtons) {
                if (btn.getText().contains("首页")) {
                    setActiveButton(btn);
                    break;
                }
            }
        }
        
        revalidate();
        repaint();
    }

    private JButton createStyledButton(String text, String pageName, String svgName, boolean isAdminMode) {
        Theme theme = mainFrame.getCurrentTheme();
        FlatSVGIcon icon = new FlatSVGIcon("icon/" + svgName, 18, 18);

        // 💡 图标颜色跟随主题文字颜色，而非硬编码白色
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> theme.getSidebarTextColor()));

        JButton button = new JButton(text, icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        button.setMargin(new Insets(0, 30, 0, 0));
        button.setMaximumSize(new Dimension(theme.getButtonMaxWidth(), theme.getButtonHeight()));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);

        // 初始颜色
        button.setBackground(new Color(0, 0, 0, 0));
        button.setForeground(theme.getSidebarTextColor());
        button.setFont(theme.getButtonFont());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            if (contentPanel != null) contentPanel.showPage(pageName);
            setActiveButton(button);
        });

        return button;
    }

    private void setActiveButton(JButton activeButton) {
        Theme theme = mainFrame.getCurrentTheme();
        for (JButton btn : allMenuButtons) {
            if (btn == activeButton) {
                // 选中态颜色跟随主题配置
                btn.setBackground(theme.getSidebarButtonActiveBackground());
                btn.setOpaque(true);
            } else {
                btn.setOpaque(false);
                btn.setBackground(new Color(0, 0, 0, 0));
            }
        }
    }

    public void updateAdminMode(boolean isAdmin) {
        buildUI();
    }

    public void applyTheme() {
        buildUI();
    }
}