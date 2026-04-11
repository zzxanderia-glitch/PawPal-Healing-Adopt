package healing.pet.ui.content;

import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.service.MatchService;
import healing.pet.service.UserPreferences;
import healing.pet.ui.utils.HealingPetMatching;
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
    private HealingPetMatching healingPetMatching;

    public MatchPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.matchService = new MatchService();

        setLayout(new BorderLayout(15, 15));
        setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("智能匹配 - 找到你的专属萌宠", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        add(titleLabel, BorderLayout.NORTH);

        healingPetMatching = new HealingPetMatching();

        add(healingPetMatching, BorderLayout.CENTER);
    }
}
