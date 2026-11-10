package archivos;

import control.Cancion;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class ManejadorArchivos {
    public static final String DIR_DATOS = "datos";
    public static final String DIR_IMAGENES = DIR_DATOS + "/imagenes";
    public static final String DIR_PDF = DIR_DATOS + "/pdf";
    public static final String DIR_LETRAS = DIR_DATOS + "/letras";

    private static final String ARCH_CANCIONES = DIR_DATOS + "/canciones.txt";
    private static final String ARCH_USUARIOS = DIR_DATOS + "/usuarios.txt";
    private static final String ARCH_LISTAS = DIR_DATOS + "/listas.txt";

    public ManejadorArchivos() {
        new File(DIR_IMAGENES).mkdirs();
        new File(DIR_PDF).mkdirs();
        new File(DIR_LETRAS).mkdirs();
    }

    public ArrayList<Cancion> cargarCanciones() {
        ArrayList<Cancion> resultado = new ArrayList<>();
        for (String linea : leerLineas(ARCH_CANCIONES)) {
            if (linea.trim().isEmpty() || linea.startsWith("#")) {
                continue;
            }
            String[] p = linea.split("\t", -1);
            if (p.length < 9) {
                continue;
            }
            int anio;
            try {
                anio = Integer.parseInt(p[4].trim());
            } catch (NumberFormatException e) {
                anio = 0;
            }
            Cancion c = new Cancion(p[0], p[1], p[2], p[3], anio, p[5], p[6], p[7], p[8]);
            c.setLetra(leerLetra(c.getClave()));
            resultado.add(c);
        }
        return resultado;
    }

    public boolean guardarCanciones(List<Cancion> canciones) {
        List<String> lineas = new ArrayList<>();
        lineas.add("# clave<TAB>titulo<TAB>genero<TAB>artista<TAB>anio<TAB>album<TAB>duracion<TAB>imagen<TAB>pdf");
        for (Cancion c : canciones) {
            lineas.add(String.join("\t", limpiar(c.getClave()), limpiar(c.getTitulo()), limpiar(c.getGenero()),
                    limpiar(c.getArtista()), String.valueOf(c.getAnio()), limpiar(c.getAlbum()),
                    limpiar(c.getDuracion()), limpiar(c.getRutaImagen()), limpiar(c.getRutaLetraPdf())));
        }
        return escribirLineas(ARCH_CANCIONES, lineas);
    }

    public String leerLetra(String clave) {
        Path p = Paths.get(DIR_LETRAS, clave + ".txt");
        try {
            if (Files.exists(p)) {
                return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
        }
        return "";
    }

    public void guardarLetra(String clave, String texto) throws IOException {
        Path p = Paths.get(DIR_LETRAS, clave + ".txt");
        if (texto == null || texto.trim().isEmpty()) {
            Files.deleteIfExists(p);
        } else {
            Files.write(p, texto.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String copiarArchivo(File origen, String carpeta, String nombreDestino) throws IOException {
        new File(carpeta).mkdirs();
        Path destino = Paths.get(carpeta, nombreDestino);
        if (origen.toPath().toAbsolutePath().normalize().equals(destino.toAbsolutePath().normalize())) {
            return carpeta + "/" + nombreDestino;
        }
        Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return carpeta + "/" + nombreDestino;
    }

    public void borrarArchivo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return;
        }
        try {
            Files.deleteIfExists(Paths.get(ruta));
        } catch (IOException e) {
        }
    }

    public ArrayList<String[]> cargarUsuarios() {
        ArrayList<String[]> resultado = new ArrayList<>();
        for (String linea : leerLineas(ARCH_USUARIOS)) {
            if (linea.trim().isEmpty() || linea.startsWith("#")) {
                continue;
            }
            String[] p = linea.split("\t", -1);
            if (p.length >= 3) {
                resultado.add(new String[]{p[0].trim(), p[1], p[2].trim()});
            }
        }
        return resultado;
    }

    public boolean guardarUsuarios(List<String[]> usuarios) {
        List<String> lineas = new ArrayList<>();
        lineas.add("# usuario<TAB>contrasena<TAB>rol (admin o normal)");
        for (String[] u : usuarios) {
            lineas.add(limpiar(u[0]) + "\t" + limpiar(u[1]) + "\t" + limpiar(u[2]));
        }
        return escribirLineas(ARCH_USUARIOS, lineas);
    }

    public LinkedHashMap<String, ArrayList<String>> cargarListas() {
        LinkedHashMap<String, ArrayList<String>> resultado = new LinkedHashMap<>();
        for (String linea : leerLineas(ARCH_LISTAS)) {
            if (linea.trim().isEmpty() || linea.startsWith("#")) {
                continue;
            }
            String[] p = linea.split("\t", -1);
            ArrayList<String> claves = new ArrayList<>();
            if (p.length > 1 && !p[1].trim().isEmpty()) {
                for (String k : p[1].split(";")) {
                    if (!k.trim().isEmpty()) {
                        claves.add(k.trim());
                    }
                }
            }
            resultado.put(p[0].trim(), claves);
        }
        return resultado;
    }

    public boolean guardarListas(Map<String, ? extends List<String>> listas) {
        List<String> lineas = new ArrayList<>();
        lineas.add("# nombre<TAB>clave1;clave2;clave3");
        for (Map.Entry<String, ? extends List<String>> e : listas.entrySet()) {
            lineas.add(limpiar(e.getKey()) + "\t" + String.join(";", e.getValue()));
        }
        return escribirLineas(ARCH_LISTAS, lineas);
    }

    public static String abrirConSistema(File archivo) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(archivo);
                return null;
            }
        } catch (Exception e) {
        }
        try {
            String so = System.getProperty("os.name", "").toLowerCase();
            String[] comando = so.contains("mac") ? new String[]{"open", archivo.getAbsolutePath()}
                    : so.contains("win") ? new String[]{"cmd", "/c", "start", "", archivo.getAbsolutePath()}
                    : new String[]{"xdg-open", archivo.getAbsolutePath()};
            new ProcessBuilder(comando).start();
            return null;
        } catch (Exception e) {
            return "No se encontró un programa para abrir el PDF.";
        }
    }

    public static String imprimirPdf(File pdf) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.PRINT)) {
                Desktop.getDesktop().print(pdf);
                return null;
            }
        } catch (Exception e) {
        }
        try {
            PrintService impresora = PrintServiceLookup.lookupDefaultPrintService();
            if (impresora == null) {
                return "No hay una impresora configurada.";
            }
            try (FileInputStream in = new FileInputStream(pdf)) {
                DocPrintJob trabajo = impresora.createPrintJob();
                trabajo.print(new SimpleDoc(in, DocFlavor.INPUT_STREAM.AUTOSENSE, null), null);
            }
            return null;
        } catch (Exception e) {
            return "No se pudo imprimir. Abre el PDF e imprímelo desde el visor.";
        }
    }

    private List<String> leerLineas(String ruta) {
        try {
            Path p = Paths.get(ruta);
            if (Files.exists(p)) {
                return Files.readAllLines(p, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
        }
        return new ArrayList<>();
    }

    private boolean escribirLineas(String ruta, List<String> lineas) {
        try {
            new File(DIR_DATOS).mkdirs();
            Files.write(Paths.get(ruta), lineas, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String limpiar(String s) {
        return s == null ? "" : s.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ').trim();
    }
}
