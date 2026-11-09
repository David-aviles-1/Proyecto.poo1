package GUI;

import java.awt.Graphics;
import javax.swing.JTextField;

public class CampoTexto extends JTextField {
    private final String guia;

    public CampoTexto() {
        this("");
    }

    public CampoTexto(String guia) {
        this.guia = guia;
        EstiloCampo.preparar(this);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        setForeground(editable ? EstiloUI.TEXTO : EstiloUI.TEXTO_SUAVE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        EstiloCampo.pintarFondo(g, this, guia);
        super.paintComponent(g);
    }
}
