package healing.pet.ui.content;

import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.service.MatchService;
import healing.pet.service.UserPreferences;
import healing.pet.view.MainFrame;
import healing.pet.view.components.PetCardPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MatchPanel extends JPanel {

    private MainFrame mainFrame;
    private MatchService matchService;

    private JComboBox<String> petTypeCombo;
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

        JLabel titleLabel = new JLabel("🎯 智能匹配 - 找到你的专属萌宠", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("宠物类型："), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] petTypes = {"全部", "猫", "狗"};
        petTypeCombo = createComboBox(petTypes);
        formPanel.add(petTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("性格偏好："), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] personalities = {"请选择", "温顺", "活泼", "独立", "聪明", "粘人"};
        personalityCombo = createComboBox(personalities);
        formPanel.add(personalityCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("居住面积："), gbc);

        gbc.gridx = 1;
        String[] spaces = {"请选择", "公寓", "别墅", "小户型", "大空间"};
        livingSpaceCombo = createComboBox(spaces);
        formPanel.add(livingSpaceCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(createLabel("陪伴时间："), gbc);

        gbc.gridx = 1;
        String[] times = {"请选择", "多", "中", "少"};
        companionTimeCombo = createComboBox(times);
        formPanel.add(companionTimeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        matchButton = new JButton("🚀 开始匹配");
        matchButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        matchButton.setPreferredSize(new Dimension(200, 45));
        matchButton.setBackground(mainFrame.getCurrentTheme().getSidebarButtonActiveBackground());
        matchButton.setForeground(Color.WHITE);
        matchButton.setFocusPainted(false);
        matchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        matchButton.addActionListener(e -> {
            try {
                performMatch();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        formPanel.add(matchButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(0, 3, 15, 15));
        resultPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(mainFrame.getCurrentTheme().getBorderColor()),
                        "匹配结果（Top 3）",
                        SwingConstants.LEFT,
                        SwingConstants.TOP
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

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

    private void performMatch() throws SQLException {
        String petType = (String) petTypeCombo.getSelectedItem();
        String personality = (String) personalityCombo.getSelectedItem();
        String livingSpace = (String) livingSpaceCombo.getSelectedItem();
        String companionTime = (String) companionTimeCombo.getSelectedItem();

        if ("请选择".equals(personality) || "请选择".equals(livingSpace) || "请选择".equals(companionTime)) {
            JOptionPane.showMessageDialog(this, "请完成所有选项！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserPreferences preferences = new UserPreferences(personality, livingSpace, companionTime);

        List<Animal> matchedPets = matchService.match(preferences);

        if ("猫".equals(petType)) {
            matchedPets = matchedPets.stream()
                    .filter(p -> p instanceof Cat)
                    .collect(Collectors.toList());
        } else if ("狗".equals(petType)) {
            matchedPets = matchedPets.stream()
                    .filter(p -> p instanceof Dog)
                    .collect(Collectors.toList());
        }

        displayResults(matchedPets, preferences);
    }

    private void displayResults(List<Animal> pets, UserPreferences preferences) {
        resultPanel.removeAll();

        if (pets == null || pets.isEmpty()) {
            resultPanel.add(new JLabel("没有找到匹配的宠物", SwingConstants.CENTER));
        } else {
            List<Animal> top3Pets = pets.stream().limit(3).collect(Collectors.toList());

            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
            
            resultPanel.add(createSummaryLabel(top3Pets.size(), preferences));
            resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
            cardsPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
            cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            for (int i = 0; i < top3Pets.size(); i++) {
                Animal pet = top3Pets.get(i);
                PetCardPanel petCard = new PetCardPanel(pet);
                cardsPanel.add(petCard);
            }

            resultPanel.add(cardsPanel);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JLabel createSummaryLabel(int count, UserPreferences prefs) {
        JLabel label = new JLabel(
                "<html><div style='text-align:center; padding:10px;'>" +
                        "✅ 为您找到最匹配的 <b>" + count + "</b> 只宠物！</br>" +
                        "偏好：" + prefs.getPersonality() + "、" + prefs.getLivingSpace() + "、" + prefs.getCompanionTime() +
                        "</div></html>",
                SwingConstants.CENTER
        );
        label.setFont(mainFrame.getCurrentTheme().getContentFont());
        return label;
    }

    private void showInitialMessage() {
        JLabel msg = new JLabel("请设置偏好条件，然后点击开始匹配按钮", SwingConstants.CENTER);
        msg.setFont(mainFrame.getCurrentTheme().getContentFont());
        resultPanel.add(msg);
    }
}