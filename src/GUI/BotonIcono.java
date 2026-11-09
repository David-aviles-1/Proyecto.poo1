package GUI;

import java.awt.*;
import javax.swing.JButton;

public class BotonIcono extends JButton {
    public static final int ANTERIOR = 0;
    public static final int PLAY = 1;
    public static final int PAUSA = 2;
    public static final int SIGUIENTE = 3;

    private int tipo;
    private final boolean circulo;

    public BotonIcono(int tipo, boolean circulo, int tam) {
        this.tipo = tipo;
        this.circulo = circulo;
        setPreferredSize(new Dimension(tam, tam));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        EstiloUI.calidad(g);
        int w = getWidth(), h = getHeight();
        if (circulo) {
            g.setPaint(new GradientPaint(0, 0, EstiloUI.ACENTO, w, h, EstiloUI.ACENTO2));
            g.fillOval(0, 0, w, h);
        }
        g.setColor(circulo ? EstiloUI.TEXTO_SOBRE_ACENTO : Color.WHITE);
        int cx = w / 2, cy = h / 2, s = Math.max(5, w / 5);

        switch (tipo) {
            case PLAY:
                g.fillPolygon(new int[]{cx - s / 2, cx - s / 2, cx + s},
                        new int[]{cy - s, cy + s, cy}, 3);
                break;
            case PAUSA:
                int bw = Math.max(3, s * 2 / 3);
                int gap = Math.max(3, s / 2);
                g.fillRoundRect(cx - gap / 2 - bw, cy - s, bw, 2 * s, 3, 3);
                g.fillRoundRect(cx + gap / 2, cy - s, bw, 2 * s, 3, 3);
                break;
            case ANTERIOR:
                g.fillRect(cx - s, cy - s, 2, 2 * s);
                g.fillPolygon(new int[]{cx + s, cx + s, cx - s + 3},
                        new int[]{cy - s, cy + s, cy}, 3);
                break;
            case SIGUIENTE:
                g.fillRect(cx + s - 2, cy - s, 2, 2 * s);
                g.fillPolygon(new int[]{cx - s, cx - s, cx + s - 3},
                        new int[]{cy - s, cy + s, cy}, 3);
                break;
            default:
                break;
        }
        g.dispose();
    }
}
