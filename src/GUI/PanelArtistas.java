package GUI;

import control.Cancion;
import control.ControladorCanciones;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Consumer;
import javax.swing.*;

public class PanelArtistas extends JPanel {
    private final ControladorCanciones ctrl;
    private final Consumer<Cancion> alReproducir;
    private final DefaultListModel<String> modelo = new DefaultListModel<>();
    private final JList<String> listaArtistas = new JList<>(modelo);
    private final JPanel detalle = new JPanel();

    public PanelArtistas(ControladorCanciones ctrl, Consumer<Cancion> alReproducir) {
        this.ctrl = ctrl;
        this.alReproducir = alReproducir;
        setLayout(new BorderLayout());
        setBackground(EstiloUI.FONDO);

        listaArtistas.setBackground(EstiloUI.FONDO);
        listaArtistas.setCellRenderer(new RenderizadorLista());
        listaArtistas.setFixedCellHeight(44);
        listaArtistas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 6));
        listaArtistas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarArtista(listaArtistas.getSelectedValue());
            }
        });
        JScrollPane scrollLista = new JScrollPane(listaArtistas);
        scrollLista.setBorder(null);
        scrollLista.getViewport().setBackground(EstiloUI.FONDO);
        scrollLista.setPreferredSize(new Dimension(220, 100));
        EstiloUI.estilizarScroll(scrollLista);
        add(scrollLista, BorderLayout.WEST);

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
        String seleccionado = listaArtistas.getSelectedValue();
        TreeMap<String, Boolean> artistas = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Cancion c : ctrl.listarTodas()) {
            artistas.put(c.getArtista(), true);
        }
        modelo.clear();
        for (String a : artistas.keySet()) {
            modelo.addElement(a);
        }
        if (seleccionado != null && modelo.contains(seleccionado)) {
            listaArtistas.setSelectedValue(seleccionado, true);
        } else if (!modelo.isEmpty()) {
            listaArtistas.setSelectedIndex(0);
        } else {
            mostrarArtista(null);
        }
    }

    private void mostrarArtista(String artista) {
        detalle.removeAll();
        if (artista == null) {
            detalle.add(EstiloUI.etiqueta("Aún no hay artistas registrados.", 14, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        } else {
            JLabel nombre = EstiloUI.etiqueta(artista, 28, Font.BOLD, EstiloUI.TEXTO);
            nombre.setAlignmentX(Component.LEFT_ALIGNMENT);
            detalle.add(nombre);
            detalle.add(Box.createVerticalStrut(18));

            TreeMap<String, ArrayList<Cancion>> porAlbum = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            for (Cancion c : ctrl.listarTodas()) {
                if (c.getArtista().equalsIgnoreCase(artista)) {
                    porAlbum.computeIfAbsent(c.getAlbum(), k -> new ArrayList<>()).add(c);
                }
            }

            for (String album : porAlbum.keySet()) {
                ArrayList<Cancion> canciones = porAlbum.get(album);
                Cancion primera = canciones.get(0);

                PanelRedondeado cabecera = new PanelRedondeado(28, EstiloUI.TARJETA);
                cabecera.setLayout(new BorderLayout(18, 0));
                cabecera.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 18));
                cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
                cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
                cabecera.add(new JLabel(EstiloUI.caratula(primera, 120)), BorderLayout.WEST);
                JPanel info = new JPanel(new GridLayout(3, 1));
                info.setOpaque(false);
                info.add(EstiloUI.etiqueta(album, 20, Font.BOLD, EstiloUI.TEXTO));
                info.add(EstiloUI.etiqueta(primera.getAnio() + "  ·  " + primera.getGenero(), 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
                info.add(EstiloUI.etiqueta(canciones.size() + " canciones", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
                cabecera.add(info, BorderLayout.CENTER);
                detalle.add(cabecera);
                detalle.add(Box.createVerticalStrut(12));

                for (Cancion c : canciones) {
                    detalle.add(EstiloUI.filaCancion(c, alReproducir, () -> EstiloUI.anadirALista(this, ctrl, c)));
                    detalle.add(Box.createVerticalStrut(8));
                }
                detalle.add(Box.createVerticalStrut(22));
            }
        }
        detalle.revalidate();
        detalle.repaint();
    }
}
