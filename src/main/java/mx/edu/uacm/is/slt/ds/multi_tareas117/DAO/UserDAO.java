package mx.edu.uacm.is.slt.ds.multi_tareas117.DAO;

import mx.edu.uacm.is.slt.ds.multi_tareas117.db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.User;
public class UserDAO {
    /**
     * Busca un usuario por username y password.
     */
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password_hash = ?";

        // ¡ARREGLO! NO usamos try-with-resources en la Connection
        Connection conn = DatabaseManager.getConnection();

        // SÍ usamos try-with-resources en PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // true si encontró al usuario

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }

    // (Opcional) Un método para crear un usuario de prueba si la BD está vacía
    public void createTestUser() {
        String checkSql = "SELECT COUNT(*) FROM Users";
        String insertSql = "INSERT INTO Users(username, password_hash, full_name, avatar_icon) VALUES(?,?,?,?)";

        // ¡ARREGLO! NO usamos try-with-resources en la Connection
        Connection conn = DatabaseManager.getConnection();

        // SÍ usamos try-with-resources en los Statements
        try (Statement checkStmt = conn.createStatement();
             PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {

            ResultSet rs = checkStmt.executeQuery(checkSql);
            if (rs.getInt(1) == 0) {
                System.out.println("Base de datos vacía, creando usuario de prueba 'admin'...");
                insertPstmt.setString(1, "admin");
                insertPstmt.setString(2, "1234");
                insertPstmt.setString(3, "Usuario Administrador");
                insertPstmt.setString(4, "fas-user-shield");
                insertPstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Error al crear usuario de prueba: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     * Devuelve true si fue exitoso, false si el usuario ya existía.
     */
    public boolean registerUser(String username, String password, String fullName, String avatar) {
        String sql = "INSERT INTO Users(username, password_hash, full_name, avatar_icon) VALUES(?,?,?,?)";

        Connection conn = DatabaseManager.getConnection();

        // Usamos try-with-resources en el PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // TODO: Hashear la contraseña
            pstmt.setString(3, fullName);
            pstmt.setString(4, avatar);

            pstmt.executeUpdate();

            System.out.println("¡Nuevo usuario registrado!: " + username);
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 19 ) {
                System.err.println("Error al registrar: El usuario '" + username + "' ya existe.");
            } else {
                System.err.println("Error al registrar usuario: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Busca en la BD y devuelve un objeto User con los datos del perfil.
     * (Usamos el modelo User que ya creamos)
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT full_name, avatar_icon FROM Users WHERE username = ?";
        Connection conn = DatabaseManager.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String avatar = rs.getString("avatar_icon");
                return new User(fullName, "Online", avatar);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza el perfil del usuario en la BD.
     * Devuelve true si fue exitoso.
     */
    public boolean updateUserProfile(String username, String newFullName, String newPassword) {
        String sql = "UPDATE Users SET full_name = ?"
                + (newPassword.isEmpty() ? "" : ", password_hash = ?")
                + " WHERE username = ?";

        Connection conn = DatabaseManager.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newFullName);
            if (!newPassword.isEmpty()) {
                pstmt.setString(2, newPassword); // TODO: Hashear esto
                pstmt.setString(3, username);
            } else {
                pstmt.setString(2, username);
            }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar perfil: " + e.getMessage());
            return false;
        }
    }

}
