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

public class HomePanel extends JPanel {

    private MainFrame mainFrame;
    private MatchService matchService;
    private JPanel petsPanel;

    public HomePanel(MainFrame mainFrame) throws SQLException {
        this.mainFrame = mainFrame;
        this.matchService = new MatchService();

        applyTheme();
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("🐾 萌友速配 - 宠物列表", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        titleLabel.setBorder(mainFrame.getCurrentTheme().createTitleBorder());
        add(titleLabel, BorderLayout.NORTH);

        petsPanel = new JPanel();
        petsPanel.setLayout(new GridLayout(0, 5, 15, 15));
        petsPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());

        loadPets();

        JScrollPane scrollPane = new JScrollPane(petsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPets() throws SQLException {
        petsPanel.removeAll();

        UserPreferences defaultPrefs = new UserPreferences("温顺", "公寓", "多");
        List<Animal> pets = matchService.match(defaultPrefs);
        
        System.out.println("HomePanel: 接收到 " + pets.size() + " 只宠物");

        if (pets == null || pets.isEmpty()) {
            System.out.println("HomePanel: 宠物列表为空！");
            JLabel noDataLabel = new JLabel("暂无宠物数据", SwingConstants.CENTER);
            noDataLabel.setFont(mainFrame.getCurrentTheme().getContentFont());
            noDataLabel.setForeground(mainFrame.getCurrentTheme().getContentTextColor());
            petsPanel.add(noDataLabel);
        } else {
            System.out.println("HomePanel: 开始创建 " + pets.size() + " 个宠物卡片");
            int catCount = 0;
            int dogCount = 0;
            for (Animal pet : pets) {
                PetCardPanel petCard = new PetCardPanel(pet);
                petsPanel.add(petCard);
                if (pet instanceof Cat) catCount++;
                else if (pet instanceof Dog) dogCount++;
            }
            System.out.println("HomePanel: 猫=" + catCount + ", 狗=" + dogCount);
        }

        petsPanel.revalidate();
        petsPanel.repaint();
    }

    public void refreshPets() throws SQLException {
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
