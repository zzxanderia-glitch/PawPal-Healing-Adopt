package healing.pet.ui.content;

import healing.pet.dao.PetDAO;
import healing.pet.dao.PetDAOImpl;
import healing.pet.model.Animal;
import healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员专用的领养审批页面
 * 展示状态为 1 (审核中) 的宠物列表
 */
public class AdminAuditPanel extends JPanel {
    private MainFrame mainFrame;
    private PetDAO petDAO;
    private JPanel listPanel;
    private JScrollPane scrollPane;

    public AdminAuditPanel(MainFrame mainFrame) throws SQLException {
        this.mainFrame = mainFrame;
        this.petDAO = new PetDAOImpl();

        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titleLabel = new JLabel("📑 领养申请审批中心", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 列表容器
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        loadAuditPets();
    }

    private void loadAuditPets() throws SQLException {
        listPanel.removeAll();
        
        // 获取所有宠物并过滤出“审核中” (status == 1) 的
        List<Animal> allPets = petDAO.getAllPets();
        List<Animal> auditPets = allPets.stream()
                .filter(p -> p.getStatus() == 1)
                .collect(Collectors.toList());

        if (auditPets.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无待审批的申请", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(150, 150, 150));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
            listPanel.add(Box.createVerticalGlue());
        } else {
            for (int i = 0; i < auditPets.size(); i++) {
                Animal pet = auditPets.get(i);
                AdminPetRowPanel row = new AdminPetRowPanel(mainFrame, pet);
                listPanel.add(row);
                
                if (i < auditPets.size() - 1) {
                    listPanel.add(Box.createVerticalStrut(10));
                }
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}