package mx.edu.uacm.is.slt.ds.multi_tareas117.DAO;

import mx.edu.uacm.is.slt.ds.multi_tareas117.db.DatabaseManager;
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.Task;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    /**
     * Guarda tarea con fechas.
     * Recibe LocalDate del DatePicker y lo convierte a LocalDateTime (fin del día).
     */
    public boolean createTask(String title, String column, String row, String userFullName, LocalDate due) {
        String sql = "INSERT INTO Tasks (title, status_column, category_row, assigned_user_id, created_at, due_date) " +
                "VALUES (?, ?, ?, (SELECT user_id FROM Users WHERE full_name = ?), ?, ?)";

        Connection conn = DatabaseManager.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, column);
            pstmt.setString(3, row);
            pstmt.setString(4, userFullName);

            // Guardar fechas como String ISO-8601
            pstmt.setString(5, LocalDateTime.now().toString()); // Creado AHORA
            pstmt.setString(6, due.atTime(23, 59).toString()); // Vence ese día al final (23:59)

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear tarea: " + e.getMessage());
            return false;
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        // ¡IMPORTANTE! Pedir también created_at, due_date y is_paused
        String sql = "SELECT task_id, title, status_column, category_row, created_at, due_date, is_paused FROM Tasks";
        Connection conn = DatabaseManager.getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getString("status_column"),
                        rs.getString("category_row"),
                        rs.getString("created_at"), // Pasamos el string de fecha
                        rs.getString("due_date"),
                        rs.getInt("is_paused") == 1
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
        }
        return tasks;
    }

    // ... (El método updateTaskPosition queda IGUAL, no cambia)
    public void updateTaskPosition(int taskId, String newColumn, String newRow) {
        String sql = "UPDATE Tasks SET status_column = ?, category_row = ? WHERE task_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newColumn);
            pstmt.setString(2, newRow);
            pstmt.setInt(3, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Actualiza SOLO la columna (status) de una tarea.
     */
    public void updateTaskStatus(int taskId, String newColumn) {
        String sql = "UPDATE Tasks SET status_column = ? WHERE task_id = ?";
        updateField(sql, newColumn, taskId);
    }

    /**
     * Alterna el estado de pausa.
     */
    public void updateTaskPause(int taskId, boolean isPaused) {
        String sql = "UPDATE Tasks SET is_paused = ? WHERE task_id = ?";

        // 1. Pedimos la conexión FUERA del try
        Connection conn = DatabaseManager.getConnection();

        // 2. Solo el PreparedStatement va dentro del try
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isPaused ? 1 : 0);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ... (updateTaskPosition para el drag-and-drop sigue igual) ...

    // Helper privado para no repetir código
    private void updateField(String sql, String value, int id) {
        // 1. Conexión afuera
        Connection conn = DatabaseManager.getConnection();

        // 2. Try solo para el statement
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, value);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
