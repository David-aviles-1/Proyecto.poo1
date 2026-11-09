package control;

public class ControladorUsuarios {
    // Control de roles de acceso (Administrador y Usuario Normal)
    public static boolean validarAcceso(String usuario, String password, boolean esAdmin) {
        if (esAdmin) {
            return usuario.equals("admin") && password.equals("admin");
        } else {
            return !usuario.isEmpty();
        }
    }
}
