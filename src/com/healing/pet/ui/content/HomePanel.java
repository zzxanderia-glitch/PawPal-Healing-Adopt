package com.healing.pet.model.TestConnection.Theme.ui.content;



import com.healing.pet.model.TestConnection.Theme.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    private MainFrame mainFrame;

    public HomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        applyTheme();
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("首页", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        titleLabel.setBorder(mainFrame.getCurrentTheme().createTitleBorder());
        add(titleLabel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea("欢迎使用本系统！\n这是首页内容区域。");
        textArea.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        textArea.setBorder(mainFrame.getCurrentTheme().createContentBorder());
        textArea.setEditable(false);
        textArea.setFont(mainFrame.getCurrentTheme().getContentFont());
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    /**
     * 应用主题到首页面板
     */
    public void applyTheme() {
        if (mainFrame != null) {
            setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        } else {
            setBackground(Color.WHITE);
        }
    }
}
