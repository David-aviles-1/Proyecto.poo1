package GUI;

import control.Cancion;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FormularioCancion extends JPanel {
    private static File ultimaCarpeta = null;

    private final CampoTexto tfClave = new CampoTexto("Ej. C001");
    private final CampoTexto tfTitulo = new CampoTexto();
    private final CampoTexto tfGenero = new CampoTexto("Ej. Rock");
    private final CampoTexto tfArtista = new CampoTexto();
    private final CampoTexto tfAnio = new CampoTexto("Ej. 2018");
    private final CampoTexto tfAlbum = new CampoTexto();
    private final CampoTexto tfDuracion = new CampoTexto("m:ss  (ej. 3:46)");
    private final JTextArea taLetra = new JTextArea();

    private final JLabel preview = new JLabel();
    private final JLabel lblImagen = EstiloUI.etiqueta("Ninguna imagen elegida", 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE);
    private final JLabel lblPdf = EstiloUI.etiqueta("Sin PDF propio", 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE);

    private File imagenElegida = null;
    private File pdfElegido = null;
    private String rutaImagenActual = null;
    private String rutaPdfActual = null;

    public FormularioCancion(boolean claveEditable) {
        tfClave.setEditable(claveEditable);
        setOpaque(false);
        setLayout(new BorderLayout(26, 0));

        add(crearColumnaDatos(), BorderLayout.CENTER);
        add(crearColumnaArchivos(), BorderLayout.EAST);
        actualizarPreview();
    }

    private JPanel crearColumnaDatos() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        anadir(p, campo("Clave", tfClave), 0, 0, 1, 0);
        anadir(p, campo("Año", tfAnio), 1, 0, 1, 0);
        anadir(p, campo("Título", tfTitulo), 0, 1, 2, 0);
        anadir(p, campo("Artista", tfArtista), 0, 2, 1, 0);
        anadir(p, campo("Álbum", tfAlbum), 1, 2, 1, 0);
        anadir(p, campo("Género", tfGenero), 0, 3, 1, 0);
        anadir(p, campo("Duración", tfDuracion), 1, 3, 1, 0);

        taLetra.setOpaque(false);
        taLetra.setForeground(EstiloUI.TEXTO);
        taLetra.setCaretColor(Color.WHITE);
        taLetra.setFont(EstiloUI.fuente(Font.PLAIN, 13));
        taLetra.setLineWrap(true);
        taLetra.setWrapStyleWord(true);
        taLetra.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane scroll = new JScrollPane(taLetra);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        EstiloUI.estilizarScroll(scroll);
        PanelRedondeado cajaLetra = new PanelRedondeado(14, EstiloUI.TARJETA_HOVER);
        cajaLetra.setLayout(new BorderLayout());
        cajaLetra.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        cajaLetra.setPreferredSize(new Dimension(300, 170));
        cajaLetra.add(scroll, BorderLayout.CENTER);
        anadir(p, campo("Letra de la canción (texto)", cajaLetra), 0, 4, 2, 1);
        return p;
    }

    private JPanel crearColumnaArchivos() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(230, 100));

        p.add(alinear(EstiloUI.etiqueta("Carátula", 12, Font.BOLD, EstiloUI.TEXTO_SUAVE)));
        p.add(Box.createVerticalStrut(6));
        preview.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(preview);
        p.add(Box.createVerticalStrut(10));

        BotonMenu elegirImagen = boton("Elegir imagen…");
        elegirImagen.addActionListener(e -> elegirImagen());
        p.add(elegirImagen);
        p.add(Box.createVerticalStrut(4));
        p.add(alinear(lblImagen));
        p.add(alinear(EstiloUI.etiqueta("PNG o JPG, mínimo 300×300 px", 11, Font.PLAIN, EstiloUI.TEXTO_SUAVE)));

        p.add(Box.createVerticalStrut(22));
        p.add(alinear(EstiloUI.etiqueta("PDF de la letra (opcional)", 12, Font.BOLD, EstiloUI.TEXTO_SUAVE)));
        p.add(Box.createVerticalStrut(6));
        BotonMenu elegirPdf = boton("Elegir PDF…");
        elegirPdf.addActionListener(e -> elegirPdf());
        p.add(elegirPdf);
        p.add(Box.createVerticalStrut(4));
        p.add(alinear(lblPdf));
        p.add(alinear(EstiloUI.etiqueta("<html>Si no eliges uno, se crea solo con la<br>letra del cuadro y la carátula.</html>",
                11, Font.PLAIN, EstiloUI.TEXTO_SUAVE)));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private BotonMenu boton(String texto) {
        BotonMenu b = new BotonMenu(texto, EstiloUI.TARJETA_HOVER);
        b.setActivo(true);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setPreferredSize(new Dimension(190, 38));
        b.setMaximumSize(new Dimension(190, 38));
        return b;
    }

    private JComponent alinear(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private JPanel campo(String etiqueta, JComponent componente) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        p.add(EstiloUI.etiqueta(etiqueta, 12, Font.BOLD, EstiloUI.TEXTO_SUAVE), BorderLayout.NORTH);
        p.add(componente, BorderLayout.CENTER);
        return p;
    }

    private void anadir(JPanel p, JComponent c, int x, int y, int ancho, double pesoY) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x;
        g.gridy = y;
        g.gridwidth = ancho;
        g.weightx = 1;
        g.weighty = pesoY;
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(0, 0, 14, x == 0 && ancho == 1 ? 14 : 0);
        p.add(c, g);
    }

    private void elegirImagen() {
        JFileChooser fc = new JFileChooser(ultimaCarpeta);
        fc.setDialogTitle("Elige la carátula");
        fc.setFileFilter(new FileNameExtensionFilter("Imágenes (PNG, JPG)", "png", "jpg", "jpeg"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imagenElegida = fc.getSelectedFile();
            ultimaCarpeta = imagenElegida.getParentFile();
            lblImagen.setText(recortar(imagenElegida.getName()));
            actualizarPreview();
        }
    }

    private void elegirPdf() {
        JFileChooser fc = new JFileChooser(ultimaCarpeta);
        fc.setDialogTitle("Elige el PDF de la letra");
        fc.setFileFilter(new FileNameExtensionFilter("Documentos PDF", "pdf"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            pdfElegido = fc.getSelectedFile();
            ultimaCarpeta = pdfElegido.getParentFile();
            lblPdf.setText(recortar(pdfElegido.getName()));
        }
    }

    private String recortar(String s) {
        return s.length() > 28 ? s.substring(0, 25) + "…" : s;
    }

    private void actualizarPreview() {
        Cancion tmp = new Cancion();
        tmp.setAlbum("♪");
        tmp.setRutaImagen(imagenElegida != null ? imagenElegida.getPath() : rutaImagenActual);
        preview.setIcon(EstiloUI.caratula(tmp, 190));
    }

    public Cancion leerCancion() {
        Cancion c = new Cancion();
        c.setClave(tfClave.getText().trim());
        c.setTitulo(tfTitulo.getText().trim());
        c.setGenero(tfGenero.getText().trim());
        c.setArtista(tfArtista.getText().trim());
        c.setAlbum(tfAlbum.getText().trim());
        c.setDuracion(tfDuracion.getText().trim());
        try {
            c.setAnio(Integer.parseInt(tfAnio.getText().trim()));
        } catch (NumberFormatException e) {
            c.setAnio(-1);
        }
        c.setLetra(taLetra.getText());
        return c;
    }

    public File getImagenElegida() {
        return imagenElegida;
    }

    public File getPdfElegido() {
        return pdfElegido;
    }

    public void cargar(Cancion c) {
        tfClave.setText(c.getClave());
        tfTitulo.setText(c.getTitulo());
        tfGenero.setText(c.getGenero());
        tfArtista.setText(c.getArtista());
        tfAnio.setText(String.valueOf(c.getAnio()));
        tfAlbum.setText(c.getAlbum());
        tfDuracion.setText(c.getDuracion());
        taLetra.setText(c.getLetra() == null ? "" : c.getLetra());
        taLetra.setCaretPosition(0);
        imagenElegida = null;
        pdfElegido = null;
        rutaImagenActual = c.getRutaImagen();
        rutaPdfActual = c.getRutaLetraPdf();
        lblImagen.setText("Se conserva la actual");
        lblPdf.setText(rutaPdfActual != null && rutaPdfActual.endsWith("_propio.pdf") ? "Se conserva el PDF propio" : "Se genera automáticamente");
        actualizarPreview();
    }

    public void limpiar() {
        for (CampoTexto t : new CampoTexto[]{tfClave, tfTitulo, tfGenero, tfArtista, tfAnio, tfAlbum, tfDuracion}) {
            t.setText("");
        }
        taLetra.setText("");
        imagenElegida = null;
        pdfElegido = null;
        rutaImagenActual = null;
        rutaPdfActual = null;
        lblImagen.setText("Ninguna imagen elegida");
        lblPdf.setText("Sin PDF propio");
        actualizarPreview();
    }
}
