package control;

import java.util.ArrayList;

public class ListaReproduccion {
    private String nombre;
    private ArrayList<Cancion> canciones;

    public ListaReproduccion(String nombre) {
        this.nombre = nombre;
        this.canciones = new ArrayList<>();
    }

    public String getNombre() { return nombre; }

    public ArrayList<Cancion> getCanciones() { return canciones; }

    public boolean contiene(Cancion c) {
        for (Cancion x : canciones) {
            if (x.getClave().equals(c.getClave())) {
                return true;
            }
        }
        return false;
    }

    public boolean agregar(Cancion c) {
        if (contiene(c)) {
            return false;
        }
        canciones.add(c);
        return true;
    }
}
