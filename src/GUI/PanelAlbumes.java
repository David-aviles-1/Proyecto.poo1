package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class PanelAlbumes extends JPanel {

    private final CardLayout cartas = new CardLayout();
    private final JPanel raiz = new JPanel(cartas);
    private final JPanel cuadricula = new JPanel(new GridLayout(0, 4, 18, 18));
    private final JPanel detalle = new JPanel();

    private final Color FONDO = new Color(18, 18, 20);
    private final Color TARJETA = new Color(30, 30, 33);
    private final Color TEXTO = Color.WHITE;
    private final Color TEXTO_SUAVE = new Color(160, 160, 167);

    public PanelAlbumes() {
        setLayout(new BorderLayout());
        setBackground(FONDO);

        raiz.setOpaque(false);
        raiz.add(crearPantallaCuadricula(), "cuadricula");
        raiz.add(crearPantallaDetalle(), "detalle");
        add(raiz, BorderLayout.CENTER);

        cargarDatosVisuales();
    }

    private JPanel crearPantallaCuadricula() {
        JPanel pantalla = new JPanel(new BorderLayout());
        pantalla.setOpaque(false);
        pantalla.setBorder(BorderFactory.createEmptyBorder(24, 28, 10, 28));
        
        JLabel titulo = new JLabel("Álbumes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(TEXTO);
        pantalla.add(titulo, BorderLayout.NORTH);

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
        pantalla.add(scroll, BorderLayout.CENTER);
        
        return pantalla;
    }

    private JPanel crearPantallaDetalle() {
        JPanel pantalla = new JPanel(new BorderLayout(0, 10));
        pantalla.setOpaque(false);
        pantalla.setBorder(BorderFactory.createEmptyBorder(22, 28, 10, 20));

        BotonMenu volver = new BotonMenu("‹  Regresar");
        volver.setPreferredSize(new Dimension(120, 36));
        volver.addActionListener(e -> cartas.show(raiz, "cuadricula"));
        
        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        arriba.setOpaque(false);
        arriba.add(volver);
        pantalla.add(arriba, BorderLayout.NORTH);

        detalle.setLayout(new BoxLayout(detalle, BoxLayout.Y_AXIS));
        detalle.setBackground(FONDO);
        detalle.setBorder(BorderFactory.createEmptyBorder(8, 0, 20, 8));
        
        pantalla.add(detalle, BorderLayout.CENTER);
        return pantalla;
    }

    private void cargarDatosVisuales() {
        cuadricula.removeAll();
        cuadricula.add(crearTarjetaFalsa("Vultures 1", "Kanye West", "2024"));
        cuadricula.add(crearTarjetaFalsa("Utopia", "Travis Scott", "2023"));
        cuadricula.add(crearTarjetaFalsa("White Pony", "Deftones", "2000"));
        cuadricula.add(crearTarjetaFalsa("Graduation", "Kanye West", "2007"));
        cuadricula.add(crearTarjetaFalsa("Astroworld", "Travis Scott", "2018"));
        cuadricula.add(crearTarjetaFalsa("We Don't Trust You", "Future", "2024"));
        
        cuadricula.revalidate();
        cuadricula.repaint();
        cartas.show(raiz, "cuadricula");
    }

    private JPanel crearTarjetaFalsa(String nombreAlbum, String nombreArtista, String anio) {
        PanelRedondeado tarjeta = new PanelRedondeado(24, TARJETA);
        tarjeta.setLayout(new BorderLayout(0, 10));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(12, 12, 14, 12));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel caratula = new JLabel();
        caratula.setOpaque(true);
        caratula.setBackground(Color.DARK_GRAY);
        caratula.setPreferredSize(new Dimension(150, 150));
        
        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        
        JLabel lblAlbum = new JLabel(nombreAlbum);
        lblAlbum.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblAlbum.setForeground(TEXTO);
        
        JLabel lblArtista = new JLabel(nombreArtista);
        lblArtista.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblArtista.setForeground(TEXTO_SUAVE);
        
        textos.add(lblAlbum);
        textos.add(lblArtista);

        tarjeta.add(caratula, BorderLayout.CENTER);
        tarjeta.add(textos, BorderLayout.SOUTH);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                mostrarDetalleAlbum(nombreAlbum, nombreArtista, anio);
            }
        });

        return tarjeta;
    }

    private void mostrarDetalleAlbum(String nombreAlbum, String nombreArtista, String anio) {
        detalle.removeAll();

        PanelRedondeado cabecera = new PanelRedondeado(28, TARJETA);
        cabecera.setLayout(new BorderLayout(22, 0));
        cabecera.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 22));
        cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
        cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        
        JLabel caratulaGrande = new JLabel();
        caratulaGrande.setOpaque(true);
        caratulaGrande.setBackground(Color.DARK_GRAY);
        caratulaGrande.setPreferredSize(new Dimension(170, 170));
        cabecera.add(caratulaGrande, BorderLayout.WEST);

        BotonMenu reproducirAlbum = new BotonMenu("Reproducir álbum");
        reproducirAlbum.setHorizontalAlignment(SwingConstants.CENTER);
        reproducirAlbum.setPreferredSize(new Dimension(170, 38));
        
        JPanel zonaBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        zonaBoton.setOpaque(false);
        zonaBoton.add(reproducirAlbum);

        JPanel info = new JPanel(new GridLayout(4, 1, 0, 4));
        info.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(nombreAlbum);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(TEXTO);
        
        JLabel lblArtista = new JLabel(nombreArtista);
        lblArtista.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblArtista.setForeground(TEXTO_SUAVE);
        
        JLabel lblDetalles = new JLabel(anio + "  ·  Hip Hop  ·  5 canciones");
        lblDetalles.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDetalles.setForeground(TEXTO_SUAVE);
        
        info.add(lblTitulo);
        info.add(lblArtista);
        info.add(lblDetalles);
        info.add(zonaBoton);
        
        cabecera.add(info, BorderLayout.CENTER);

        detalle.add(cabecera);
        detalle.add(Box.createVerticalStrut(14));
        
        for (int i = 1; i <= 5; i++) {
            detalle.add(crearFilaCancionFalsa("Canción " + i, nombreArtista, nombreAlbum));
            detalle.add(Box.createVerticalStrut(8));
        }

        detalle.revalidate();
        detalle.repaint();
        cartas.show(raiz, "detalle");
    }

    private JPanel crearFilaCancionFalsa(String titulo, String artista, String album) {
        PanelRedondeado fila = new PanelRedondeado(22, TARJETA);
        fila.setLayout(new BorderLayout(14, 0));
        fila.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 18));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        BotonIcono play = new BotonIcono(BotonIcono.PLAY, true, 36);

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setForeground(TEXTO);
        
        JLabel lblSub = new JLabel(artista + "  ·  " + album);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(TEXTO_SUAVE);
        
        textos.add(lblTitulo);
        textos.add(lblSub);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        derecha.setOpaque(false);
        
        JLabel lblDuracion = new JLabel("3:45");
        lblDuracion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDuracion.setForeground(TEXTO_SUAVE);
        
        JButton mas = new JButton("+");
        mas.setContentAreaFilled(false);
        mas.setBorderPainted(false);
        mas.setFocusPainted(false);
        mas.setFont(new Font("SansSerif", Font.BOLD, 22));
        mas.setForeground(Color.WHITE);
        mas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        derecha.add(lblDuracion);
        derecha.add(mas);

        fila.add(play, BorderLayout.WEST);
        fila.add(textos, BorderLayout.CENTER);
        fila.add(derecha, BorderLayout.EAST);
        
        return fila;
    }
}