package mx.edu.uacm.is.slt.ds.multi_tareas117.util;

public class SessionManager {

    private static String loggedInUsername;

    /**
     * Se llama al iniciar sesión para "guardar" al usuario.
     */
    public static void login(String username) {
        loggedInUsername = username;
        System.out.println("Session: Usuario " + username + " ha iniciado sesión.");
    }

    /**
     * Se usa para "cerrar sesión" (limpiar la variable).
     */
    public static void logout() {
        loggedInUsername = null;
    }

    /**
     * Lo usaremos en la vista de "Perfil" para saber a quién modificar.
     */
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static boolean isLoggedIn() {
        return loggedInUsername != null;
    }
}
