package GUI;

import control.Cancion;
import control.ControladorCanciones;
import java.awt.*;
import javax.swing.*;

public class PanelModificar extends JPanel {
    private final ControladorCanciones ctrl;
    private final Runnable alCambiar;
    private final BarraBusquedaClave barra;
    private final FormularioCancion formulario = new FormularioCancion(false);
    private final CardLayout cartas = new CardLayout();
    private final JPanel zona = new JPanel(cartas);
    private String claveActual = null;

    public PanelModificar(ControladorCanciones ctrl, Runnable alCambiar) {
        this.ctrl = ctrl;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(0, 16));
        setBackground(EstiloUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 18, 24));

        JPanel arriba = new JPanel(new BorderLayout(0, 14));
        arriba.setOpaque(false);
        JPanel titulo = new JPanel(new GridLayout(2, 1));
        titulo.setOpaque(false);
        titulo.add(EstiloUI.etiqueta("Modificar canción", 26, Font.BOLD, EstiloUI.TEXTO));
        titulo.add(EstiloUI.etiqueta("Busca por clave y cambia lo que necesites. La clave no se puede cambiar.", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        barra = new BarraBusquedaClave(this::buscar);
        arriba.add(titulo, BorderLayout.NORTH);
        arriba.add(barra, BorderLayout.CENTER);
        add(arriba, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(formulario);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        EstiloUI.estilizarScroll(scroll);

        JPanel vacio = new JPanel(new GridBagLayout());
        vacio.setOpaque(false);
        vacio.add(EstiloUI.etiqueta("Busca una canción para editar sus datos.", 14, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        zona.setOpaque(false);
        zona.add(vacio, "vacio");
        zona.add(scroll, "formulario");
        add(zona, BorderLayout.CENTER);

        BotonPrimario guardar = new BotonPrimario("Guardar cambios");
        guardar.setPreferredSize(new Dimension(170, 42));
        guardar.addActionListener(e -> guardar());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(guardar);
        add(pie, BorderLayout.SOUTH);
    }

    public void buscar(String clave) {
        barra.setClave(clave);
        if (clave.isEmpty()) {
            DialogoEstilo.mensaje(this, "Falta la clave", "Escribe la clave de la canción que quieres modificar.");
            return;
        }
        Cancion c = ctrl.consultarCancion(clave);
        if (c == null) {
            claveActual = null;
            cartas.show(zona, "vacio");
            DialogoEstilo.mensaje(this, "No encontrada", "No existe una canción con la clave " + clave + ".");
            return;
        }
        claveActual = c.getClave();
        formulario.cargar(c);
        cartas.show(zona, "formulario");
    }

    private void guardar() {
        if (claveActual == null) {
            DialogoEstilo.mensaje(this, "Nada que guardar", "Primero busca la canción que quieres modificar.");
            return;
        }
        Cancion nuevos = formulario.leerCancion();
        String error = ctrl.modificarCancion(claveActual, nuevos, formulario.getImagenElegida(), formulario.getPdfElegido());
        if (error != null) {
            DialogoEstilo.mensaje(this, "No se pudo modificar", error);
            return;
        }
        DialogoEstilo.mensaje(this, "Cambios guardados", "La canción \"" + nuevos.getTitulo() + "\" se actualizó correctamente.");
        alCambiar.run();
        buscar(claveActual);
    }

    public void limpiar() {
        claveActual = null;
        barra.setClave("");
        formulario.limpiar();
        cartas.show(zona, "vacio");
    }
}
