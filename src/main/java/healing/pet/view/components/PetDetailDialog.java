package healing.pet.view.components;

import healing.pet.model.Animal;
import healing.pet.view.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PetDetailDialog extends JDialog {
    private Animal pet;
    private MainFrame mainFrame;

    public PetDetailDialog(MainFrame parent, Animal pet) {
        super(parent, "宠物详情", true);
        this.mainFrame = parent;
        this.pet = pet;

        setSize(650, 500);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        // 💡 获取当前主题颜色
        Color themeBg = mainFrame.getCurrentTheme().getContentBackgroundColor();
        Color themeText = mainFrame.getCurrentTheme().getContentTextColor();
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;

        // 动态计算卡片颜色（亮色用白，暗色用深灰）
        Color cardBg = isDark ? new Color(45, 45, 48) : Color.WHITE;
        Color cardBorder = isDark ? new Color(80, 80, 85) : new Color(200, 180, 200);
        Color labelColor = isDark ? new Color(180, 160, 180) : new Color(100, 80, 100);

        // 主面板：带圆角和阴影
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cardBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(cardBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- 顶部：标题栏 ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel(pet.getName() + " 的详细档案");
        title.setFont(new Font("微软雅黑", Font.BOLD, 22));
        title.setForeground(themeText);
        header.add(title, BorderLayout.WEST);

        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 24));
        closeBtn.setForeground(isDark ? new Color(200, 200, 200) : new Color(150, 150, 150));
        closeBtn.setBorder(null);
        closeBtn.setContentAreaFilled(false);
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        // --- 中间：左右分栏 ---
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(280, 0));

        JLabel imageLabel = new JLabel(loadPetImageIcon(pet.getPhotoPath(), 260, 320));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        centerPanel.add(imagePanel, BorderLayout.WEST);

        // 右侧：信息区
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 0, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 10);
        gbc.weightx = 1.0;

        Font labelFont = new Font("微软雅黑", Font.BOLD, 14);
        Font valueFont = new Font("微软雅黑", Font.PLAIN, 14);

        int row = 0;

        addInfoRow(infoPanel, "年 龄：", String.valueOf(pet.getAge()) + " 岁", labelFont, valueFont, labelColor, themeText, gbc, row++);

        String breed = "未知";
        if (pet instanceof healing.pet.model.Dog) breed = ((healing.pet.model.Dog) pet).getBreed();
        else if (pet instanceof healing.pet.model.Cat) breed = ((healing.pet.model.Cat) pet).getBreed();
        addInfoRow(infoPanel, "品 种：", breed, labelFont, valueFont, labelColor, themeText, gbc, row++);

        addInfoRow(infoPanel, "简 介：", pet.getStory(), labelFont, valueFont, labelColor, themeText, gbc, row++);
        addInfoRow(infoPanel, "详细故事：", pet.getDetailStory(), labelFont, valueFont, labelColor, themeText, gbc, row++);
        addInfoRow(infoPanel, "习 性：", pet.getHabits(), labelFont, valueFont, labelColor, themeText, gbc, row++);
        addInfoRow(infoPanel, "偏 好：", pet.getPreference(), labelFont, valueFont, labelColor, themeText, gbc, row++);

        centerPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // --- 底部：按钮 ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        if (pet.getStatus() == 0) {
            JButton applyBtn = new JButton("申请领养");
            applyBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
            applyBtn.setForeground(Color.WHITE);
            applyBtn.setBackground(isDark ? new Color(100, 150, 200) : new Color(255, 130, 150));
            applyBtn.setFocusPainted(false);
            applyBtn.setBorderPainted(false);
            applyBtn.setOpaque(true);
            applyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            applyBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "申请已提交，请等待管理员审核！");
                dispose();
            });
            footer.add(applyBtn);
        }
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addInfoRow(JPanel panel, String label, String value, Font labelFont, Font valueFont, Color labelColor, Color textColor, GridBagConstraints gbc, int row) {
        if (value == null) value = "暂无数据";

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(labelFont);
        lbl.setForeground(labelColor);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel("<html><div style='width: 250px;'>" + value + "</div></html>");
        val.setFont(valueFont);
        val.setForeground(textColor);
        panel.add(val, gbc);
    }

    private ImageIcon loadPetImageIcon(String path, int width, int height) {
        try {
            if (path == null) return createPlaceholder(width, height);
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
            e.printStackTrace();
        }
        return createPlaceholder(width, height);
    }

    private ImageIcon createPlaceholder(int w, int h) {
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(isDark ? new Color(50, 50, 55) : new Color(240, 230, 240));
        g.fillRoundRect(0, 0, w, h, 15, 15);
        g.setColor(isDark ? new Color(150, 150, 150) : new Color(180, 160, 180));
        g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        g.drawString("暂无图片", w/2 - 30, h/2);
        g.dispose();
        return new ImageIcon(img);
    }
}