package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.UserDAO;
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.User;
import mx.edu.uacm.is.slt.ds.multi_tareas117.util.SessionManager;

public class ProfileController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField fullNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button saveButton;
    @FXML
    private Label errorLabel;

    private UserDAO userDAO = new UserDAO();
    private String currentUsername;

    @FXML
    public void initialize() {
        // 1. Obtener el nombre de usuario de la sesión
        currentUsername = SessionManager.getLoggedInUsername();
        if (currentUsername == null) {
            showError("Error: No se pudo encontrar al usuario logueado.");
            saveButton.setDisable(true);
            return;
        }

        User user = userDAO.getUserByUsername(currentUsername);
        if (user != null) {
            usernameField.setText(currentUsername);
            fullNameField.setText(user.getName());
        } else {
            showError("Error: No se pudieron cargar los datos del perfil.");
            saveButton.setDisable(true);
        }
    }

    /**
     * Se llama al hacer clic en "Guardar Cambios".
     */
    @FXML
    void onSaveClick(ActionEvent event) {
        String fullName = fullNameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // --- Validaciones ---
        if (fullName.isEmpty()) {
            showError("El nombre completo no puede estar vacío.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden.");
            return;
        }

        // --- Guardar en la BD ---
        boolean success = userDAO.updateUserProfile(currentUsername, fullName, password);

        if (success) {
            showAlert("¡Perfil Actualizado!", "Tu información ha sido guardada correctamente.");
            // Limpiar campos de contraseña por seguridad
            passwordField.clear();
            confirmPasswordField.clear();
            errorLabel.setVisible(false);
        } else {
            showError("Hubo un error al guardar. Inténtalo de nuevo.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}