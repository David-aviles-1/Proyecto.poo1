package control;

public class Cancion {
    private String clave;
    private String titulo;
    private String genero;
    private String artista;
    private int anio;
    private String album;
    private String duracion;
    private String rutaImagen;
    private String rutaLetraPdf;
    private String letra;

    public Cancion() {
    }

    public Cancion(String clave, String titulo, String genero, String artista,
                   int anio, String album, String duracion,
                   String rutaImagen, String rutaLetraPdf) {
        this.clave = clave;
        this.titulo = titulo;
        this.genero = genero;
        this.artista = artista;
        this.anio = anio;
        this.album = album;
        this.duracion = duracion;
        this.rutaImagen = rutaImagen;
        this.rutaLetraPdf = rutaLetraPdf;
    }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }

    public String getLetra() { return letra; }
    public void setLetra(String letra) { this.letra = letra; }

    public String getRutaLetraPdf() { return rutaLetraPdf; }
    public void setRutaLetraPdf(String rutaLetraPdf) { this.rutaLetraPdf = rutaLetraPdf; }
}
