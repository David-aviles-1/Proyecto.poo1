package GUI;

import java.awt.*;
import javax.swing.*;

public class RenderizadorLista extends JLabel implements ListCellRenderer<String> {
    private boolean seleccionado = false;
    private final Color colorSeleccion;

    public RenderizadorLista() {
        this(EstiloUI.TARJETA);
    }

    public RenderizadorLista(Color colorSeleccion) {
        this.colorSeleccion = colorSeleccion;
        setOpaque(false);
        setFont(EstiloUI.fuente(Font.BOLD, 14));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> lista, String valor,
            int indice, boolean seleccionado, boolean conFoco) {
        setText(valor);
        this.seleccionado = seleccionado;
        setForeground(seleccionado ? EstiloUI.ACENTO : EstiloUI.TEXTO);
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (seleccionado) {
            Graphics2D g2 = (Graphics2D) g.create();
            EstiloUI.calidad(g2);
            g2.setColor(colorSeleccion);
            g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 6, 26, 26);
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
