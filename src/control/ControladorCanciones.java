package control;

import archivos.PDF;
import archivos.ManejadorArchivos;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.Collator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import javax.imageio.ImageIO;

public class ControladorCanciones {
    public static final int MIN_LADO_IMAGEN = 300;
    public static final int MAX_LADO_IMAGEN = 4000;

    private final ManejadorArchivos archivos = new ManejadorArchivos();
    private final Collator alfabetico = Collator.getInstance(Locale.forLanguageTag("es"));
    private final ArrayList<Cancion> canciones;
    private final ArrayList<ListaReproduccion> listas = new ArrayList<>();

    public ControladorCanciones() {
        alfabetico.setStrength(Collator.SECONDARY);
        canciones = archivos.cargarCanciones();
        ordenar();

        LinkedHashMap<String, ArrayList<String>> guardadas = archivos.cargarListas();
        for (String nombre : guardadas.keySet()) {
            ListaReproduccion lista = new ListaReproduccion(nombre);
            for (String clave : guardadas.get(nombre)) {
                Cancion c = consultarCancion(clave);
                if (c != null) {
                    lista.agregar(c);
                }
            }
            listas.add(lista);
        }
    }

    public ArrayList<Cancion> listarTodas() {
        return canciones;
    }

    public int total() {
        return canciones.size();
    }

    public String agregarCancion(Cancion c, File imagen, File pdfPropio) {
        normalizar(c);
        String error = validarCampos(c, null);
        if (error != null) {
            return error;
        }
        if (imagen == null) {
            return "Debes elegir una imagen de carátula.";
        }
        if ((error = validarImagen(imagen)) != null) {
            return error;
        }
        if (pdfPropio != null && (error = validarPdf(pdfPropio)) != null) {
            return error;
        }
        if (pdfPropio == null && vacio(c.getLetra())) {
            return "Escribe la letra de la canción o elige un PDF con la letra.";
        }
        try {
            c.setRutaImagen(archivos.copiarArchivo(imagen, ManejadorArchivos.DIR_IMAGENES,
                    c.getClave() + "." + extension(imagen)));
            archivos.guardarLetra(c.getClave(), c.getLetra());
            if (pdfPropio != null) {
                c.setRutaLetraPdf(archivos.copiarArchivo(pdfPropio, ManejadorArchivos.DIR_PDF, c.getClave() + "_propio.pdf"));
            } else {
                generarPdfDeLetra(c);
            }
            canciones.add(c);
            ordenar();
            guardarTodo();
            return null;
        } catch (Exception e) {
            return "No se pudo guardar la canción: " + e.getMessage();
        }
    }

    public Cancion consultarCancion(String clave) {
        if (clave == null) {
            return null;
        }
        for (Cancion c : canciones) {
            if (c.getClave().equalsIgnoreCase(clave.trim())) {
                return c;
            }
        }
        return null;
    }

    public String modificarCancion(String clave, Cancion nuevos, File imagenNueva, File pdfNuevo) {
        Cancion actual = consultarCancion(clave);
        if (actual == null) {
            return "No existe una canción con la clave " + clave + ".";
        }
        nuevos.setClave(actual.getClave());
        normalizar(nuevos);
        String error = validarCampos(nuevos, actual);
        if (error != null) {
            return error;
        }
        if (imagenNueva != null && (error = validarImagen(imagenNueva)) != null) {
            return error;
        }
        if (pdfNuevo != null && (error = validarPdf(pdfNuevo)) != null) {
            return error;
        }
        boolean tienePdfPropio = pdfNuevo != null || esPdfPropio(actual.getRutaLetraPdf());
        if (!tienePdfPropio && vacio(nuevos.getLetra())) {
            return "Escribe la letra de la canción o elige un PDF con la letra.";
        }
        try {
            actual.setTitulo(nuevos.getTitulo());
            actual.setGenero(nuevos.getGenero());
            actual.setArtista(nuevos.getArtista());
            actual.setAnio(nuevos.getAnio());
            actual.setAlbum(nuevos.getAlbum());
            actual.setDuracion(nuevos.getDuracion());
            actual.setLetra(nuevos.getLetra());

            if (imagenNueva != null) {
                String nueva = archivos.copiarArchivo(imagenNueva, ManejadorArchivos.DIR_IMAGENES,
                        actual.getClave() + "." + extension(imagenNueva));
                if (!nueva.equals(actual.getRutaImagen())) {
                    archivos.borrarArchivo(actual.getRutaImagen());
                }
                actual.setRutaImagen(nueva);
            }
            archivos.guardarLetra(actual.getClave(), actual.getLetra());
            if (pdfNuevo != null) {
                if (!esPdfPropio(actual.getRutaLetraPdf())) {
                    archivos.borrarArchivo(actual.getRutaLetraPdf());
                }
                actual.setRutaLetraPdf(archivos.copiarArchivo(pdfNuevo, ManejadorArchivos.DIR_PDF, actual.getClave() + "_propio.pdf"));
            } else if (!esPdfPropio(actual.getRutaLetraPdf())) {
                generarPdfDeLetra(actual);
            }
            ordenar();
            guardarTodo();
            return null;
        } catch (Exception e) {
            return "No se pudo guardar los cambios: " + e.getMessage();
        }
    }

    public boolean eliminarCancion(String clave) {
        Cancion c = consultarCancion(clave);
        if (c == null) {
            return false;
        }
        canciones.remove(c);
        for (ListaReproduccion l : listas) {
            l.getCanciones().remove(c);
        }
        archivos.borrarArchivo(c.getRutaImagen());
        archivos.borrarArchivo(ManejadorArchivos.DIR_PDF + "/" + c.getClave() + ".pdf");
        archivos.borrarArchivo(ManejadorArchivos.DIR_PDF + "/" + c.getClave() + "_propio.pdf");
        archivos.borrarArchivo(ManejadorArchivos.DIR_LETRAS + "/" + c.getClave() + ".txt");
        guardarTodo();
        return true;
    }

    public String validarCampos(Cancion c, Cancion ignorar) {
        if (vacio(c.getClave())) {
            return "La clave es obligatoria.";
        }
        if (!c.getClave().matches("[A-Za-z0-9_-]{1,12}")) {
            return "La clave solo puede tener letras, números, guion y guion bajo (máximo 12 caracteres).";
        }
        if (vacio(c.getTitulo())) {
            return "El título es obligatorio.";
        }
        if (c.getTitulo().length() > 60) {
            return "El título no puede pasar de 60 caracteres.";
        }
        if (vacio(c.getGenero())) {
            return "El género es obligatorio.";
        }
        if (!c.getGenero().matches("[\\p{L} .&/-]{2,30}")) {
            return "El género solo puede tener letras (de 2 a 30 caracteres).";
        }
        if (vacio(c.getArtista())) {
            return "El artista es obligatorio.";
        }
        if (c.getArtista().length() > 50) {
            return "El nombre del artista no puede pasar de 50 caracteres.";
        }
        int maximo = LocalDate.now().getYear() + 1;
        if (c.getAnio() < 1900 || c.getAnio() > maximo) {
            return "El año debe ser un número entre 1900 y " + maximo + ".";
        }
        if (vacio(c.getAlbum())) {
            return "El álbum es obligatorio.";
        }
        if (c.getAlbum().length() > 60) {
            return "El nombre del álbum no puede pasar de 60 caracteres.";
        }
        if (vacio(c.getDuracion()) || !c.getDuracion().matches("\\d{1,2}:[0-5]\\d") || c.getDuracion().equals("0:00")
                || c.getDuracion().equals("00:00")) {
            return "La duración debe tener el formato minutos:segundos, por ejemplo 3:46.";
        }

        for (Cancion otra : canciones) {
            if (otra == ignorar) {
                continue;
            }
            if (otra.getClave().equalsIgnoreCase(c.getClave())) {
                return "Ya existe una canción con la clave " + c.getClave() + ".";
            }
            if (otra.getTitulo().equalsIgnoreCase(c.getTitulo()) && otra.getArtista().equalsIgnoreCase(c.getArtista())) {
                return "Esa canción ya está registrada (mismo título y mismo artista).";
            }
        }
        return null;
    }

    public String validarImagen(File f) {
        if (f == null || !f.exists()) {
            return "No se encontró el archivo de la imagen.";
        }
        String ext = extension(f);
        if (!ext.equals("png") && !ext.equals("jpg") && !ext.equals("jpeg")) {
            return "La carátula debe ser una imagen PNG o JPG.";
        }
        try {
            BufferedImage img = ImageIO.read(f);
            if (img == null) {
                return "El archivo elegido no es una imagen válida.";
            }
            if (img.getWidth() < MIN_LADO_IMAGEN || img.getHeight() < MIN_LADO_IMAGEN) {
                return "La imagen es muy pequeña (" + img.getWidth() + "x" + img.getHeight()
                        + " px). Debe medir al menos " + MIN_LADO_IMAGEN + "x" + MIN_LADO_IMAGEN + " px.";
            }
            if (img.getWidth() > MAX_LADO_IMAGEN || img.getHeight() > MAX_LADO_IMAGEN) {
                return "La imagen es demasiado grande (" + img.getWidth() + "x" + img.getHeight()
                        + " px). El máximo es " + MAX_LADO_IMAGEN + "x" + MAX_LADO_IMAGEN + " px.";
            }
        } catch (Exception e) {
            return "No se pudo leer la imagen.";
        }
        return null;
    }

    public String validarPdf(File f) {
        if (f == null || !f.exists()) {
            return "No se encontró el archivo PDF.";
        }
        if (!f.getName().toLowerCase().endsWith(".pdf")) {
            return "La letra debe ser un archivo con extensión .pdf.";
        }
        try (FileInputStream in = new FileInputStream(f)) {
            byte[] inicio = new byte[5];
            if (in.read(inicio) < 5 || !new String(inicio, "ISO-8859-1").equals("%PDF-")) {
                return "El archivo elegido no es un PDF válido.";
            }
        } catch (Exception e) {
            return "No se pudo leer el PDF.";
        }
        return null;
    }

    public ArrayList<ListaReproduccion> getListas() {
        return listas;
    }

    public boolean crearLista(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.trim().length() > 30
                || buscarLista(nombre.trim()) != null) {
            return false;
        }
        listas.add(new ListaReproduccion(nombre.trim()));
        guardarTodo();
        return true;
    }

    public ListaReproduccion buscarLista(String nombre) {
        for (ListaReproduccion l : listas) {
            if (l.getNombre().equalsIgnoreCase(nombre)) {
                return l;
            }
        }
        return null;
    }

    public boolean agregarALista(String nombreLista, Cancion c) {
        ListaReproduccion l = buscarLista(nombreLista);
        if (l == null || !l.agregar(c)) {
            return false;
        }
        guardarTodo();
        return true;
    }

    private void generarPdfDeLetra(Cancion c) throws Exception {
        String ruta = ManejadorArchivos.DIR_PDF + "/" + c.getClave() + ".pdf";
        PDF.generarLetra(c.getTitulo(), c.getArtista(), c.getAlbum(), c.getAnio(),
                new File(c.getRutaImagen()), c.getLetra(), new File(ruta));
        c.setRutaLetraPdf(ruta);
    }

    private void guardarTodo() {
        archivos.guardarCanciones(canciones);
        LinkedHashMap<String, ArrayList<String>> mapa = new LinkedHashMap<>();
        for (ListaReproduccion l : listas) {
            ArrayList<String> claves = new ArrayList<>();
            for (Cancion c : l.getCanciones()) {
                claves.add(c.getClave());
            }
            mapa.put(l.getNombre(), claves);
        }
        archivos.guardarListas(mapa);
    }

    private void ordenar() {
        canciones.sort((a, b) -> alfabetico.compare(a.getTitulo(), b.getTitulo()));
    }

    private void normalizar(Cancion c) {
        c.setClave(limpio(c.getClave()));
        c.setTitulo(limpio(c.getTitulo()));
        c.setGenero(limpio(c.getGenero()));
        c.setArtista(limpio(c.getArtista()));
        c.setAlbum(limpio(c.getAlbum()));
        c.setDuracion(limpio(c.getDuracion()));
        c.setLetra(c.getLetra() == null ? "" : c.getLetra().replace("\r", "").trim());
    }

    private String limpio(String s) {
        return s == null ? "" : s.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ').trim();
    }

    private boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean esPdfPropio(String ruta) {
        return ruta != null && ruta.endsWith("_propio.pdf");
    }

    private String extension(File f) {
        String n = f.getName().toLowerCase();
        int i = n.lastIndexOf('.');
        return i < 0 ? "" : n.substring(i + 1);
    }
}
