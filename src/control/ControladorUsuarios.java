package control;

import archivos.ManejadorArchivos;
import java.util.ArrayList;

public class ControladorUsuarios {
    public static final String ROL_ADMIN = "admin";
    public static final String ROL_NORMAL = "normal";

    private final ManejadorArchivos archivos = new ManejadorArchivos();
    private final ArrayList<String[]> usuarios;

    public ControladorUsuarios() {
        usuarios = archivos.cargarUsuarios();
        if (usuarios.isEmpty()) {
            usuarios.add(new String[]{"admin", "admin123", ROL_ADMIN});
            usuarios.add(new String[]{"usuario", "usuario123", ROL_NORMAL});
            archivos.guardarUsuarios(usuarios);
        }
    }

    public String iniciarSesion(String usuario, String contrasena) {
        if (usuario == null || contrasena == null) {
            return null;
        }
        for (String[] u : usuarios) {
            if (u[0].equalsIgnoreCase(usuario.trim()) && u[1].equals(contrasena)) {
                return u[2];
            }
        }
        return null;
    }
}
