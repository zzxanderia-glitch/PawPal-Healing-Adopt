package healing.pet.ui.content;

import healing.pet.dao.PetDAO;
import healing.pet.dao.PetDAOImpl;
import healing.pet.model.Animal;
import healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminPetPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel petsListPanel;
    private JScrollPane scrollPane;
    private PetDAO petDAO;

    public AdminPetPanel(MainFrame mainFrame) throws SQLException {
        this.mainFrame = mainFrame;
        this.petDAO = new PetDAOImpl();

        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titleLabel = new JLabel("宠物档案管理库", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        petsListPanel = new JPanel();
        petsListPanel.setLayout(new BoxLayout(petsListPanel, BoxLayout.Y_AXIS));
        petsListPanel.setOpaque(false);
        petsListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        scrollPane = new JScrollPane(petsListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolBar.setOpaque(false);

        JButton refreshBtn = new JButton("刷新列表");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(110, 170, 240));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            try {
                loadPets();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        toolBar.add(refreshBtn);
        add(toolBar, BorderLayout.SOUTH);

        loadPets();
    }

    private void loadPets() throws SQLException {
        petsListPanel.removeAll();
        List<Animal> pets = petDAO.getAllPets();

        if (pets == null || pets.isEmpty()) {
            JLabel noDataLabel = new JLabel("暂无宠物数据", SwingConstants.CENTER);
            noDataLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            noDataLabel.setForeground(Color.GRAY);
            petsListPanel.add(noDataLabel);
        } else {
            for (int i = 0; i < pets.size(); i++) {
                Animal pet = pets.get(i);
                AdminPetRowPanel rowPanel = new AdminPetRowPanel(mainFrame, pet,this);
                petsListPanel.add(rowPanel);

                if (i < pets.size() - 1) {
                    petsListPanel.add(Box.createVerticalStrut(10));
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
            petsListPanel.revalidate();
            petsListPanel.repaint();
        });
    }
    public void refresh() {
        try {
            loadPets();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "刷新失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}