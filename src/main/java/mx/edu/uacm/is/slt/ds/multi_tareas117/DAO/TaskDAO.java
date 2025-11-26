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
     * Crea una nueva tarea.
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
            pstmt.setString(5, LocalDateTime.now().toString());
            pstmt.setString(6, due.atTime(23, 59).toString());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todas las tareas (con JOIN para el nombre de usuario).
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.task_id, t.title, t.status_column, t.category_row, " +
                "t.created_at, t.due_date, t.is_paused, u.full_name " +
                "FROM Tasks t " +
                "LEFT JOIN Users u ON t.assigned_user_id = u.user_id";

        Connection conn = DatabaseManager.getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getString("status_column"),
                        rs.getString("category_row"),
                        rs.getString("created_at"),
                        rs.getString("due_date"),
                        rs.getInt("is_paused") == 1,
                        rs.getString("full_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Actualiza una tarea (Edición completa).
     */
    public boolean updateTask(int taskId, String title, String column, String row, String userFullName, LocalDate due) {
        String sql = "UPDATE Tasks SET title = ?, status_column = ?, category_row = ?, " +
                "assigned_user_id = (SELECT user_id FROM Users WHERE full_name = ?), " +
                "due_date = ? " +
                "WHERE task_id = ?";

        Connection conn = DatabaseManager.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, column);
            pstmt.setString(3, row);
            pstmt.setString(4, userFullName);
            pstmt.setString(5, due.atTime(23, 59).toString());
            pstmt.setInt(6, taskId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza posición (Drag & Drop).
     * ¡ESTE FALTABA!
     */
    public void updateTaskPosition(int taskId, String newColumn, String newRow) {
        String sql = "UPDATE Tasks SET status_column = ?, category_row = ? WHERE task_id = ?";
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newColumn);
            pstmt.setString(2, newRow);
            pstmt.setInt(3, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza solo el estatus (Menú contextual).
     */
    public void updateTaskStatus(int taskId, String newColumn) {
        String sql = "UPDATE Tasks SET status_column = ? WHERE task_id = ?";
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newColumn);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pausar / Reanudar.
     */
    public void updateTaskPause(int taskId, boolean isPaused) {
        String sql = "UPDATE Tasks SET is_paused = ? WHERE task_id = ?";
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isPaused ? 1 : 0);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
