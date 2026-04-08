package healing.pet.ui.content;

import healing.pet.dao.PetDAO;
import healing.pet.dao.PetDAOImpl;
import healing.pet.model.Animal;
import healing.pet.view.MainFrame;
import healing.pet.view.components.PetCardPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class HomePanel extends JPanel {

    private MainFrame mainFrame;
    private PetDAO petDAO;
    private JPanel petsPanel;
    private JScrollPane scrollPane;

    public HomePanel(MainFrame mainFrame) throws SQLException {
        this.mainFrame = mainFrame;
        this.petDAO = new PetDAOImpl();

        applyTheme();
        setLayout(new BorderLayout());

        // 顶部标题
        JLabel titleLabel = new JLabel("萌友速配 - 宠物列表", SwingConstants.CENTER);
        titleLabel.setFont(mainFrame.getCurrentTheme().getTitleFont());
        titleLabel.setBorder(mainFrame.getCurrentTheme().createTitleBorder());
        add(titleLabel, BorderLayout.NORTH);

        // 宠物展示面板
        petsPanel = new JPanel();
        petsPanel.setLayout(new GridLayout(0, 5, 15, 15));
        petsPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());

        // 💡 修复：正确初始化滚动条
        scrollPane = new JScrollPane(petsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);

        loadPets();
    }

    private void loadPets() throws SQLException {
        petsPanel.removeAll();
        List<Animal> pets = petDAO.getAllPets();

        if (pets == null || pets.isEmpty()) {
            JLabel noDataLabel = new JLabel("暂无宠物数据", SwingConstants.CENTER);
            noDataLabel.setFont(mainFrame.getCurrentTheme().getContentFont());
            petsPanel.add(noDataLabel);
        } else {
            for (Animal pet : pets) {
                PetCardPanel petCard = new PetCardPanel(mainFrame, pet);
                petsPanel.add(petCard);
            }
        }

        // 💡 修复：异步重置滚动位置并重绘
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
            petsPanel.revalidate();
            petsPanel.repaint();
        });
    }

    public void refreshPets() throws SQLException {
        loadPets();
    }
    
    public void resetScrollToTop() {
        SwingUtilities.invokeLater(() -> {
            if (scrollPane != null) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }

    // ... existing code ...
    public void applyTheme() {
        if (mainFrame != null) {
            setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
            if (petsPanel != null) {
                petsPanel.setBackground(mainFrame.getCurrentTheme().getContentBackgroundColor());
            }
        }
    }

    public void refresh() {
        try {
            loadPets();
        } catch (SQLException e) {
            System.err.println("刷新宠物列表失败：" + e.getMessage());
        }
    }
}
