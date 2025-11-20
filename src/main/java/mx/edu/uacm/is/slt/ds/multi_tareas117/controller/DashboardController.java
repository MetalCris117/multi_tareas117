package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.edu.uacm.is.slt.ds.multi_tareas117.Main;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.TaskDAO;
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.Task;
import atlantafx.base.theme.PrimerDark;
import javafx.scene.control.ProgressBar;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class DashboardController {

    @FXML
    private GridPane kanbanGrid;
    @FXML
    private Button createTaskButton;

    private TaskDAO taskDAO = new TaskDAO();
    private Node draggedCard; // Para drag-and-drop

    @FXML
    public void initialize() {
        System.out.println("Tablero Kanban cargado. Refrescando desde la BD...");
        refreshKanbanBoard(); // Carga las tareas al iniciar
    }

    /**
     * Se llama al hacer clic en "Crear Tarea".
     * Implementa el [Opcion Crear Tarea] -> [Aparece Modal]
     */
    @FXML
    private void onCreateTaskClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/mx/edu/uacm/is/slt/ds/multi_tareas117/views/pages/new-task-view.fxml"));
            Parent root = fxmlLoader.load();

            // Obtenemos el controlador del modal
            NewTaskController modalController = fxmlLoader.getController();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(new PrimerDark().getUserAgentStylesheet()); // Aplica el tema

            Stage modalStage = new Stage();
            modalStage.setTitle("Crear Nueva Tarea");
            modalStage.setScene(scene);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(createTaskButton.getScene().getWindow());
            modalStage.setResizable(false);

            modalStage.showAndWait(); // Espera a que el modal se cierre

            // Diagrama: [Actualizar Kanban]
            if (modalController.isTaskCreated()) {
                refreshKanbanBoard();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpia el tablero y lo vuelve a dibujar desde la BD.
     * Implementa el [Actualizar Kanban]
     */
    private void refreshKanbanBoard() {
        // 1. Limpiar solo las celdas y tarjetas (nodos dinámicos)
        kanbanGrid.getChildren().removeIf(node -> node instanceof VBox);

        // 2. Obtener todas las tareas de la BD
        List<Task> tasks = taskDAO.getAllTasks();

        // 3. Dibujar cada tarea en el tablero
        for (Task task : tasks) {
            // Crear la "tarjeta" (StackPane)
            StackPane card = createCard(task);

            // Encontrar la celda correcta (Columna, Fila)
            int colIndex = getColumnIndex(task.getStatusColumn());
            int rowIndex = getRowIndex(task.getCategoryRow());

            // Añadir la tarjeta a la celda
            addCardToCell(card, colIndex, rowIndex);
        }
    }

    /**
     * Crea el StackPane visual para una tarea.
     */
    private StackPane createCard(Task task) {
        StackPane card = new StackPane();
        card.getStyleClass().add("kanban-card");
        card.setMaxWidth(Double.MAX_VALUE);
        card.setUserData(task);

        VBox cardContent = new VBox(5);
        cardContent.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(task.getTitle());
        titleLabel.setWrapText(true);

        ProgressBar timeProgress = new ProgressBar();
        timeProgress.setMaxWidth(Double.MAX_VALUE);
        timeProgress.setPrefHeight(10);

        // --- LÓGICA VISUAL ACTUALIZADA ---

        // CASO 1: Tarea Finalizada ("done")
        if (task.getStatusColumn().equals("done")) {
            timeProgress.setProgress(1.0); // Barra llena
            timeProgress.setStyle("-fx-accent: #4caf50;"); // Verde Brillante
            titleLabel.setStyle("-fx-text-fill: #888; -fx-strikethrough: true;"); // Texto gris y tachado
        }
        // CASO 2: Tarea Pausada
        else if (task.isPaused()) {
            double progress = task.getTimeProgress();
            timeProgress.setProgress(progress);
            timeProgress.setStyle("-fx-accent: #78909c;"); // Gris Azulado
            titleLabel.setText("⏸ " + task.getTitle());
        }
        // CASO 3: Tarea Activa (Calculamos urgencia)
        else {
            double progress = task.getTimeProgress();
            timeProgress.setProgress(progress);

            if (progress >= 1.0) timeProgress.setStyle("-fx-accent: #ef5350;"); // Rojo
            else if (progress > 0.75) timeProgress.setStyle("-fx-accent: #ffca28;"); // Amarillo
            else timeProgress.setStyle("-fx-accent: #29b6f6;"); // Azul
        }

        // ... (El resto del método sigue igual: Label de fecha, añadir hijos, setupDraggable...)
        Label dateLabel = new Label("Vence: " + task.getFormattedDueDate());
        dateLabel.getStyleClass().add("text-small");
        dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #b0bec5;");

        cardContent.getChildren().addAll(titleLabel, timeProgress, dateLabel);
        card.getChildren().add(cardContent);

        setupDraggable(card);
        setupContextMenu(card, task); // <-- Asegúrate de llamar al menú

        return card;
    }
    /**
     * Añade una tarjeta a una celda del GridPane.
     * Si la celda (VBox) no existe, la crea.
     */
    private void addCardToCell(StackPane card, int colIndex, int rowIndex) {
        VBox cell = getCell(colIndex, rowIndex);
        if (cell == null) {
            // Si no existe, crearla
            cell = new VBox();
            cell.getStyleClass().add("kanban-cell");
            cell.setMaxWidth(Double.MAX_VALUE);
            setupDropTarget(cell);
            kanbanGrid.add(cell, colIndex, rowIndex);
        }
        VBox.setMargin(card, new Insets(5));
        cell.getChildren().add(card);
    }

    private int getColumnIndex(String statusId) {
        switch (statusId) {
            case "todo": return 1;
            case "week": return 2;
            case "doing": return 3;
            case "done": return 4;
            default: return 1;
        }
    }

    private int getRowIndex(String rowId) {
        switch (rowId) {
            case "rrhh": return 1;
            case "ventas": return 2;
            case "mktg": return 3;
            case "atc": return 4;
            default: return 1;
        }
    }

    private VBox getCell(int col, int row) {
        for (Node node : kanbanGrid.getChildren()) {
            if (node instanceof VBox && GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (VBox) node;
            }
        }
        return null;
    }

    private void setupDraggable(Node card) {
        card.setOnDragDetected(event -> {
            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("kanban-card");
            db.setContent(content);
            draggedCard = card;
            event.consume();
        });
    }

    private void setupDropTarget(VBox cell) {
        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString() && draggedCard != null) {
                VBox oldParent = (VBox) draggedCard.getParent();
                oldParent.getChildren().remove(draggedCard);
                cell.getChildren().add(draggedCard);
                success = true;

                Task task = (Task) draggedCard.getUserData();
                int newCol = GridPane.getColumnIndex(cell);
                int newRow = GridPane.getRowIndex(cell);

                String newColId = getColumnId(newCol);
                String newRowId = getRowId(newRow);

                task.setStatusColumn(newColId);
                task.setCategoryRow(newRowId);
                taskDAO.updateTaskPosition(task.getId(), newColId, newRowId);

                if (newColId.equals("done") && task.isPaused()) {
                    task.setPaused(false);
                    taskDAO.updateTaskPause(task.getId(), false); // Actualizamos en BD
                }

                refreshKanbanBoard();
            }

            event.setDropCompleted(success);
            draggedCard = null;
            event.consume();
        });

        cell.setOnDragExited(event -> cell.getStyleClass().remove("drag-over"));

        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString() && draggedCard != null) {
                VBox oldParent = (VBox) draggedCard.getParent();
                oldParent.getChildren().remove(draggedCard);
                cell.getChildren().add(draggedCard);
                success = true;

                Task task = (Task) draggedCard.getUserData();
                int newCol = GridPane.getColumnIndex(cell);
                int newRow = GridPane.getRowIndex(cell);

                String newColId = getColumnId(newCol);
                String newRowId = getRowId(newRow);

                task.setStatusColumn(newColId);
                task.setCategoryRow(newRowId);
                taskDAO.updateTaskPosition(task.getId(), newColId, newRowId);
            }

            event.setDropCompleted(success);
            draggedCard = null;
            event.consume();
        });
    }

    /**
     * Crea el menú de opciones (clic derecho).
     */
    private void setupContextMenu(StackPane card, Task task) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem moveTodo = new MenuItem("Mover a: Por Hacer");
        moveTodo.setOnAction(e -> updateTaskStatus(task, "todo"));

        MenuItem moveWeek = new MenuItem("Mover a: Esta Semana");
        moveWeek.setOnAction(e -> updateTaskStatus(task, "week"));

        MenuItem moveDoing = new MenuItem("Mover a: Haciendo");
        moveDoing.setOnAction(e -> updateTaskStatus(task, "doing"));

        MenuItem moveDone = new MenuItem("Mover a: Hecho");
        moveDone.setOnAction(e -> updateTaskStatus(task, "done"));

        contextMenu.getItems().addAll(moveTodo, moveWeek, moveDoing, moveDone);

        if (!task.getStatusColumn().equals("done")) {
            contextMenu.getItems().add(new SeparatorMenuItem());

            MenuItem togglePause = new MenuItem(task.isPaused() ? "▶ Reanudar Tarea" : "⏸ Pausar Tarea");
            togglePause.setOnAction(e -> {
                boolean newState = !task.isPaused();
                taskDAO.updateTaskPause(task.getId(), newState);
                refreshKanbanBoard();
            });

            contextMenu.getItems().add(togglePause);
        }

        card.setOnContextMenuRequested(e -> {
            contextMenu.show(card, e.getScreenX(), e.getScreenY());
        });
    }
    /**
     * Helper para mover tareas desde el menú
     */
    private void updateTaskStatus(Task task, String newColumnId) {
        taskDAO.updateTaskStatus(task.getId(), newColumnId);
        refreshKanbanBoard();
    }

    private String getColumnId(int colIndex) {
        switch (colIndex) {
            case 1: return "todo";
            case 2: return "week";
            case 3: return "doing";
            case 4: return "done";
            default: return "todo";
        }
    }

    private String getRowId(int rowIndex) {
        switch (rowIndex) {
            case 1: return "rrhh";
            case 2: return "ventas";
            case 3: return "mktg";
            case 4: return "atc";
            default: return "rrhh";
        }
    }
}