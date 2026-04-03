package com.healing.pet.view.components;

import com.healing.pet.model.Animal;
import com.healing.pet.model.Cat;
import com.healing.pet.model.Dog;

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
 * 治愈系宠物卡片组件（最终美化版）
 * 圆角+柔影+渐变+悬浮动画+治愈配色
 */
public class PetCardPanel extends JPanel {
    // ========== 治愈系样式常量（可自由调整） ==========
    private static final int CORNER_RADIUS = 24;    // 圆角半径
    private static final int SHADOW_SIZE = 10;      // 阴影大小
    private static final Color CARD_BG = new Color(255, 252, 245); // 暖米色主背景
    private static final Color CARD_HOVER_BG = new Color(255, 247, 235); // 悬浮浅橙
    private static final Color LABEL_BG = new Color(255, 204, 213); // 软粉标签
    private static final Color LABEL_HOVER_BG = new Color(255, 182, 193); // 悬浮深粉
    private static final Font NAME_FONT = new Font("微软雅黑", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("微软雅黑", Font.PLAIN, 13);
    private static final float HOVER_SCALE = 1.05f; // 悬浮放大比例

    // 数据与状态
    private final Animal pet;
    private ImageIcon petIcon;
    private boolean isHovered = false;
    private float currentScale = 1.0f; // 动画缩放系数

    public PetCardPanel(Animal pet) {
        this.pet = pet;
        initCard();
        addMouseListener(new CardMouseListener());
    }

    // 初始化卡片基础设置
    private void initCard() {
        setOpaque(false);
        setPreferredSize(new Dimension(230, 290));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loadPetImage();
    }

    // 加载宠物图片（适配你的photoPath，支持本地/资源文件）
    private void loadPetImage() {
        try {
            String path = pet.getPhotoPath();
            // 优先从项目resources目录加载（推荐）
            File imgFile = new File("src/main/resources/images/" + path);
            if (imgFile.exists()) {
                BufferedImage img = ImageIO.read(imgFile);
                // 图片圆角处理
                BufferedImage roundedImg = makeRoundedImage(img, 16);
                Image scaled = roundedImg.getScaledInstance(190, 160, Image.SCALE_SMOOTH);
                petIcon = new ImageIcon(scaled);
            } else {
                // 加载失败显示默认治愈图
                petIcon = createDefaultImage();
            }
        } catch (IOException e) {
            petIcon = createDefaultImage();
        }
    }

    // 图片圆角处理（让宠物图更柔和）
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

    // 默认治愈图（加载失败兜底）
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
        // 开启抗锯齿+高质量渲染
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        // 1. 绘制柔化阴影（多层阴影更自然）
        drawSoftShadow(g2d, w, h);

        // 2. 绘制卡片主体（带缩放动画）
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

        // 卡片边框（悬浮加深）
        g2d.setColor(isHovered ? new Color(230, 230, 230) : new Color(245, 245, 245));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(cardRect);

        // 3. 绘制宠物图片
        if (petIcon != null) {
            int imgX = x + (cardW - petIcon.getIconWidth()) / 2;
            int imgY = y + SHADOW_SIZE + 18;
            g2d.drawImage(petIcon.getImage(), imgX, imgY, null);
        }

        // 4. 绘制宠物名字
        g2d.setColor(new Color(70, 70, 70));
        g2d.setFont(NAME_FONT);
        String name = pet.getName();
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (cardW - fm.stringWidth(name)) / 2;
        int textY = y + SHADOW_SIZE + 18 + petIcon.getIconHeight() + 32;
        g2d.drawString(name, textX, textY);

        // 5. 绘制治愈系标签（悬浮变色）
        String label = getHealingLabel(pet);
        g2d.setFont(LABEL_FONT);
        fm = g2d.getFontMetrics();
        int labelW = fm.stringWidth(label) + 24;
        int labelH = fm.getHeight() + 8;
        int labelX = x + (cardW - labelW) / 2;
        int labelY = textY + 22;

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

    // 多层柔化阴影（替代单一阴影，更自然）
    private void drawSoftShadow(Graphics2D g2d, int w, int h) {
        for (int i = 0; i < SHADOW_SIZE; i++) {
            float alpha = 0.05f * (SHADOW_SIZE - i);
            g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
            RoundRectangle2D shadowRect = new RoundRectangle2D.Double(
                    i, i, w - 2 * i, h - 2 * i,
                    CORNER_RADIUS + i, CORNER_RADIUS + i
            );
            g2d.fill(shadowRect);
        }
    }

    // 个性化治愈标签（根据宠物类型/性格动态生成）
    private String getHealingLabel(Animal pet) {
        if (pet instanceof Cat) {
            String guide = pet.getCareGuide();
            if (guide.contains("粘人")) return "软萌粘人精";
            if (guide.contains("独立")) return "高冷小傲娇";
            return "治愈小猫咪";
        } else if (pet instanceof Dog) {
            String guide = pet.getCareGuide();
            if (guide.contains("活泼")) return "暖心小天使";
            if (guide.contains("聪明")) return "机灵小伙伴";
            return "治愈小狗勾";
        }
        return "治愈小宝贝";
    }

    // 鼠标悬浮监听器（带平滑动画）
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

        // 平滑缩放动画（替代瞬间放大，更治愈）
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

    public Animal getPet() {
        return pet;
    }
}