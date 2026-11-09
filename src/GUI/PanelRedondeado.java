package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class PanelRedondeado extends JPanel {
    private final int radio;
    private Color fondo;

    public PanelRedondeado(int radio, Color fondo) {
        this.radio = radio;
        this.fondo = fondo;
        setOpaque(false);
    }

    public void setFondo(Color fondo) {
        this.fondo = fondo;
        repaint();
    }

    public void activarHover(Color hover) {
        final Color normal = fondo;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setFondo(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!contains(e.getPoint())) {
                    setFondo(normal);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        EstiloUI.calidad(g2);
        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio * 2, radio * 2);
        g2.dispose();
        super.paintComponent(g);
    }
}
