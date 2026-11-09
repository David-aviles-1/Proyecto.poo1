package GUI;

import control.Cancion;
import control.ControladorCanciones;
import java.awt.*;
import javax.swing.*;

public class PanelAgregar extends JPanel {
    private final ControladorCanciones ctrl;
    private final Runnable alCambiar;
    private final FormularioCancion formulario = new FormularioCancion(true);

    public PanelAgregar(ControladorCanciones ctrl, Runnable alCambiar) {
        this.ctrl = ctrl;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(0, 14));
        setBackground(EstiloUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 18, 24));

        JPanel titulo = new JPanel(new GridLayout(2, 1));
        titulo.setOpaque(false);
        titulo.add(EstiloUI.etiqueta("Agregar canción", 26, Font.BOLD, EstiloUI.TEXTO));
        titulo.add(EstiloUI.etiqueta("Todos los campos son obligatorios.", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        add(titulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(formulario);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        EstiloUI.estilizarScroll(scroll);
        add(scroll, BorderLayout.CENTER);

        BotonPrimario guardar = new BotonPrimario("Guardar canción");
        guardar.setPreferredSize(new Dimension(170, 42));
        guardar.addActionListener(e -> guardar());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(guardar);
        add(pie, BorderLayout.SOUTH);
    }

    private void guardar() {
        Cancion c = formulario.leerCancion();
        String error = ctrl.agregarCancion(c, formulario.getImagenElegida(), formulario.getPdfElegido());
        if (error != null) {
            DialogoEstilo.mensaje(this, "No se pudo guardar", error);
            return;
        }
        DialogoEstilo.mensaje(this, "Canción guardada", "\"" + c.getTitulo() + "\" se registró correctamente. "
                + "Ya hay " + ctrl.total() + " canciones.");
        formulario.limpiar();
        alCambiar.run();
    }
}
