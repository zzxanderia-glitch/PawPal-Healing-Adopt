package com.healing.pet.ui.content;

import com.healing.pet.model.Animal;
import com.healing.pet.service.MatchService;
import com.healing.pet.service.UserPreferences;
import com.healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomePanel extends JPanel {

    private MainFrame mainFrame;
    private MatchService matchService;
    private JPanel petsPanel;

    public HomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.matchService = new MatchService();

        applyTheme();
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("🐾 萌友速配 - 宠物列表", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        titleLabel.setBorder(mainFrame.getCurrentTheme().createTitleBorder());
        add(titleLabel, BorderLayout.NORTH);

        petsPanel = new JPanel();
        petsPanel.setLayout(new BoxLayout(petsPanel, BoxLayout.Y_AXIS));
        petsPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());

        loadPets();

        JScrollPane scrollPane = new JScrollPane(petsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPets() {
        petsPanel.removeAll();

        UserPreferences defaultPrefs = new UserPreferences("温顺", "公寓", "多");
        List<Animal> pets = matchService.match(defaultPrefs);

        if (pets == null || pets.isEmpty()) {
            JLabel noDataLabel = new JLabel("暂无宠物数据", SwingConstants.CENTER);
            noDataLabel.setFont(mainFrame.getCurrentTheme().getContentFont());
            noDataLabel.setForeground(mainFrame.getCurrentTheme().getContentTextColor());
            petsPanel.add(noDataLabel);
        } else {
            for (int i = 0; i < pets.size(); i++) {
                Animal pet = pets.get(i);
                JPanel petCard = createPetCard(pet, i + 1);
                petsPanel.add(petCard);
                petsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        petsPanel.revalidate();
        petsPanel.repaint();
    }

    private JPanel createPetCard(Animal pet, int index) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 10));
        card.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mainFrame.getCurrentTheme().getBorderColor(), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel indexLabel = new JLabel(String.valueOf(index), SwingConstants.CENTER);
        indexLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        indexLabel.setForeground(mainFrame.getCurrentTheme().getSidebarButtonActiveBackground());
        indexLabel.setPreferredSize(new Dimension(50, 50));
        card.add(indexLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 2, 10, 5));
        infoPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());

        infoPanel.add(createLabel("名字："));
        infoPanel.add(createValueLabel(pet.getName()));

        infoPanel.add(createLabel("类型："));
        infoPanel.add(createValueLabel(pet.getClass().getSimpleName()));

        infoPanel.add(createLabel("照顾指南："));
        infoPanel.add(createValueLabel(pet.getCareGuide()));

        card.add(infoPanel, BorderLayout.CENTER);

        JButton voiceButton = new JButton("听听叫声");
        voiceButton.setFont(mainFrame.getCurrentTheme().getButtonFont());
        voiceButton.setCursor(mainFrame.getCurrentTheme().getButtonCursor());
        voiceButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(HomePanel.this, 
                pet.getVoice(), 
                pet.getName() + "的叫声", 
                JOptionPane.INFORMATION_MESSAGE)
        );
        card.add(voiceButton, BorderLayout.EAST);

        return card;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        label.setForeground(mainFrame.getCurrentTheme().getContentTextColor());
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(mainFrame.getCurrentTheme().getContentFont());
        label.setForeground(mainFrame.getCurrentTheme().getContentTextColor());
        return label;
    }

    public void refreshPets() {
        loadPets();
    }

    public void applyTheme() {
        if (mainFrame != null) {
            setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        } else {
            setBackground(Color.WHITE);
        }
    }
}
