package GUI;

import control.Cancion;
import control.ControladorCanciones;
import control.ListaReproduccion;
import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

public class PanelListas extends JPanel {
    private final ControladorCanciones ctrl;
    private final Consumer<Cancion> alReproducir;
    private final DefaultListModel<String> modelo = new DefaultListModel<>();
    private final JList<String> listaNombres = new JList<>(modelo);
    private final JPanel detalle = new JPanel();

    public PanelListas(ControladorCanciones ctrl, Consumer<Cancion> alReproducir) {
        this.ctrl = ctrl;
        this.alReproducir = alReproducir;
        setLayout(new BorderLayout());
        setBackground(EstiloUI.FONDO);

        listaNombres.setBackground(EstiloUI.FONDO);
        listaNombres.setCellRenderer(new RenderizadorLista());
        listaNombres.setFixedCellHeight(44);
        listaNombres.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 6));
        listaNombres.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarLista(listaNombres.getSelectedValue());
            }
        });

        BotonMenu nueva = new BotonMenu("+  Nueva lista", EstiloUI.TARJETA);
        nueva.setActivo(true);
        nueva.setHorizontalAlignment(SwingConstants.CENTER);
        nueva.setPreferredSize(new Dimension(100, 40));
        nueva.addActionListener(e -> crearListaNueva());
        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(8, 14, 16, 10));
        pie.add(nueva, BorderLayout.CENTER);

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBackground(EstiloUI.FONDO);
        izquierda.setPreferredSize(new Dimension(220, 100));
        JScrollPane scrollLista = new JScrollPane(listaNombres);
        scrollLista.setBorder(null);
        scrollLista.getViewport().setBackground(EstiloUI.FONDO);
        EstiloUI.estilizarScroll(scrollLista);
        izquierda.add(scrollLista, BorderLayout.CENTER);
        izquierda.add(pie, BorderLayout.SOUTH);
        add(izquierda, BorderLayout.WEST);

        detalle.setLayout(new BoxLayout(detalle, BoxLayout.Y_AXIS));
        detalle.setBackground(EstiloUI.FONDO);
        detalle.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 28));
        JScrollPane scrollDetalle = new JScrollPane(detalle);
        scrollDetalle.setBorder(null);
        scrollDetalle.getVerticalScrollBar().setUnitIncrement(16);
        EstiloUI.estilizarScroll(scrollDetalle);
        add(scrollDetalle, BorderLayout.CENTER);

        actualizar();
    }

    public void actualizar() {
        String seleccionada = listaNombres.getSelectedValue();
        modelo.clear();
        for (ListaReproduccion l : ctrl.getListas()) {
            modelo.addElement(l.getNombre());
        }
        if (seleccionada != null && modelo.contains(seleccionada)) {
            listaNombres.setSelectedValue(seleccionada, true);
        } else if (!modelo.isEmpty()) {
            listaNombres.setSelectedIndex(0);
        } else {
            mostrarLista(null);
        }
    }

    private void crearListaNueva() {
        String nombre = DialogoEstilo.pedirTexto(this, "Nueva lista", "Escribe el nombre de tu nueva lista de reproducción:");
        if (nombre == null) {
            return;
        }
        if (ctrl.crearLista(nombre)) {
            actualizar();
            listaNombres.setSelectedValue(nombre.trim(), true);
        } else {
            DialogoEstilo.mensaje(this, "No se pudo crear", "El nombre está vacío o ya existe una lista con ese nombre.");
        }
    }

    private void mostrarLista(String nombre) {
        detalle.removeAll();
        ListaReproduccion lista = nombre == null ? null : ctrl.buscarLista(nombre);
        if (lista == null) {
            detalle.add(EstiloUI.etiqueta("Crea tu primera lista con \"Nueva lista\".", 14, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        } else {
            JLabel titulo = EstiloUI.etiqueta(lista.getNombre(), 28, Font.BOLD, EstiloUI.TEXTO);
            titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel total = EstiloUI.etiqueta(lista.getCanciones().size() + " canciones", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE);
            total.setAlignmentX(Component.LEFT_ALIGNMENT);
            detalle.add(titulo);
            detalle.add(total);
            detalle.add(Box.createVerticalStrut(18));

            if (lista.getCanciones().isEmpty()) {
                JLabel vacia = EstiloUI.etiqueta("Esta lista está vacía. Añade canciones con el botón + desde Artistas.", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE);
                vacia.setAlignmentX(Component.LEFT_ALIGNMENT);
                detalle.add(vacia);
            }
            for (Cancion c : lista.getCanciones()) {
                detalle.add(EstiloUI.filaCancion(c, alReproducir, null));
                detalle.add(Box.createVerticalStrut(8));
            }
        }
        detalle.revalidate();
        detalle.repaint();
    }
}
