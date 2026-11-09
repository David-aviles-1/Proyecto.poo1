package GUI;

import archivos.ManejadorArchivos;
import control.Cancion;
import java.awt.*;
import java.awt.Desktop;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;

public class PanelReproduciendo extends JPanel {
    private Cancion actual;

    private final JTextArea letra = new JTextArea();
    private final JLabel caratula = new JLabel();
    private final JLabel titulo = EstiloUI.etiqueta(" ", 22, Font.BOLD, EstiloUI.TEXTO);
    private final JLabel artista = EstiloUI.etiqueta(" ", 14, Font.PLAIN, EstiloUI.TEXTO_SUAVE);
    private final JLabel detalles = EstiloUI.etiqueta(" ", 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE);

    public PanelReproduciendo(Runnable alVolver) {
        setLayout(new BorderLayout(0, 0));
        setBackground(EstiloUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 28, 10, 28));

        BotonMenu volver = new BotonMenu("‹  Volver", EstiloUI.TARJETA);
        volver.setActivo(true);
        volver.setPreferredSize(new Dimension(110, 36));
        volver.addActionListener(e -> alVolver.run());
        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        arriba.setOpaque(false);
        arriba.add(volver);
        add(arriba, BorderLayout.NORTH);

        letra.setEditable(false);
        letra.setOpaque(false);
        letra.setLineWrap(true);
        letra.setWrapStyleWord(true);
        letra.setFont(EstiloUI.fuente(Font.BOLD, 26));
        letra.setForeground(new Color(255, 255, 255, 235));
        letra.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 30));
        JScrollPane scroll = new JScrollPane(letra);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        EstiloUI.estilizarScroll(scroll);
        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 20));
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        PanelRedondeado tarjeta = new PanelRedondeado(30, new Color(0, 0, 0, 80));
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        tarjeta.setPreferredSize(new Dimension(340, 100));

        caratula.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel datos = new JPanel(new GridLayout(3, 1, 0, 2));
        datos.setOpaque(false);
        datos.add(titulo);
        datos.add(artista);
        datos.add(detalles);

        JPanel botonesPdf = new JPanel(new GridLayout(1, 3, 8, 0));
        botonesPdf.setOpaque(false);
        botonesPdf.add(botonPdf("Ver PDF", e -> verPdf()));
        botonesPdf.add(botonPdf("Imprimir", e -> imprimirPdf()));
        botonesPdf.add(botonPdf("Descargar", e -> descargarPdf()));

        JPanel abajoTarjeta = new JPanel(new BorderLayout(0, 12));
        abajoTarjeta.setOpaque(false);
        abajoTarjeta.add(datos, BorderLayout.CENTER);
        abajoTarjeta.add(botonesPdf, BorderLayout.SOUTH);

        tarjeta.add(caratula, BorderLayout.NORTH);
        tarjeta.add(abajoTarjeta, BorderLayout.CENTER);
        add(tarjeta, BorderLayout.EAST);

        mostrar(null);
    }

    private BotonMenu botonPdf(String texto, java.awt.event.ActionListener accion) {
        BotonMenu b = new BotonMenu(texto, EstiloUI.TARJETA_HOVER);
        b.setActivo(true);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setFont(EstiloUI.fuente(Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        b.setPreferredSize(new Dimension(90, 34));
        b.addActionListener(accion);
        return b;
    }

    public void mostrar(Cancion c) {
        actual = c;
        if (c == null) {
            Cancion vacia = new Cancion();
            vacia.setAlbum("♪");
            caratula.setIcon(EstiloUI.caratula(vacia, 270));
            titulo.setText("Sin reproducción");
            artista.setText("Elige una canción para ver su letra");
            detalles.setText(" ");
            letra.setText("Aquí aparecerá la letra de la canción que estés escuchando.");
        } else {
            caratula.setIcon(EstiloUI.caratula(c, 270));
            titulo.setText(c.getTitulo());
            artista.setText(c.getArtista() + "  ·  " + c.getAlbum());
            detalles.setText(c.getAnio() + "  ·  " + c.getGenero() + "  ·  " + c.getDuracion());
            String texto = c.getLetra();
            letra.setText(texto == null || texto.trim().isEmpty()
                    ? "La letra de esta canción está en el PDF. Usa \"Ver PDF\" para abrirla." : texto);
        }
        letra.setCaretPosition(0);
        repaint();
    }

    private File pdfActual() {
        if (actual == null) {
            DialogoEstilo.mensaje(this, "Sin canción", "Primero elige una canción.");
            return null;
        }
        String ruta = actual.getRutaLetraPdf();
        File f = (ruta == null || ruta.isEmpty()) ? null : new File(ruta);
        if (f == null || !f.exists()) {
            DialogoEstilo.mensaje(this, "Sin PDF", "Esta canción todavía no tiene un PDF de letra.");
            return null;
        }
        return f;
    }

    private void verPdf() {
        File f = pdfActual();
        if (f == null) {
            return;
        }
        String error = ManejadorArchivos.abrirConSistema(f);
        if (error != null) {
            DialogoEstilo.mensaje(this, "No se pudo abrir", error);
        }
    }

    private void imprimirPdf() {
        File f = pdfActual();
        if (f == null) {
            return;
        }
        String error = ManejadorArchivos.imprimirPdf(f);
        if (error != null) {
            DialogoEstilo.mensaje(this, "No se pudo imprimir", error);
        } else {
            DialogoEstilo.mensaje(this, "Impresión", "El PDF se envió a la impresora.");
        }
    }

    private void descargarPdf() {
        File f = pdfActual();
        if (f == null) {
            return;
        }
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File(actual.getTitulo() + ".pdf"));
        if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.copy(f.toPath(), selector.getSelectedFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
                DialogoEstilo.mensaje(this, "Listo", "PDF guardado correctamente.");
            } catch (Exception ex) {
                DialogoEstilo.mensaje(this, "Error", "No se pudo guardar el PDF.");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        float tono = actual == null ? 0.5f : EstiloUI.tonoAlbum(actual.getAlbum());
        Color arriba = Color.getHSBColor(0f, 0f, 0.20f + 0.16f * tono);
        g2.setPaint(new GradientPaint(0, 0, arriba, getWidth() * 0.8f, getHeight(), EstiloUI.FONDO));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
