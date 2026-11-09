package GUI;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.text.JTextComponent;

class EstiloCampo {
    static void preparar(JTextComponent c) {
        c.setOpaque(false);
        c.setForeground(EstiloUI.TEXTO);
        c.setCaretColor(Color.WHITE);
        c.setFont(EstiloUI.fuente(Font.PLAIN, 14));
        c.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        c.setPreferredSize(new Dimension(200, 42));
        c.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                c.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                c.repaint();
            }
        });
    }

    static void pintarFondo(Graphics g, JTextComponent c, String guia) {
        Graphics2D g2 = (Graphics2D) g.create();
        EstiloUI.calidad(g2);
        g2.setColor(c.isEditable() ? EstiloUI.TARJETA_HOVER : EstiloUI.TARJETA);
        g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 24, 24);
        if (c.isFocusOwner() && c.isEditable()) {
            g2.setColor(new Color(EstiloUI.ACENTO.getRed(), EstiloUI.ACENTO.getGreen(), EstiloUI.ACENTO.getBlue(), 160));
            g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 24, 24);
        }
        if (guia != null && !guia.isEmpty() && c.getDocument().getLength() == 0 && !c.isFocusOwner()) {
            g2.setColor(new Color(255, 255, 255, 90));
            g2.setFont(c.getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(guia, c.getInsets().left, (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent());
        }
        g2.dispose();
    }
}
