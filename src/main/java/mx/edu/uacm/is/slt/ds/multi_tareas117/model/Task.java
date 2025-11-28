package mx.edu.uacm.is.slt.ds.multi_tareas117.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private String statusColumn;
    private String categoryRow;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private boolean isPaused;
    private String assignedUser;

    // Constructor
    public Task(int id, String title, String statusColumn, String categoryRow, String createdAtStr, String dueDateStr,boolean isPaused, String assignedUser) {
        this.id = id;
        this.title = title;
        this.statusColumn = statusColumn;
        this.categoryRow = categoryRow;
        // Convertimos String de la BD a Objetos de Fecha
        this.createdAt = LocalDateTime.parse(createdAtStr);
        this.dueDate = LocalDateTime.parse(dueDateStr);
        this.isPaused = isPaused;
        this.assignedUser = assignedUser;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatusColumn() { return statusColumn; }
    public String getCategoryRow() { return categoryRow; }
    public LocalDateTime getDueDate() { return dueDate; }
    public boolean isPaused() { return isPaused; }
    public String getAssignedUser() { return assignedUser; }

    public void setStatusColumn(String statusColumn) { this.statusColumn = statusColumn; }
    public void setCategoryRow(String categoryRow) { this.categoryRow = categoryRow; }
    public void setPaused(boolean paused) { isPaused = paused; }

    /**
     * LÓGICA DE PROGRESO DEL TIEMPO
     * Retorna un valor entre 0.0 (inicio) y 1.0 (se acabó el tiempo).
     */
    public double getTimeProgress() {
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(dueDate)) return 1.0;

        long totalMinutes = Duration.between(createdAt, dueDate).toMinutes();
        long elapsedMinutes = Duration.between(createdAt, now).toMinutes();

        if (totalMinutes <= 0) return 1.0;

        return (double) elapsedMinutes / totalMinutes;
    }

    public String getFormattedDueDate() {
        return dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}