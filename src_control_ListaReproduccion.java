package control;

import java.util.ArrayList;

public class ListaReproduccion {
    private String nombreLista;
    private ArrayList<Cancion> canciones;

    public ListaReproduccion(String nombreLista) {
        this.nombreLista = nombreLista;
        this.canciones = new ArrayList<>();
    }

    public void agregarCancion(Cancion c) {
        canciones.add(c);
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }
}
