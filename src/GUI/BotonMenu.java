package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class BotonMenu extends JButton {
    private boolean activo = false;
    private boolean encima = false;
    private final Color relleno;
    private final Color textoNormal = Color.LIGHT_GRAY;
    private final Color textoActivo = Color.WHITE; // El color cuando le das clic

    public BotonMenu(String texto) {
        // Un gris oscuro para el fondo cuando el botón está activo
        this(texto, new Color(40, 40, 40));
    }

    public BotonMenu(String texto, Color relleno) {
        super(texto);
        this.relleno = relleno;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, 14)); // Quitamos EstiloUI.fuente
        setForeground(textoNormal);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                encima = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                encima = false;
                repaint();
            }
        });
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        setForeground(activo ? textoActivo : textoNormal);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Esto reemplaza a EstiloUI.calidad(g2) para suavizar los bordes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (activo) {
            g2.setColor(relleno);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
        } else if (encima) {
            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}