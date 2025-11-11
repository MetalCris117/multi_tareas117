package mx.edu.uacm.is.slt.ds.multi_tareas117.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:multi_tareas_117.db";
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Conexión a SQLite establecida.");
                initializeDatabase(conn);
            } catch (SQLException e) {
                System.err.println("Error al conectar a la BD: " + e.getMessage());
            }
        }
        return conn;
    }

    public static void close() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión a SQLite cerrada.");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Crea las tablas necesarias si no existen.
     * ¡CAMBIO! Ahora recibe la conexión como parámetro.
     */
    private static void initializeDatabase(Connection connectionToUse) {

        String usersTableSql = "CREATE TABLE IF NOT EXISTS Users ("
                + " user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " password_hash TEXT NOT NULL,"
                + " full_name TEXT NOT NULL,"
                + " avatar_icon TEXT"
                + ");";

        String tasksTableSql = "CREATE TABLE IF NOT EXISTS Tasks ("
                + " task_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " title TEXT NOT NULL,"
                + " status_column TEXT NOT NULL,"
                + " category_row TEXT NOT NULL,"
                + " assigned_user_id INTEGER,"
                + " FOREIGN KEY (assigned_user_id) REFERENCES Users(user_id)"
                + ");";

        String messagesTableSql = "CREATE TABLE IF NOT EXISTS Messages ("
                + " message_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " sender_id INTEGER NOT NULL,"
                + " receiver_id INTEGER NOT NULL,"
                + " content TEXT NOT NULL,"
                + " timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + " FOREIGN KEY (sender_id) REFERENCES Users(user_id),"
                + " FOREIGN KEY (receiver_id) REFERENCES Users(user_id)"
                + ");";

        try (Statement stmt = connectionToUse.createStatement()) {
            stmt.execute(usersTableSql);
            stmt.execute(tasksTableSql);
            stmt.execute(messagesTableSql);
            System.out.println("Tablas de la BD verificadas/creadas.");
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }
}