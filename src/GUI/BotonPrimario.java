package GUI;

import java.awt.*;
import javax.swing.JButton;

public class BotonPrimario extends JButton {
    public BotonPrimario(String texto) {
        super(texto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(EstiloUI.TEXTO_SOBRE_ACENTO);
        setFont(EstiloUI.fuente(Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(130, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        EstiloUI.calidad(g2);
        g2.setPaint(new GradientPaint(0, 0, EstiloUI.ACENTO, getWidth(), 0, EstiloUI.ACENTO2));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 26, 26);
        g2.dispose();
        super.paintComponent(g);
    }
}
