package GUI;

import control.Cancion;
import control.ControladorCanciones;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.*;

public class BarraReproduccion extends JPanel {
    private final ControladorCanciones ctrl;

    private final JLabel lblCaratula = new JLabel();
    private final JLabel lblTitulo = EstiloUI.etiqueta("Sin reproducción", 15, Font.BOLD, EstiloUI.TEXTO);
    private final JLabel lblArtista = EstiloUI.etiqueta("Elige una canción", 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE);
    private final JLabel lblActual = EstiloUI.etiqueta("0:00", 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE);
    private final JLabel lblTotal = EstiloUI.etiqueta("0:00", 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE);

    private final BotonIcono btnAnterior = new BotonIcono(BotonIcono.ANTERIOR, false, 34);
    private final BotonIcono btnPlay = new BotonIcono(BotonIcono.PLAY, true, 46);
    private final BotonIcono btnSiguiente = new BotonIcono(BotonIcono.SIGUIENTE, false, 34);

    private final BarraProgreso progreso;
    private final BarraProgreso volumen;
    private final Timer reloj;

    private Cancion actual;
    private int segundosTotales = 0;
    private int segundosActuales = 0;
    private boolean sonando = false;

    private Runnable alAbrirDetalle;
    private Consumer<Cancion> alCambiarCancion;

    public BarraReproduccion(ControladorCanciones ctrl) {
        this.ctrl = ctrl;
        setLayout(new BorderLayout());
        setBackground(EstiloUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(0, 18, 16, 18));
        setPreferredSize(new Dimension(100, 118));

        PanelRedondeado tarjeta = new PanelRedondeado(30, EstiloUI.TARJETA);
        tarjeta.setLayout(new BorderLayout(24, 0));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 24));
        add(tarjeta, BorderLayout.CENTER);

        JPanel izquierda = new JPanel(new BorderLayout(12, 0));
        izquierda.setOpaque(false);
        izquierda.setPreferredSize(new Dimension(270, 70));
        izquierda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        izquierda.setToolTipText("Ver letra y carátula");
        lblCaratula.setIcon(placeholder());
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(lblTitulo);
        textos.add(lblArtista);
        izquierda.add(lblCaratula, BorderLayout.WEST);
        izquierda.add(textos, BorderLayout.CENTER);
        izquierda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirDetalle();
            }
        });

        progreso = new BarraProgreso(true, f -> {
            if (actual != null) {
                segundosActuales = (int) (f * segundosTotales);
                actualizarProgreso();
            }
        });
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        botones.setOpaque(false);
        botones.add(btnAnterior);
        botones.add(btnPlay);
        botones.add(btnSiguiente);

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        fila.add(lblActual, BorderLayout.WEST);
        fila.add(progreso, BorderLayout.CENTER);
        fila.add(lblTotal, BorderLayout.EAST);

        JPanel centro = new JPanel(new BorderLayout(0, 2));
        centro.setOpaque(false);
        centro.add(botones, BorderLayout.NORTH);
        centro.add(fila, BorderLayout.CENTER);

        BotonMenu btnLetra = new BotonMenu("Letra", EstiloUI.TARJETA_HOVER);
        btnLetra.setActivo(true);
        btnLetra.setHorizontalAlignment(SwingConstants.CENTER);
        btnLetra.setPreferredSize(new Dimension(84, 34));
        btnLetra.addActionListener(e -> abrirDetalle());

        volumen = new BarraProgreso(false, f -> {
        });
        volumen.setFraccion(0.7);
        volumen.setPreferredSize(new Dimension(100, 16));

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 18));
        derecha.setOpaque(false);
        derecha.setPreferredSize(new Dimension(240, 70));
        derecha.add(btnLetra);
        derecha.add(volumen);

        tarjeta.add(izquierda, BorderLayout.WEST);
        tarjeta.add(centro, BorderLayout.CENTER);
        tarjeta.add(derecha, BorderLayout.EAST);

        btnPlay.addActionListener(e -> alternarPlayPausa());
        btnSiguiente.addActionListener(e -> saltar(+1));
        btnAnterior.addActionListener(e -> saltar(-1));

        reloj = new Timer(1000, e -> {
            if (sonando && actual != null) {
                segundosActuales++;
                if (segundosActuales >= segundosTotales) {
                    saltar(+1);
                } else {
                    actualizarProgreso();
                }
            }
        });
        reloj.start();
    }

    public void setAlAbrirDetalle(Runnable accion) {
        this.alAbrirDetalle = accion;
    }

    public void setAlCambiarCancion(Consumer<Cancion> accion) {
        this.alCambiarCancion = accion;
    }

    private void abrirDetalle() {
        if (alAbrirDetalle != null) {
            alAbrirDetalle.run();
        }
    }

    public void reproducir(Cancion c) {
        actual = c;
        segundosTotales = convertirASegundos(c.getDuracion());
        segundosActuales = 0;
        sonando = true;
        lblTitulo.setText(c.getTitulo());
        lblArtista.setText(c.getArtista() + "  ·  " + c.getAlbum());
        lblCaratula.setIcon(EstiloUI.caratula(c, 64));
        lblTotal.setText(formato(segundosTotales));
        progreso.setSemilla(c.getTitulo().hashCode());
        btnPlay.setTipo(BotonIcono.PAUSA);
        actualizarProgreso();
        if (alCambiarCancion != null) {
            alCambiarCancion.accept(c);
        }
    }

    private void alternarPlayPausa() {
        if (actual == null) {
            ArrayList<Cancion> todas = ctrl.listarTodas();
            if (!todas.isEmpty()) {
                reproducir(todas.get(0));
            }
            return;
        }
        sonando = !sonando;
        btnPlay.setTipo(sonando ? BotonIcono.PAUSA : BotonIcono.PLAY);
    }

    private void saltar(int paso) {
        ArrayList<Cancion> todas = ctrl.listarTodas();
        if (todas.isEmpty()) {
            return;
        }
        int indice = 0;
        if (actual != null) {
            for (int i = 0; i < todas.size(); i++) {
                if (todas.get(i).getClave().equals(actual.getClave())) {
                    indice = i;
                    break;
                }
            }
            indice = (indice + paso + todas.size()) % todas.size();
        }
        reproducir(todas.get(indice));
    }

    private void actualizarProgreso() {
        lblActual.setText(formato(segundosActuales));
        progreso.setFraccion(segundosTotales == 0 ? 0 : (double) segundosActuales / segundosTotales);
    }

    private ImageIcon placeholder() {
        Cancion vacia = new Cancion();
        vacia.setAlbum("♪");
        return EstiloUI.caratula(vacia, 64);
    }

    private int convertirASegundos(String duracion) {
        try {
            String[] partes = duracion.split(":");
            return Integer.parseInt(partes[0].trim()) * 60 + Integer.parseInt(partes[1].trim());
        } catch (Exception e) {
            return 180;
        }
    }

    private String formato(int segundos) {
        return (segundos / 60) + ":" + String.format("%02d", segundos % 60);
    }
}
