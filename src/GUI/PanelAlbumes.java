package GUI;

import control.Cancion;
import control.ControladorCanciones;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Consumer;
import javax.swing.*;

public class PanelAlbumes extends JPanel {
    private final ControladorCanciones ctrl;
    private final Consumer<Cancion> alReproducir;

    private final CardLayout cartas = new CardLayout();
    private final JPanel raiz = new JPanel(cartas);
    private final JPanel cuadricula = new JPanel(new GridLayout(0, 4, 18, 18));
    private final JPanel detalle = new JPanel();

    public PanelAlbumes(ControladorCanciones ctrl, Consumer<Cancion> alReproducir) {
        this.ctrl = ctrl;
        this.alReproducir = alReproducir;
        setLayout(new BorderLayout());
        setBackground(EstiloUI.FONDO);

        raiz.setOpaque(false);
        raiz.add(crearPantallaCuadricula(), "cuadricula");
        raiz.add(crearPantallaDetalle(), "detalle");
        add(raiz, BorderLayout.CENTER);

        actualizar();
    }

    private JPanel crearPantallaCuadricula() {
        JPanel pantalla = new JPanel(new BorderLayout());
        pantalla.setOpaque(false);
        pantalla.setBorder(BorderFactory.createEmptyBorder(24, 28, 10, 28));
        pantalla.add(EstiloUI.etiqueta("Álbumes", 26, Font.BOLD, EstiloUI.TEXTO), BorderLayout.NORTH);

        cuadricula.setOpaque(false);
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        contenedor.add(cuadricula, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        EstiloUI.estilizarScroll(scroll);
        pantalla.add(scroll, BorderLayout.CENTER);
        return pantalla;
    }

    private JPanel crearPantallaDetalle() {
        JPanel pantalla = new JPanel(new BorderLayout(0, 10));
        pantalla.setOpaque(false);
        pantalla.setBorder(BorderFactory.createEmptyBorder(22, 28, 10, 20));

        BotonMenu volver = new BotonMenu("‹  Álbumes", EstiloUI.TARJETA);
        volver.setActivo(true);
        volver.setPreferredSize(new Dimension(120, 36));
        volver.addActionListener(e -> cartas.show(raiz, "cuadricula"));
        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        arriba.setOpaque(false);
        arriba.add(volver);
        pantalla.add(arriba, BorderLayout.NORTH);

        detalle.setLayout(new BoxLayout(detalle, BoxLayout.Y_AXIS));
        detalle.setBackground(EstiloUI.FONDO);
        detalle.setBorder(BorderFactory.createEmptyBorder(8, 0, 20, 8));
        JScrollPane scroll = new JScrollPane(detalle);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(EstiloUI.FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        EstiloUI.estilizarScroll(scroll);
        pantalla.add(scroll, BorderLayout.CENTER);
        return pantalla;
    }

    public void actualizar() {
        cuadricula.removeAll();

        TreeMap<String, Cancion> albumes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Cancion c : ctrl.listarTodas()) {
            String clave = c.getAlbum() + "|" + c.getArtista();
            if (!albumes.containsKey(clave)) {
                albumes.put(clave, c);
            }
        }

        for (Cancion c : albumes.values()) {
            cuadricula.add(crearTarjeta(c));
        }
        if (albumes.isEmpty()) {
            cuadricula.add(EstiloUI.etiqueta("Aún no hay canciones registradas.", 14, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        }
        cuadricula.revalidate();
        cuadricula.repaint();
        cartas.show(raiz, "cuadricula");
    }

    private JPanel crearTarjeta(Cancion c) {
        PanelRedondeado tarjeta = new PanelRedondeado(24, EstiloUI.TARJETA);
        tarjeta.setLayout(new BorderLayout(0, 10));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(12, 12, 14, 12));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tarjeta.setToolTipText("Ver canciones de " + c.getAlbum());
        tarjeta.activarHover(EstiloUI.TARJETA_HOVER);

        JLabel caratula = new JLabel(EstiloUI.caratula(c, 150), SwingConstants.CENTER);
        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(EstiloUI.etiqueta(c.getAlbum(), 13, Font.BOLD, EstiloUI.TEXTO));
        textos.add(EstiloUI.etiqueta(c.getArtista(), 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE));

        tarjeta.add(caratula, BorderLayout.CENTER);
        tarjeta.add(textos, BorderLayout.SOUTH);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarAlbum(c);
            }
        });
        return tarjeta;
    }

    private void mostrarAlbum(Cancion portada) {
        detalle.removeAll();

        ArrayList<Cancion> canciones = new ArrayList<>();
        for (Cancion c : ctrl.listarTodas()) {
            if (c.getAlbum().equalsIgnoreCase(portada.getAlbum())
                    && c.getArtista().equalsIgnoreCase(portada.getArtista())) {
                canciones.add(c);
            }
        }

        PanelRedondeado cabecera = new PanelRedondeado(28, EstiloUI.TARJETA);
        cabecera.setLayout(new BorderLayout(22, 0));
        cabecera.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 22));
        cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
        cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        cabecera.add(new JLabel(EstiloUI.caratula(portada, 170)), BorderLayout.WEST);

        BotonMenu reproducirAlbum = new BotonMenu("Reproducir álbum", EstiloUI.TARJETA_HOVER);
        reproducirAlbum.setActivo(true);
        reproducirAlbum.setHorizontalAlignment(SwingConstants.CENTER);
        reproducirAlbum.setPreferredSize(new Dimension(170, 38));
        reproducirAlbum.addActionListener(e -> alReproducir.accept(canciones.get(0)));
        JPanel zonaBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        zonaBoton.setOpaque(false);
        zonaBoton.add(reproducirAlbum);

        JPanel info = new JPanel(new GridLayout(4, 1, 0, 4));
        info.setOpaque(false);
        info.add(EstiloUI.etiqueta(portada.getAlbum(), 26, Font.BOLD, EstiloUI.TEXTO));
        info.add(EstiloUI.etiqueta(portada.getArtista(), 15, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        info.add(EstiloUI.etiqueta(portada.getAnio() + "  ·  " + portada.getGenero() + "  ·  "
                + canciones.size() + (canciones.size() == 1 ? " canción" : " canciones"),
                13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        info.add(zonaBoton);
        cabecera.add(info, BorderLayout.CENTER);

        detalle.add(cabecera);
        detalle.add(Box.createVerticalStrut(14));
        for (Cancion c : canciones) {
            detalle.add(EstiloUI.filaCancion(c, alReproducir, () -> EstiloUI.anadirALista(this, ctrl, c)));
            detalle.add(Box.createVerticalStrut(8));
        }

        detalle.revalidate();
        detalle.repaint();
        cartas.show(raiz, "detalle");
    }
}
