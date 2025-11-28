package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.TaskDAO;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.UserDAO;
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.Task; // Importar modelo Task
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.User;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class NewTaskController {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> columnComboBox;
    @FXML private ComboBox<String> rowComboBox;
    @FXML private ComboBox<String> userComboBox;
    @FXML private Button createTaskButton; // Usaremos este botón para ambas cosas
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;
    @FXML private DatePicker dueDatePicker;
    @FXML private Label titleLabel; // (Opcional) El Label que dice "Crear Nueva Tarea"

    private UserDAO userDAO = new UserDAO();
    private TaskDAO taskDAO = new TaskDAO();

    private boolean taskCreated = false;
    private Task taskToEdit = null; // Variable para guardar la tarea si estamos editando

    @FXML
    public void initialize() {
        columnComboBox.setItems(FXCollections.observableArrayList("Por hacer", "Esta semana", "Haciendo", "Hecho"));
        rowComboBox.setItems(FXCollections.observableArrayList("Recursos Humanos", "Ventas", "Marketing", "Atención al cliente"));

        userComboBox.setItems(FXCollections.observableArrayList(
                userDAO.getAllUsers().stream().map(User::getName).collect(Collectors.toList())
        ));

        columnComboBox.getSelectionModel().selectFirst();
        rowComboBox.getSelectionModel().selectFirst();
        userComboBox.getSelectionModel().selectFirst();
    }

    /**
     * ¡MÉTODO NUEVO! Se llama desde el Dashboard para activar el "Modo Edición".
     */
    public void setTaskToEdit(Task task) {
        this.taskToEdit = task;

        createTaskButton.setText("Guardar Cambios");

        titleField.setText(task.getTitle());

        columnComboBox.setValue(getColumnText(task.getStatusColumn()));
        rowComboBox.setValue(getRowText(task.getCategoryRow()));
        userComboBox.setValue(task.getAssignedUser());

        dueDatePicker.setValue(task.getDueDate().toLocalDate());
    }

    @FXML
    void onCreateTaskClick(ActionEvent event) {
        String title = titleField.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty()) { showError("El título es obligatorio."); return; }
        if (dueDate == null) { showError("Debes seleccionar una fecha."); return; }

        String column = convertColumnToId(columnComboBox.getValue());
        String row = convertRowToId(rowComboBox.getValue());
        String user = userComboBox.getValue();

        boolean success;

        if (taskToEdit == null) {
            success = taskDAO.createTask(title, column, row, user, dueDate);
        } else {
            success = taskDAO.updateTask(taskToEdit.getId(), title, column, row, user, dueDate);
        }

        if (success) {
            taskCreated = true;
            closeWindow();
        } else {
            showError("Error al guardar en la base de datos.");
        }
    }

    public boolean isTaskCreated() { return taskCreated; }
    @FXML void onCancelClick(ActionEvent event) { closeWindow(); }
    private void showError(String message) { errorLabel.setText(message); errorLabel.setVisible(true); }
    private void closeWindow() { Stage stage = (Stage) cancelButton.getScene().getWindow(); stage.close(); }

    private String convertColumnToId(String column) {
        switch (column) { case "Por hacer": return "todo"; case "Esta semana": return "week"; case "Haciendo": return "doing"; case "Hecho": return "done"; default: return "todo"; }
    }
    private String convertRowToId(String row) {
        switch (row) { case "Recursos Humanos": return "rrhh"; case "Ventas": return "ventas"; case "Marketing": return "mktg"; case "Atención al cliente": return "atc"; default: return "rrhh"; }
    }

    private String getColumnText(String id) {
        switch (id) { case "todo": return "Por hacer"; case "week": return "Esta semana"; case "doing": return "Haciendo"; case "done": return "Hecho"; default: return "Por hacer"; }
    }
    private String getRowText(String id) {
        switch (id) { case "rrhh": return "Recursos Humanos"; case "ventas": return "Ventas"; case "mktg": return "Marketing"; case "atc": return "Atención al cliente"; default: return "Recursos Humanos"; }
    }
}
