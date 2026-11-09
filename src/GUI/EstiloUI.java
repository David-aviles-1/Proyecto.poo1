package GUI;

import control.Cancion;
import control.ControladorCanciones;
import control.ListaReproduccion;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class EstiloUI {
    public static final Color FONDO = new Color(18, 18, 20);
    public static final Color FONDO_LATERAL = new Color(11, 11, 12);
    public static final Color TARJETA = new Color(30, 30, 33);
    public static final Color TARJETA_HOVER = new Color(46, 46, 50);
    public static final Color ACENTO = new Color(245, 245, 247);
    public static final Color ACENTO2 = new Color(165, 165, 172);
    public static final Color TEXTO = Color.WHITE;
    public static final Color TEXTO_SUAVE = new Color(160, 160, 167);
    public static final Color TEXTO_MENU = new Color(205, 205, 211);
    public static final Color TEXTO_SOBRE_ACENTO = new Color(18, 18, 20);

    private static final String FAMILIA = elegirFamilia();

    private static String elegirFamilia() {
        String[] preferidas = {"Inter", "Segoe UI", "SF Pro Text", "Noto Sans", "Cantarell",
            "Ubuntu", "Roboto", "Helvetica Neue", "Arial"};
        Set<String> instaladas = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String f : preferidas) {
            if (instaladas.contains(f)) {
                return f;
            }
        }
        return "SansSerif";
    }

    public static Font fuente(int estilo, int tam) {
        return new Font(FAMILIA, estilo, tam);
    }

    public static void calidad(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    private static BufferedImage escalarSuave(BufferedImage origen, int tam) {
        int lado = Math.min(origen.getWidth(), origen.getHeight());
        BufferedImage actual = origen.getSubimage((origen.getWidth() - lado) / 2,
                (origen.getHeight() - lado) / 2, lado, lado);
        while (actual.getWidth() / 2 >= tam) {
            int nuevo = actual.getWidth() / 2;
            BufferedImage reducida = new BufferedImage(nuevo, nuevo, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = reducida.createGraphics();
            calidad(g);
            g.drawImage(actual, 0, 0, nuevo, nuevo, null);
            g.dispose();
            actual = reducida;
        }
        return actual;
    }

    public static JLabel etiqueta(String texto, int tam, int estilo, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente(estilo, tam));
        l.setForeground(color);
        return l;
    }

    public static JButton botonTexto(String texto) {
        JButton b = new JButton(texto);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setForeground(TEXTO);
        b.setFont(fuente(Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static float tonoAlbum(String album) {
        String a = album == null ? "?" : album;
        if (a.equals("♪")) {
            return 0.90f;
        }
        return (Math.abs(a.hashCode()) % 360) / 360f;
    }

    private static final java.util.Map<String, ImageIcon> CACHE = new java.util.HashMap<>();

    public static ImageIcon caratula(Cancion c, int tam) {
        File archivo = null;
        String claveCache = null;
        String ruta = c.getRutaImagen();
        if (ruta != null && !ruta.isEmpty()) {
            File f = new File(ruta);
            if (f.exists()) {
                archivo = f;
                claveCache = ruta + "|" + f.lastModified() + "|" + tam;
                ImageIcon guardado = CACHE.get(claveCache);
                if (guardado != null) {
                    return guardado;
                }
            }
        }

        BufferedImage out = new BufferedImage(tam, tam, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        calidad(g);

        g.setColor(Color.WHITE);
        g.fill(new RoundRectangle2D.Float(0, 0, tam, tam, tam / 4f, tam / 4f));
        g.setComposite(AlphaComposite.SrcIn);

        BufferedImage img = null;
        if (archivo != null) {
            try {
                img = ImageIO.read(archivo);
            } catch (Exception e) {
                img = null;
            }
        }

        if (img != null) {
            g.drawImage(escalarSuave(img, tam), 0, 0, tam, tam, null);
        } else {
            float tono = tonoAlbum(c.getAlbum());
            Color c1 = Color.getHSBColor(0f, 0f, 0.50f + 0.30f * tono);
            Color c2 = Color.getHSBColor(0f, 0f, 0.14f + 0.14f * tono);
            g.setPaint(new GradientPaint(0, 0, c1, tam, tam, c2));
            g.fillRect(0, 0, tam, tam);
            g.setColor(new Color(255, 255, 255, 215));
            String album = c.getAlbum() == null || c.getAlbum().isEmpty() ? "?" : c.getAlbum();
            if (album.equals("\u266A")) {
                dibujarNotaMusical(g, tam);
            } else {
                g.setFont(fuente(Font.BOLD, tam / 2));
                String inicial = album.substring(0, 1).toUpperCase();
                FontMetrics fm = g.getFontMetrics();
                g.drawString(inicial, (tam - fm.stringWidth(inicial)) / 2,
                        (tam - fm.getHeight()) / 2 + fm.getAscent());
            }
        }
        g.dispose();
        ImageIcon icono = new ImageIcon(out);
        if (claveCache != null && img != null) {
            if (CACHE.size() > 400) {
                CACHE.clear();
            }
            CACHE.put(claveCache, icono);
        }
        return icono;
    }

    private static void dibujarNotaMusical(Graphics2D g, int s) {
        AffineTransform original = g.getTransform();
        g.rotate(-0.35, s * 0.41, s * 0.665);
        g.fillOval((int) (s * 0.30), (int) (s * 0.58), (int) (s * 0.23), (int) (s * 0.17));
        g.setTransform(original);
        g.fillRect((int) (s * 0.50), (int) (s * 0.27), Math.max(2, (int) (s * 0.045)), (int) (s * 0.42));
        Path2D bandera = new Path2D.Double();
        bandera.moveTo(s * 0.545, s * 0.27);
        bandera.curveTo(s * 0.70, s * 0.31, s * 0.74, s * 0.45, s * 0.64, s * 0.56);
        bandera.curveTo(s * 0.68, s * 0.46, s * 0.62, s * 0.41, s * 0.545, s * 0.40);
        bandera.closePath();
        g.fill(bandera);
    }

    public static JPanel filaCancion(Cancion c, Consumer<Cancion> reproducir, Runnable anadir) {
        PanelRedondeado fila = new PanelRedondeado(22, TARJETA);
        fila.setLayout(new BorderLayout(14, 0));
        fila.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 18));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.activarHover(TARJETA_HOVER);

        BotonIcono play = new BotonIcono(BotonIcono.PLAY, true, 36);
        play.addActionListener(e -> reproducir.accept(c));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(etiqueta(c.getTitulo(), 14, Font.BOLD, TEXTO));
        textos.add(etiqueta(c.getArtista() + "  ·  " + c.getAlbum(), 12, Font.PLAIN, TEXTO_SUAVE));

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        derecha.setOpaque(false);
        derecha.add(etiqueta(c.getDuracion(), 13, Font.PLAIN, TEXTO_SUAVE));
        if (anadir != null) {
            JButton mas = botonTexto("+");
            mas.setToolTipText("Añadir a una lista de reproducción");
            mas.setFont(fuente(Font.BOLD, 22));
            mas.setForeground(ACENTO);
            mas.addActionListener(e -> anadir.run());
            derecha.add(mas);
        }

        fila.add(play, BorderLayout.WEST);
        fila.add(textos, BorderLayout.CENTER);
        fila.add(derecha, BorderLayout.EAST);
        return fila;
    }

    public static void anadirALista(Component padre, ControladorCanciones ctrl, Cancion c) {
        ArrayList<ListaReproduccion> listas = ctrl.getListas();
        if (listas.isEmpty()) {
            DialogoEstilo.mensaje(padre, "Sin listas", "Todavía no hay listas. Crea una en \"Listas de reproducción\".");
            return;
        }
        String[] nombres = new String[listas.size()];
        for (int i = 0; i < nombres.length; i++) {
            nombres[i] = listas.get(i).getNombre();
        }
        String elegido = DialogoEstilo.elegir(padre, "Añadir a lista", "¿A qué lista la quieres añadir?", nombres);
        if (elegido == null) {
            return;
        }
        if (ctrl.agregarALista(elegido, c)) {
            DialogoEstilo.mensaje(padre, "Listo", "Canción añadida a \"" + elegido + "\".");
        } else {
            DialogoEstilo.mensaje(padre, "Repetida", "Esa canción ya está en \"" + elegido + "\".");
        }
    }

    public static void estilizarScroll(JScrollPane panel) {
        JScrollBar barra = panel.getVerticalScrollBar();
        barra.setPreferredSize(new Dimension(12, 0));
        barra.setOpaque(false);
        barra.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(255, 255, 255, 70);
                trackColor = new Color(0, 0, 0, 0);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return botonVacio();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return botonVacio();
            }

            private JButton botonVacio() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle area) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle area) {
                Graphics2D g2 = (Graphics2D) g.create();
                EstiloUI.calidad(g2);
                g2.setColor(thumbColor);
                g2.fillRoundRect(area.x + 2, area.y, area.width - 4, area.height, 8, 8);
                g2.dispose();
            }
        });
    }
}
