package healing.pet.view.components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    public RoundedPanel() {
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                putClientProperty("scale", 1.05f);
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                putClientProperty("scale", 1.0f);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 20, 20);

        super.paintComponent(g);
    }
}
