package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class BarraProgreso extends JPanel {
    private final boolean ondas;
    private double fraccion = 0;
    private int semilla = 1;

    public BarraProgreso(boolean ondas, Consumer<Double> alHacerClic) {
        this.ondas = ondas;
        setOpaque(false);
        setPreferredSize(new Dimension(200, ondas ? 38 : 16));
        if (alHacerClic != null) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    double f = (double) e.getX() / Math.max(1, getWidth());
                    alHacerClic.accept(Math.max(0, Math.min(1, f)));
                }
            });
        }
    }

    public void setFraccion(double f) {
        this.fraccion = Math.max(0, Math.min(1, f));
        repaint();
    }

    public void setSemilla(int semilla) {
        this.semilla = semilla;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        EstiloUI.calidad(g);
        int w = getWidth(), h = getHeight();
        GradientPaint degradado = new GradientPaint(0, 0, EstiloUI.ACENTO, w, 0, EstiloUI.ACENTO2);
        Color apagado = new Color(255, 255, 255, 40);

        if (ondas) {
            int ancho = 4, paso = 7;
            int n = Math.max(1, (w + paso - ancho) / paso);
            int llenas = (int) Math.round(n * fraccion);
            Random r = new Random(semilla);
            for (int i = 0; i < n; i++) {
                double base = 0.55 + 0.45 * Math.sin(i * 0.27 + (semilla % 10));
                double v = Math.abs(base) * (0.5 + 0.5 * r.nextDouble());
                int alto = 6 + (int) ((h - 8) * Math.min(1, v));
                g.setPaint(i < llenas ? degradado : apagado);
                g.fillRoundRect(i * paso, (h - alto) / 2, ancho, alto, ancho, ancho);
            }
        } else {
            int grosor = 6, y = (h - grosor) / 2;
            g.setColor(apagado);
            g.fillRoundRect(0, y, w, grosor, grosor, grosor);
            int lleno = (int) (w * fraccion);
            g.setPaint(degradado);
            g.fillRoundRect(0, y, lleno, grosor, grosor, grosor);
            g.setColor(Color.WHITE);
            g.fillOval(Math.max(0, Math.min(w - 12, lleno - 6)), (h - 12) / 2, 12, 12);
        }
        g.dispose();
    }
}
