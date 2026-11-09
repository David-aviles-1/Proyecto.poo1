package GUI;

import java.awt.BorderLayout;
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class PanelArtistas extends JPanel {

    private final DefaultListModel<String> modelo = new DefaultListModel<>();
    private final JList<String> listaArtistas = new JList<>(modelo);
    private final JPanel detalle = new JPanel();

    private final Color FONDO = new Color(18, 18, 20);
    private final Color TARJETA = new Color(30, 30, 33);
    private final Color TEXTO = Color.WHITE;
    private final Color TEXTO_SUAVE = new Color(160, 160, 167);

    public PanelArtistas() {
        setLayout(new BorderLayout());
        setBackground(FONDO);

        listaArtistas.setBackground(FONDO);
        listaArtistas.setForeground(TEXTO);
        listaArtistas.setFont(new Font("SansSerif", Font.BOLD, 14));
        listaArtistas.setFixedCellHeight(44);
        listaArtistas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 6));
        listaArtistas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarArtista(listaArtistas.getSelectedValue());
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaArtistas);
        scrollLista.setBorder(null);
        scrollLista.getViewport().setBackground(FONDO);
        scrollLista.setPreferredSize(new Dimension(220, 100));
        add(scrollLista, BorderLayout.WEST);

        detalle.setLayout(new BoxLayout(detalle, BoxLayout.Y_AXIS));
        detalle.setBackground(FONDO);
        detalle.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 28));

        JScrollPane scrollDetalle = new JScrollPane(detalle);
        scrollDetalle.setBorder(null);
        scrollDetalle.getViewport().setBackground(FONDO);
        scrollDetalle.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollDetalle, BorderLayout.CENTER);

        cargarArtistasFalsos();
    }

    private void cargarArtistasFalsos() {
        modelo.clear();
        modelo.addElement("Kanye West");
        modelo.addElement("Travis Scott");
        modelo.addElement("Deftones");
        modelo.addElement("Future");

        if (!modelo.isEmpty()) {
            listaArtistas.setSelectedIndex(0);
        }
    }

    private void mostrarArtista(String artista) {
        detalle.removeAll();
        if (artista == null) {
            JLabel vacio = new JLabel("Aún no hay artistas seleccionados.");
            vacio.setForeground(TEXTO_SUAVE);
            detalle.add(vacio);
        } else {
            JLabel nombre = new JLabel(artista);
            nombre.setFont(new Font("SansSerif", Font.BOLD, 28));
            nombre.setForeground(TEXTO);
            nombre.setAlignmentX(Component.LEFT_ALIGNMENT);
            detalle.add(nombre);
            detalle.add(Box.createVerticalStrut(18));

            PanelRedondeado cabecera = new PanelRedondeado(28, TARJETA);
            cabecera.setLayout(new BorderLayout(18, 0));
            cabecera.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 18));
            cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
            cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

            JLabel caratula = new JLabel();
            caratula.setOpaque(true);
            caratula.setBackground(Color.DARK_GRAY);
            caratula.setPreferredSize(new Dimension(120, 120));
            cabecera.add(caratula, BorderLayout.WEST);

            JPanel info = new JPanel(new GridLayout(3, 1));
            info.setOpaque(false);

            JLabel lblAlbum = new JLabel("Álbum Principal");
            lblAlbum.setFont(new Font("SansSerif", Font.BOLD, 20));
            lblAlbum.setForeground(TEXTO);

            JLabel lblSub = new JLabel("2024  ·  Hip Hop");
            lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblSub.setForeground(TEXTO_SUAVE);

            JLabel lblTotal = new JLabel("5 canciones");
            lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblTotal.setForeground(TEXTO_SUAVE);

            info.add(lblAlbum);
            info.add(lblSub);
            info.add(lblTotal);
            cabecera.add(info, BorderLayout.CENTER);

            detalle.add(cabecera);
            detalle.add(Box.createVerticalStrut(12));

            for (int i = 1; i <= 3; i++) {
                detalle.add(crearFilaCancionFalsa("Canción de " + artista + " " + i, artista));
                detalle.add(Box.createVerticalStrut(8));
            }
        }
        detalle.revalidate();
        detalle.repaint();
    }

    private JPanel crearFilaCancionFalsa(String titulo, String artista) {
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

        JLabel lblSub = new JLabel(artista + "  ·  Álbum");
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