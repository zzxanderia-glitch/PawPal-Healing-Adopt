package healing.pet.ui.content;

import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.view.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 管理员专用：宠物列表行组件（左图右信息）
 */
public class AdminPetRowPanel extends JPanel {
    private Animal pet;
    private MainFrame mainFrame;

    public AdminPetRowPanel(MainFrame mainFrame, Animal pet) {
        this.mainFrame = mainFrame;
        this.pet = pet;

        setLayout(new BorderLayout(15, 0));
        setOpaque(false);
        
        // 边框颜色跟随主题
        Color borderColor = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme 
                ? new Color(80, 80, 85) : new Color(220, 220, 220);
        
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // 固定高度，确保整齐
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        setMinimumSize(new Dimension(0, 140));
        setPreferredSize(new Dimension(0, 140));

        // --- 左侧：图片 ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(120, 120));

        ImageIcon icon = loadPetImageIcon(pet.getPhotoPath(), 120, 120);
        JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);
        // 图片边框
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200, 100), 1));
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        // --- 右侧：信息与操作 ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        // 第一行：名字 + 类型
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topRow.setOpaque(false);

        JLabel nameLabel = new JLabel(pet.getName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        nameLabel.setForeground(getTextColor());

        String typeStr = (pet instanceof Dog) ? "🐶 狗" : "🐱 猫";
        JLabel typeLabel = new JLabel(typeStr);
        typeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        typeLabel.setForeground(getSubTextColor());

        topRow.add(nameLabel);
        topRow.add(typeLabel);
        rightPanel.add(topRow);
        rightPanel.add(Box.createVerticalStrut(8));

        // 第二行：品种 | 年龄
        JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoRow.setOpaque(false);

        String breed = "未知";
        if (pet instanceof Dog) breed = ((Dog) pet).getBreed();
        else if (pet instanceof Cat) breed = ((Cat) pet).getBreed();

        JLabel breedLabel = new JLabel("品种：" + breed);
        breedLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        breedLabel.setForeground(getTextColor());

        JLabel ageLabel = new JLabel("  |  年龄：" + pet.getAge() + "岁");
        ageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        ageLabel.setForeground(getTextColor());

        infoRow.add(breedLabel);
        infoRow.add(ageLabel);
        rightPanel.add(infoRow);
        rightPanel.add(Box.createVerticalStrut(8));

        // 第三行：状态
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusRow.setOpaque(false);
        
        JLabel statusLabel = new JLabel("状态：" + getStatusText());
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        statusLabel.setForeground(getStatusColor());
        statusRow.add(statusLabel);
        rightPanel.add(statusRow);

        // 第四行（仅已领养显示）：领养人
        if (pet.getStatus() == 2) {
            rightPanel.add(Box.createVerticalStrut(5));
            JPanel adopterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            adopterRow.setOpaque(false);
            
            JLabel adopterLabel = new JLabel(" 领养人：用户_9527"); // 模拟数据，实际应从数据库获取
            adopterLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            adopterLabel.setForeground(getSubTextColor());
            adopterRow.add(adopterLabel);
            rightPanel.add(adopterRow);
        }

        // 底部：操作按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        if (pet.getStatus() == 0) { // 待领养
            JButton editBtn = createSmallButton("编辑", new Color(110, 170, 240));
            editBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "编辑功能开发中..."));
            buttonPanel.add(editBtn);

            JButton auditBtn = createSmallButton("提交审核", new Color(255, 180, 50));
            auditBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "已提交审核！"));
            buttonPanel.add(auditBtn);
            
        } else if (pet.getStatus() == 1) { // 审核中
            JButton passBtn = createSmallButton("通过", new Color(100, 200, 100));
            passBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "审核通过！"));
            buttonPanel.add(passBtn);

            JButton rejectBtn = createSmallButton("驳回", new Color(255, 100, 100));
            rejectBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "已驳回申请。"));
            buttonPanel.add(rejectBtn);
            
        } else if (pet.getStatus() == 2) { // 已领养
            JButton viewBtn = createSmallButton("查看详情", new Color(150, 150, 150));
            viewBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "查看领养档案..."));
            buttonPanel.add(viewBtn);
        }

        rightPanel.add(Box.createVerticalGlue()); // 将按钮推到底部
        rightPanel.add(buttonPanel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private JButton createSmallButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(70, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String getStatusText() {
        switch (pet.getStatus()) {
            case 0: return "🟢 待领养";
            case 1: return "🟡 审核中";
            case 2: return "🔵 已领养";
            default: return "⚪ 未知";
        }
    }

    private Color getStatusColor() {
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
        switch (pet.getStatus()) {
            case 0: return isDark ? new Color(100, 255, 100) : new Color(50, 150, 50);
            case 1: return isDark ? new Color(255, 200, 100) : new Color(200, 150, 0);
            case 2: return isDark ? new Color(150, 200, 255) : new Color(50, 100, 200);
            default: return getTextColor();
        }
    }

    private Color getTextColor() {
        return mainFrame.getCurrentTheme().getContentTextColor();
    }

    private Color getSubTextColor() {
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
        return isDark ? new Color(180, 180, 180) : new Color(120, 120, 120);
    }

    private ImageIcon loadPetImageIcon(String path, int width, int height) {
        try {
            if (path == null || path.trim().isEmpty()) return createPlaceholder(width, height);
            if (path.startsWith("/")) path = path.substring(1);
            if (path.startsWith("images/")) path = path.substring(7);

            BufferedImage img = null;
            InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + path);
            if (is != null) {
                img = ImageIO.read(is);
                is.close();
            }
            if (img == null) {
                File f = new File("src/main/resources/images/" + path);
                if (f.exists()) img = ImageIO.read(f);
            }

            if (img != null) {
                Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return createPlaceholder(width, height);
    }

    private ImageIcon createPlaceholder(int w, int h) {
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(isDark ? new Color(60, 60, 65) : new Color(240, 240, 240));
        g.fillRoundRect(0, 0, w, h, 10, 10);
        g.setColor(isDark ? new Color(150, 150, 150) : new Color(180, 180, 180));
        g.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        g.drawString("暂无图片", w/2 - 30, h/2);
        g.dispose();
        return new ImageIcon(img);
    }
}