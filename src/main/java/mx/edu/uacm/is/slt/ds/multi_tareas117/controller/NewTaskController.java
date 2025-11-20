package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.TaskDAO;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.UserDAO;
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.User;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class NewTaskController {

    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<String> columnComboBox;
    @FXML
    private ComboBox<String> rowComboBox;
    @FXML
    private ComboBox<String> userComboBox;
    @FXML
    private Button createTaskButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label errorLabel;
    @FXML
    private DatePicker dueDatePicker;

    private UserDAO userDAO = new UserDAO();
    private TaskDAO taskDAO = new TaskDAO();

    private boolean taskCreated = false; // Bandera para el Dashboard

    @FXML
    public void initialize() {
        columnComboBox.setItems(FXCollections.observableArrayList(
                "Por hacer", "Esta semana", "Haciendo", "Hecho"
        ));
        rowComboBox.setItems(FXCollections.observableArrayList(
                "Recursos Humanos", "Ventas", "Marketing", "Atención al cliente"
        ));
        userComboBox.setItems(FXCollections.observableArrayList(
                userDAO.getAllUsers().stream()
                        .map(User::getName) // Convierte List<User> a List<String>
                        .collect(Collectors.toList())
        ));
        columnComboBox.getSelectionModel().selectFirst();
        rowComboBox.getSelectionModel().selectFirst();
        userComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    void onCreateTaskClick(ActionEvent event) {
        String title = titleField.getText();
        LocalDate dueDate = dueDatePicker.getValue();
        if (title.isEmpty()) {
            showError("El título es obligatorio.");
            return;
        }
        if (dueDate == null) {
            showError("Debes seleccionar una fecha límite.");
            return;
        }

        String column = convertColumnToId(columnComboBox.getValue());
        String row = convertRowToId(rowComboBox.getValue());

        boolean success = taskDAO.createTask(title, column, row, userComboBox.getValue(), dueDate);

        if (success) {
            taskCreated = true;
            closeWindow();
        } else {
            showError("Error al guardar la tarea.");
        }
    }
    public boolean isTaskCreated() {
        return taskCreated;
    }

    @FXML
    void onCancelClick(ActionEvent event) {
        closeWindow();
    }

    private String convertColumnToId(String column) {
        switch (column) {
            case "Por hacer": return "todo";
            case "Esta semana": return "week";
            case "Haciendo": return "doing";
            case "Hecho": return "done";
            default: return "todo";
        }
    }

    private String convertRowToId(String row) {
        switch (row) {
            case "Recursos Humanos": return "rrhh";
            case "Ventas": return "ventas";
            case "Marketing": return "mktg";
            case "Atención al cliente": return "atc";
            default: return "rrhh";
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
