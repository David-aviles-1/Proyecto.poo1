package GUI;

import java.awt.*;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class BarraBusquedaClave extends JPanel {
    private final CampoTexto campo = new CampoTexto("Escribe la clave de la canción (ej. C001)");

    public BarraBusquedaClave(Consumer<String> alBuscar) {
        setLayout(new BorderLayout(12, 0));
        setOpaque(false);
        BotonPrimario buscar = new BotonPrimario("Buscar");
        add(campo, BorderLayout.CENTER);
        add(buscar, BorderLayout.EAST);
        buscar.addActionListener(e -> alBuscar.accept(campo.getText().trim()));
        campo.addActionListener(e -> alBuscar.accept(campo.getText().trim()));
    }

    public void setClave(String clave) {
        campo.setText(clave);
    }

    public String getClave() {
        return campo.getText().trim();
    }
}
