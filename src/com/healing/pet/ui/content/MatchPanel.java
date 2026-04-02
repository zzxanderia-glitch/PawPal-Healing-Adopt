package com.healing.pet.ui.content;

import com.healing.pet.model.Animal;
import com.healing.pet.service.MatchService;
import com.healing.pet.service.UserPreferences;
import com.healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MatchPanel extends JPanel {

    private MainFrame mainFrame;
    private MatchService matchService;

    private JComboBox<String> personalityCombo;
    private JComboBox<String> livingSpaceCombo;
    private JComboBox<String> companionTimeCombo;
    private JButton matchButton;
    private JPanel resultPanel;

    public MatchPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.matchService = new MatchService();

        setLayout(new BorderLayout(15, 15));
        setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 标题
        JLabel titleLabel = new JLabel("🎯 智能匹配 - 找到你的专属萌宠", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        add(titleLabel, BorderLayout.NORTH);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        // 性格偏好
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("性格偏好："), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] personalities = {"请选择", "温顺", "活泼", "独立", "聪明", "粘人"};
        personalityCombo = createComboBox(personalities);
        formPanel.add(personalityCombo, gbc);

        // 居住面积
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("居住面积："), gbc);

        gbc.gridx = 1;
        String[] spaces = {"请选择", "公寓", "别墅", "小户型", "大空间"};
        livingSpaceCombo = createComboBox(spaces);
        formPanel.add(livingSpaceCombo, gbc);

        // 陪伴时间
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("陪伴时间："), gbc);

        gbc.gridx = 1;
        String[] times = {"请选择", "多", "中", "少"};
        companionTimeCombo = createComboBox(times);
        formPanel.add(companionTimeCombo, gbc);

        // 匹配按钮
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        matchButton = new JButton("🚀 开始匹配");
        matchButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        matchButton.setPreferredSize(new Dimension(200, 45));
        matchButton.setBackground(mainFrame.getCurrentTheme().getSidebarButtonActiveBackground());
        matchButton.setForeground(Color.WHITE);
        matchButton.setFocusPainted(false);
        matchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        matchButton.addActionListener(e -> performMatch());
        formPanel.add(matchButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        // 结果面板
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(mainFrame.getCurrentTheme().getBorderColor()),
                        "匹配结果",
                        SwingConstants.LEFT,
                        SwingConstants.TOP
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setPreferredSize(new Dimension(0, 350));
        add(scrollPane, BorderLayout.CENTER);

        // 初始提示
        showInitialMessage();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(mainFrame.getCurrentTheme().getContentFont());
        return label;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setPreferredSize(new Dimension(200, 35));
        combo.setFont(mainFrame.getCurrentTheme().getContentFont());
        return combo;
    }

    private void performMatch() {
        String personality = (String) personalityCombo.getSelectedItem();
        String livingSpace = (String) livingSpaceCombo.getSelectedItem();
        String companionTime = (String) companionTimeCombo.getSelectedItem();

        if ("请选择".equals(personality) || "请选择".equals(livingSpace) || "请选择".equals(companionTime)) {
            JOptionPane.showMessageDialog(this, "请完成所有选项！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserPreferences preferences = new UserPreferences(personality, livingSpace, companionTime);

        // 调用 MatchService 的 match 方法进行匹配
        List<Animal> matchedPets = matchService.match(preferences);

        displayResults(matchedPets, preferences);
    }

    private void displayResults(List<Animal> pets, UserPreferences preferences) {
        resultPanel.removeAll();

        if (pets == null || pets.isEmpty()) {
            resultPanel.add(new JLabel("没有找到匹配的宠物", SwingConstants.CENTER));
        } else {
            resultPanel.add(createSummaryLabel(pets.size(), preferences));
            resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            for (int i = 0; i < pets.size(); i++) {
                Animal pet = pets.get(i);
                resultPanel.add(createPetCard(pet, i + 1));
                resultPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JLabel createSummaryLabel(int count, UserPreferences prefs) {
        JLabel label = new JLabel(
                "<html><div style='text-align:center; padding:10px;'>" +
                        "✅ 为您找到 <b>" + count + "</b> 只匹配的宠物！</br>" +
                        "偏好：" + prefs.getPersonality() + "、" + prefs.getLivingSpace() + "、" + prefs.getCompanionTime() +
                        "</div></html>",
                SwingConstants.CENTER
        );
        label.setFont(mainFrame.getCurrentTheme().getContentFont());
        return label;
    }

    private JPanel createPetCard(Animal pet, int rank) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getRankColor(rank), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 排名
        JLabel rankLabel = new JLabel("#" + rank, SwingConstants.CENTER);
        rankLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 28));
        rankLabel.setForeground(getRankColor(rank));
        rankLabel.setPreferredSize(new Dimension(60, 60));
        card.add(rankLabel, BorderLayout.WEST);

        // 信息
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        infoPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        infoPanel.add(createInfoLabel("名字："));
        infoPanel.add(createValueLabel(pet.getName()));
        infoPanel.add(createInfoLabel("类型："));
        infoPanel.add(createValueLabel(pet.getClass().getSimpleName()));
        infoPanel.add(createInfoLabel("照顾指南："));
        infoPanel.add(createValueLabel(pet.getCareGuide()));
        card.add(infoPanel, BorderLayout.CENTER);

        // 按钮
        JButton voiceBtn = new JButton("🔊 听叫声");
        voiceBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, pet.getVoice())
        );
        card.add(voiceBtn, BorderLayout.EAST);

        return card;
    }

    private Color getRankColor(int rank) {
        if (rank == 1) return new Color(255, 215, 0);
        if (rank == 2) return new Color(192, 192, 192);
        if (rank == 3) return new Color(205, 127, 50);
        return mainFrame.getCurrentTheme().getContentTextColor();
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Microsoft YaHei", Font.BOLD, 13));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(mainFrame.getCurrentTheme().getContentFont());
        return label;
    }

    private void showInitialMessage() {
        JLabel msg = new JLabel("请设置偏好条件，然后点击开始匹配按钮", SwingConstants.CENTER);
        msg.setFont(mainFrame.getCurrentTheme().getContentFont());
        resultPanel.add(msg);
    }
}