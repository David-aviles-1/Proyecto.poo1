package archivos;

import java.io.File;
import java.io.IOException;

public class ManejadorArchivos {
    public static void verificarYCrearArchivo() {
        try {
            File archivo = new File("canciones_data.txt");
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el archivo de datos: " + e.getMessage());
        }
    }
}
