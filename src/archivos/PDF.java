package archivos;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;

public class PDF {
    private static final int ANCHO = 595;
    private static final int ALTO = 842;
    private static final int MARGEN = 56;
    private static final int TAM_LETRA = 12;
    private static final int INTERLINEADO = 17;
    private static final Charset WIN_ANSI = Charset.forName("windows-1252");

    private static final int[] ANCHOS = {
        278, 278, 355, 556, 556, 889, 667, 191, 333, 333, 389, 584, 278, 333, 278, 278,
        556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 278, 278, 584, 584, 584, 556,
        1015, 667, 667, 722, 722, 667, 611, 778, 722, 278, 500, 667, 556, 833, 722, 778,
        667, 778, 722, 667, 611, 722, 667, 944, 667, 667, 611, 278, 278, 278, 469, 556,
        333, 556, 556, 500, 556, 556, 278, 556, 556, 222, 222, 500, 222, 833, 556, 556,
        556, 556, 333, 500, 278, 556, 500, 722, 500, 500, 500, 334, 260, 334, 584};

    private static class Linea {
        final String texto;
        Linea(String texto) {
            this.texto = texto;
        }
    }

    public static void generarLetra(String titulo, String artista, String album, int anio,
            File imagen, String letra, File destino) throws IOException {
        byte[] jpeg = prepararImagen(imagen);

        double anchoCabecera = ANCHO - 2 * MARGEN - (jpeg != null ? 112 : 0);
        List<String> lineasTitulo = ajustar(titulo, 22, anchoCabecera, true);
        double anchoCuerpo = ANCHO - 2 * MARGEN;
        List<Linea> cuerpo = new ArrayList<>();
        String texto = letra == null ? "" : letra.replace("\r", "").replace("\t", "    ");
        for (String parrafo : texto.split("\n", -1)) {
            if (parrafo.trim().isEmpty()) {
                cuerpo.add(new Linea(""));
            } else {
                for (String l : ajustar(parrafo, TAM_LETRA, anchoCuerpo, false)) {
                    cuerpo.add(new Linea(l));
                }
            }
        }

        List<ByteArrayOutputStream> paginas = new ArrayList<>();
        int indice = 0;
        boolean primera = true;
        do {
            ByteArrayOutputStream pagina = new ByteArrayOutputStream();
            double y;
            if (primera) {
                y = dibujarCabecera(pagina, titulo, lineasTitulo, artista, album, anio, jpeg != null);
            } else {
                escribir(pagina, "0.55 0.55 0.55 rg BT /F1 9 Tf " + num(MARGEN) + " " + num(ALTO - 36) + " Td ");
                pagina.write(cadenaPdf(titulo + "  -  " + artista));
                escribir(pagina, " Tj ET\n");
                y = ALTO - MARGEN - 14;
            }
            primera = false;
            double limite = 70;
            while (indice < cuerpo.size() && y > limite) {
                String linea = cuerpo.get(indice).texto;
                if (!linea.isEmpty()) {
                    escribir(pagina, "0.12 0.12 0.12 rg BT /F1 " + TAM_LETRA + " Tf " + num(MARGEN) + " " + num(y) + " Td ");
                    pagina.write(cadenaPdf(linea));
                    escribir(pagina, " Tj ET\n");
                }
                y -= INTERLINEADO;
                indice++;
            }
            paginas.add(pagina);
        } while (indice < cuerpo.size());

        for (int i = 0; i < paginas.size(); i++) {
            escribir(paginas.get(i), "0.55 0.55 0.55 rg BT /F1 9 Tf " + num(ANCHO / 2.0 - 25) + " 32 Td ");
            paginas.get(i).write(cadenaPdf("Página " + (i + 1) + " de " + paginas.size()));
            escribir(paginas.get(i), " Tj ET\n");
        }

        List<byte[]> objetos = new ArrayList<>();
        objetos.add(null);
        objetos.add(null);
        objetos.add(ascii("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>"));
        objetos.add(ascii("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>"));
        int numImagen = 0;
        if (jpeg != null) {
            BufferedImage leida = ImageIO.read(new java.io.ByteArrayInputStream(jpeg));
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            escribir(o, "<< /Type /XObject /Subtype /Image /Width " + leida.getWidth() + " /Height " + leida.getHeight()
                    + " /ColorSpace /DeviceRGB /BitsPerComponent 8 /Filter /DCTDecode /Length " + jpeg.length + " >>\nstream\n");
            o.write(jpeg);
            escribir(o, "\nendstream");
            objetos.add(o.toByteArray());
            numImagen = objetos.size();
        }

        StringBuilder kids = new StringBuilder();
        for (ByteArrayOutputStream pagina : paginas) {
            byte[] contenido = pagina.toByteArray();
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            escribir(o, "<< /Length " + contenido.length + " >>\nstream\n");
            o.write(contenido);
            escribir(o, "endstream");
            objetos.add(o.toByteArray());
            int numContenido = objetos.size();

            String recursos = "/Font << /F1 3 0 R /F2 4 0 R >>"
                    + (numImagen > 0 ? " /XObject << /Im1 " + numImagen + " 0 R >>" : "");
            objetos.add(ascii("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 " + ANCHO + " " + ALTO + "] /Resources << "
                    + recursos + " >> /Contents " + numContenido + " 0 R >>"));
            kids.append(objetos.size()).append(" 0 R ");
        }
        objetos.set(0, ascii("<< /Type /Catalog /Pages 2 0 R >>"));
        objetos.set(1, ascii("<< /Type /Pages /Kids [" + kids + "] /Count " + paginas.size() + " >>"));

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        escribir(pdf, "%PDF-1.4\n");
        pdf.write(new byte[]{'%', (byte) 0xE2, (byte) 0xE3, (byte) 0xCF, (byte) 0xD3, '\n'});
        int[] posiciones = new int[objetos.size()];
        for (int i = 0; i < objetos.size(); i++) {
            posiciones[i] = pdf.size();
            escribir(pdf, (i + 1) + " 0 obj\n");
            pdf.write(objetos.get(i));
            escribir(pdf, "\nendobj\n");
        }
        int inicioXref = pdf.size();
        escribir(pdf, "xref\n0 " + (objetos.size() + 1) + "\n0000000000 65535 f \n");
        for (int pos : posiciones) {
            escribir(pdf, String.format(Locale.US, "%010d 00000 n \n", pos));
        }
        escribir(pdf, "trailer\n<< /Size " + (objetos.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + inicioXref + "\n%%EOF\n");

        File carpeta = destino.getParentFile();
        if (carpeta != null) {
            carpeta.mkdirs();
        }
        try (FileOutputStream salida = new FileOutputStream(destino)) {
            salida.write(pdf.toByteArray());
        }
    }

    private static double dibujarCabecera(ByteArrayOutputStream p, String titulo, List<String> lineasTitulo,
            String artista, String album, int anio, boolean conImagen) throws IOException {
        double tope = ALTO - MARGEN;
        double xTexto = MARGEN + (conImagen ? 112 : 0);

        if (conImagen) {
            escribir(p, "q 96 0 0 96 " + num(MARGEN) + " " + num(tope - 96) + " cm /Im1 Do Q\n");
        }
        double y = tope - 20;
        for (String l : lineasTitulo) {
            escribir(p, "0.08 0.08 0.08 rg BT /F2 22 Tf " + num(xTexto) + " " + num(y) + " Td ");
            p.write(cadenaPdf(l));
            escribir(p, " Tj ET\n");
            y -= 27;
        }
        escribir(p, "0.35 0.35 0.35 rg BT /F1 14 Tf " + num(xTexto) + " " + num(y - 2) + " Td ");
        p.write(cadenaPdf(artista));
        escribir(p, " Tj ET\n");
        y -= 22;
        String detalle = album + (anio > 0 ? "  -  " + anio : "");
        escribir(p, "0.5 0.5 0.5 rg BT /F1 11 Tf " + num(xTexto) + " " + num(y - 2) + " Td ");
        p.write(cadenaPdf("Álbum: " + detalle));
        escribir(p, " Tj ET\n");

        double bajo = Math.min(tope - 96, y - 10) - 14;
        escribir(p, "0.75 0.75 0.75 RG 0.8 w " + num(MARGEN) + " " + num(bajo) + " m " + num(ANCHO - MARGEN) + " " + num(bajo) + " l S\n");
        return bajo - 30;
    }

    private static byte[] prepararImagen(File imagen) {
        if (imagen == null || !imagen.exists()) {
            return null;
        }
        try {
            BufferedImage origen = ImageIO.read(imagen);
            if (origen == null) {
                return null;
            }
            int lado = Math.min(origen.getWidth(), origen.getHeight());
            BufferedImage cuadrada = origen.getSubimage((origen.getWidth() - lado) / 2, (origen.getHeight() - lado) / 2, lado, lado);
            int destino = Math.min(400, lado);
            BufferedImage actual = cuadrada;
            while (actual.getWidth() / 2 >= destino) {
                int n = actual.getWidth() / 2;
                actual = escalar(actual, n);
            }
            BufferedImage rgb = new BufferedImage(destino, destino, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgb.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(actual, 0, 0, destino, destino, java.awt.Color.WHITE, null);
            g.dispose();
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            ImageIO.write(rgb, "jpg", salida);
            return salida.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    private static BufferedImage escalar(BufferedImage origen, int n) {
        BufferedImage r = new BufferedImage(n, n, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = r.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(origen, 0, 0, n, n, null);
        g.dispose();
        return r;
    }

    private static List<String> ajustar(String texto, double tam, double anchoMax, boolean negrita) {
        List<String> lineas = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        for (String palabra : texto.split(" ", -1)) {
            while (ancho(palabra, tam, negrita) > anchoMax && palabra.length() > 1) {
                int corte = palabra.length() - 1;
                while (corte > 1 && ancho(palabra.substring(0, corte), tam, negrita) > anchoMax) {
                    corte--;
                }
                if (actual.length() > 0) {
                    lineas.add(actual.toString());
                    actual.setLength(0);
                }
                lineas.add(palabra.substring(0, corte));
                palabra = palabra.substring(corte);
            }
            String prueba = actual.length() == 0 ? palabra : actual + " " + palabra;
            if (ancho(prueba, tam, negrita) <= anchoMax || actual.length() == 0) {
                actual.setLength(0);
                actual.append(prueba);
            } else {
                lineas.add(actual.toString());
                actual.setLength(0);
                actual.append(palabra);
            }
        }
        lineas.add(actual.toString());
        return lineas;
    }

    private static double ancho(String s, double tam, boolean negrita) {
        double total = 0;
        for (char c : s.toCharArray()) {
            if (c >= 32 && c <= 126) {
                total += ANCHOS[c - 32];
            } else if (c >= 0xC0 && c <= 0xDE) {
                total += 667;
            } else {
                total += 556;
            }
        }
        return total / 1000.0 * tam * (negrita ? 1.08 : 1.0);
    }

    private static byte[] cadenaPdf(String s) {
        byte[] b = s.getBytes(WIN_ANSI);
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        o.write('(');
        for (byte x : b) {
            if (x == '(' || x == ')' || x == '\\') {
                o.write('\\');
            }
            o.write(x);
        }
        o.write(')');
        return o.toByteArray();
    }

    private static void escribir(ByteArrayOutputStream o, String s) throws IOException {
        o.write(s.getBytes(StandardCharsets.ISO_8859_1));
    }

    private static byte[] ascii(String s) {
        return s.getBytes(StandardCharsets.ISO_8859_1);
    }

    private static String num(double v) {
        return String.format(Locale.US, "%.2f", v);
    }
}
