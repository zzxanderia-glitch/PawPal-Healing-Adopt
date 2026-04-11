package healing.pet.view.components;

import healing.pet.model.Animal;
import healing.pet.view.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class PetCardPanel extends JPanel {
    private Animal pet;
    private MainFrame mainFrame;
    private boolean isHovered = false;
    private int hoverOffset = 0;
    private Image petImage; // 💡 用于缓存加载好的图片

    public PetCardPanel(MainFrame mainFrame, Animal pet) {
        this.mainFrame = mainFrame;
        this.pet = pet;

        // 💡 组长核心动作：加载图片
        loadPetImage();

        setPreferredSize(new Dimension(220, 320));
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                hoverOffset = 5;
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                hoverOffset = 0;
                repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                new PetDetailDialog(mainFrame, pet).setVisible(true);
            }
        });
    }

    /**
     * 💡 组长新增：图片加载逻辑
     */
    private void loadPetImage() {
        try {
            String path = pet.getPhotoPath();
            
            if (path == null || path.isEmpty()) {
                System.err.println("宠物 " + pet.getName() + " 没有设置图片路径");
                return;
            }

            // 兼容性处理：如果路径以/开头，删掉它，方便从ClassPath读取
            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            // 1. 尝试从资源文件夹（Classpath）加载
            URL imgUrl = getClass().getClassLoader().getResource(path);

            if (imgUrl != null) {
                petImage = new ImageIcon(imgUrl).getImage();
            } else {
                // 2. 如果找不到，尝试从本地磁盘加载（兜底方案）
                petImage = new ImageIcon("src/main/resources/" + path).getImage();
            }
        } catch (Exception e) {
            System.err.println("图片加载失败: " + (pet.getPhotoPath() != null ? pet.getPhotoPath() : "路径为空"));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth() - 20;
        int h = getHeight() - 20;
        int x = 10;
        int y = 10 - hoverOffset;

        // 1. 阴影 (暗色模式下加深阴影或去掉，这里保持原样或稍微调整)
        boolean isDark = mainFrame.getCurrentTheme() instanceof healing.pet.ui.Theme.DarkTheme;
        g2.setColor(new Color(0, 0, 0, isDark ? 50 : 15)); // 暗色模式阴影稍微加深一点
        g2.fillRoundRect(x + 3, y + 5, w, h, 25, 25);

        // 2. 背景
        // 💡 修复：暗色模式下卡片颜色跟随侧边栏浅灰色，亮色模式保持白色
        Color bgColor;
        if (isDark) {
            bgColor = new Color(45, 45, 48); // 与 DarkTheme 的侧边栏颜色一致
            if (isHovered) {
                bgColor = new Color(60, 60, 65); // 悬浮稍微变亮
            }
        } else {
            bgColor = isHovered ? Color.WHITE : new Color(250, 250, 250);
        }
        
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, w, h, 25, 25);

        // 3. 绘制图片
        if (petImage != null) {
            Shape oldClip = g2.getClip();
            g2.setClip(new RoundRectangle2D.Float(x + 10, y + 10, w - 20, 180, 20, 20));
            g2.drawImage(petImage, x + 10, y + 10, w - 20, 180, this);
            g2.setClip(oldClip);
        } else {
            // 没图时的占位颜色
            g2.setColor(isDark ? new Color(60, 60, 65) : new Color(230, 230, 230));
            g2.fillRoundRect(x + 10, y + 10, w - 20, 180, 20, 20);
            g2.setColor(isDark ? Color.LIGHT_GRAY : Color.GRAY);
            g2.drawString("暂无图片", x + w/2 - 25, y + 100);
        }

        // 4. 名字和品种
        // 💡 修复：暗色模式下文字颜色变浅
        g2.setColor(isDark ? new Color(220, 220, 220) : new Color(60, 60, 60));
        g2.setFont(new Font("微软雅黑", Font.BOLD, 18));
        g2.drawString(pet.getName(), x + 20, y + 215);

        g2.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        g2.setColor(isDark ? new Color(180, 180, 180) : new Color(120, 120, 120));
        String breed = pet.getBreed() != null ? pet.getBreed() : "萌宠";
        g2.drawString(breed + " | " + pet.getAge() + "岁", x + 20, y + 240);

        // 5. 状态标签 (文字颜色适配)
        drawStatusBadge(g2, x + w - 75, y + 15);

        g2.dispose();
    }

    private void drawStatusBadge(Graphics2D g2, int x, int y) {
        String text;
        Color bgColor;
        switch (pet.getStatus()) {
            case 1: text = "审核中"; bgColor = new Color(255, 180, 50); break;
            case 2: text = "已领养"; bgColor = new Color(150, 150, 150); break;
            default: text = "领养我"; bgColor = new Color(255, 154, 162); break;
        }
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, 65, 25, 12, 12);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("微软雅黑", Font.BOLD, 12));
        g2.drawString(text, x + 14, y + 17);
    }
}