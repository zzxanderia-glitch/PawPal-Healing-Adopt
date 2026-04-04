package healing.pet.view.components;

import healing.pet.model.Animal;
import healing.pet.model.Cat;
import healing.pet.model.Dog;
import healing.pet.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 治愈系宠物卡片：适配 Animal 模型，保留所有美化效果
 */
public class PetCardPanel extends JPanel {
    // 治愈系样式常量
    private static final int CORNER_RADIUS = 24;
    private static final int SHADOW_SIZE = 10;
    private static final Color CARD_BG = new Color(255, 252, 245);
    private static final Color CARD_HOVER_BG = new Color(255, 247, 235);
    private static final Color LABEL_BG = new Color(255, 204, 213);
    private static final Color LABEL_HOVER_BG = new Color(255, 182, 193);
    private static final Font NAME_FONT = new Font("微软雅黑", Font.BOLD, 18);
    private static final Font INFO_FONT = new Font("微软雅黑", Font.PLAIN, 14);
    private static final Font LABEL_FONT = new Font("微软雅黑", Font.PLAIN, 13);
    private static final float HOVER_SCALE = 1.05f;

    // 适配 Animal 模型
    private final Animal pet;
    private ImageIcon petIcon;
    private boolean isHovered = false;
    private float currentScale = 1.0f;

    public PetCardPanel(Animal pet) {
        this.pet = pet;
        initCard();
        addMouseListener(new CardMouseListener());
    }

    private void initCard() {
        setOpaque(false);
        setPreferredSize(new Dimension(230, 350));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loadPetImage();
    }

    // 加载图片（适配 Animal 的 photoPath 字段）
    private void loadPetImage() {
        try {
            String path = pet.getPhotoPath();
            System.out.println("加载图片路径：" + path);

            // 路径兼容处理
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            BufferedImage img = null;
            // 从资源目录加载
            java.io.InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                img = ImageIO.read(is);
                is.close();
            } else {
                // 兜底：从文件系统加载
                File imgFile = new File("src/main/resources" + path);
                if (imgFile.exists()) {
                    img = ImageIO.read(imgFile);
                }
            }

            // 图片处理（圆角+缩放）
            if (img != null) {
                BufferedImage roundedImg = makeRoundedImage(img, 16);
                Image scaled = roundedImg.getScaledInstance(190, 160, Image.SCALE_SMOOTH);
                petIcon = new ImageIcon(scaled);
            } else {
                petIcon = createDefaultImage();
            }
        } catch (IOException e) {
            System.out.println("图片加载异常：" + e.getMessage());
            petIcon = createDefaultImage();
        }
    }

    // 图片圆角处理
    private BufferedImage makeRoundedImage(BufferedImage image, int radius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rounded = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rounded.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, radius, radius));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return rounded;
    }

    // 默认占位图
    private ImageIcon createDefaultImage() {
        BufferedImage img = new BufferedImage(190, 160, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(240, 245, 250));
        g.fillRoundRect(0, 0, 190, 160, 16, 16);
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        g.drawString("🐾 " + pet.getName(), 40, 85);
        g.dispose();
        return new ImageIcon(img);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        // 绘制柔化阴影
        drawSoftShadow(g2d, w, h);

        // 绘制卡片主体（带缩放动画）
        int cardW = (int) (w * currentScale);
        int cardH = (int) (h * currentScale);
        int x = (w - cardW) / 2;
        int y = (h - cardH) / 2;

        // 卡片背景（悬浮变色）
        Color bgColor = isHovered ? CARD_HOVER_BG : CARD_BG;
        g2d.setColor(bgColor);
        RoundRectangle2D cardRect = new RoundRectangle2D.Double(
                x + SHADOW_SIZE, y + SHADOW_SIZE,
                cardW - 2 * SHADOW_SIZE,
                cardH - 2 * SHADOW_SIZE,
                CORNER_RADIUS, CORNER_RADIUS
        );
        g2d.fill(cardRect);

        // 卡片边框
        g2d.setColor(isHovered ? new Color(230, 230, 230) : new Color(245, 245, 245));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(cardRect);

        // 绘制宠物图片
        if (petIcon != null) {
            int imgX = x + (cardW - petIcon.getIconWidth()) / 2;
            int imgY = y + SHADOW_SIZE + 18;
            g2d.drawImage(petIcon.getImage(), imgX, imgY, null);
        }

        // 绘制宠物名字
        g2d.setColor(new Color(70, 70, 70));
        g2d.setFont(NAME_FONT);
        String name = pet.getName();
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (cardW - fm.stringWidth(name)) / 2;
        int textY = y + SHADOW_SIZE + 18 + petIcon.getIconHeight() + 20;
        g2d.drawString(name, textX, textY);

        // 绘制宠物信息（年龄+类型+性格）
        g2d.setFont(INFO_FONT);
        g2d.setColor(new Color(90, 90, 90));

        // 年龄
        String ageText = "年龄：" + pet.getAge() + "岁";
        int ageX = x + (cardW - fm.stringWidth(ageText)) / 2;
        int ageY = textY + 25;
        g2d.drawString(ageText, ageX, ageY);

        // 类型（猫咪/狗狗）
        String typeText = "类型：" + (pet instanceof Cat ? "猫咪" : "狗狗");
        int typeX = x + (cardW - fm.stringWidth(typeText)) / 2;
        int typeY = ageY + 25;
        g2d.drawString(typeText, typeX, typeY);

        // 性格
        String personality = pet instanceof Cat ? ((Cat) pet).getPersonality() : ((Dog) pet).getPersonality();
        String personalityText = "性格：" + personality;
        int personalityX = x + (cardW - fm.stringWidth(personalityText)) / 2;
        int personalityY = typeY + 25;
        g2d.drawString(personalityText, personalityX, personalityY);

        // 绘制治愈系标签
        String label = getHealingLabel(pet);
        g2d.setFont(LABEL_FONT);
        fm = g2d.getFontMetrics();
        int labelW = fm.stringWidth(label) + 24;
        int labelH = fm.getHeight() + 8;
        int labelX = x + (cardW - labelW) / 2;
        int labelY = personalityY + 25;

        // 标签背景（悬浮变色）
        Color labelBg = isHovered ? LABEL_HOVER_BG : LABEL_BG;
        g2d.setColor(labelBg);
        RoundRectangle2D labelRect = new RoundRectangle2D.Double(
                labelX, labelY - labelH + fm.getAscent(),
                labelW, labelH, 12, 12
        );
        g2d.fill(labelRect);

        // 标签文字
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, labelX + 12, labelY);

        g2d.dispose();
    }

    // 多层柔化阴影
    private void drawSoftShadow(Graphics2D g2d, int w, int h) {
        for (int i = 0; i < SHADOW_SIZE; i++) {
            float alpha = 0.05f * (SHADOW_SIZE - i);
            g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
            RoundRectangle2D shadowRect = new RoundRectangle2D.Double(
                    i, i, w - 2 * i, h - 2 * i,
                    CORNER_RADIUS + i, CORNER_RADIUS + i
            );
            g2d.fill(shadowRect);
        }
    }

    // 个性化治愈标签（适配 Cat/Dog）
    // 个性化治愈标签（适配 Cat/Dog，兼容 Java 8）
    private String getHealingLabel(Animal pet) {
        if (pet instanceof Cat) {
            String personality = ((Cat) pet).getPersonality();
            if ("粘人".equals(personality)) {
                return "软萌粘人精";
            } else if ("独立".equals(personality)) {
                return "高冷小傲娇";
            } else if ("活泼".equals(personality)) {
                return "元气小淘气";
            } else {
                return "治愈小猫咪";
            }
        } else if (pet instanceof Dog) {
            String personality = ((Dog) pet).getPersonality();
            if ("活泼".equals(personality)) {
                return "暖心小天使";
            } else if ("聪明".equals(personality)) {
                return "机灵小伙伴";
            } else if ("温顺".equals(personality)) {
                return "温柔守护者";
            } else {
                return "治愈小狗勾";
            }
        }
        return "治愈小宝贝";
    }

    // 鼠标监听器（悬浮动画+点击弹窗）
    private class CardMouseListener extends MouseAdapter {
        private Timer timer;

        @Override
        public void mouseEntered(MouseEvent e) {
            isHovered = true;
            startScaleAnimation(HOVER_SCALE);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            isHovered = false;
            startScaleAnimation(1.0f);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // 点击弹出详情弹窗（适配 Animal 模型）
            MainFrame parentFrame = (MainFrame) SwingUtilities.getWindowAncestor(PetCardPanel.this);
            new PetDetailDialog((JFrame) parentFrame, pet);
        }

        // 平滑缩放动画
        private void startScaleAnimation(float targetScale) {
            if (timer != null && timer.isRunning()) timer.stop();
            timer = new Timer(10, evt -> {
                float step = (targetScale - currentScale) * 0.2f;
                currentScale += step;
                if (Math.abs(targetScale - currentScale) < 0.001f) {
                    currentScale = targetScale;
                    timer.stop();
                }
                revalidate();
                repaint();
            });
            timer.start();
        }
    }

    // 获取当前卡片的宠物对象
    public Animal getPet() {
        return pet;
    }
}