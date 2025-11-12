package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.UserDAO;
import org.kordamp.ikonli.javafx.FontIcon;

public class RegisterController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private Button togglePasswordButton;
    @FXML
    private Button toggleConfirmPasswordButton;
    @FXML
    private FontIcon passwordIcon;
    @FXML
    private FontIcon confirmPasswordIcon;

    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;
    @FXML
    private Label errorLabel;

    private UserDAO userDAO = new UserDAO();
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    /**
     * ¡NUEVO! Se llama automáticamente al cargar el FXML.
     */
    @FXML
    public void initialize() {
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        confirmPasswordTextField.textProperty().bindBidirectional(confirmPasswordField.textProperty());

        togglePasswordButton.setOnAction(e -> {
            isPasswordVisible = toggleVisibility(passwordField, passwordTextField, passwordIcon, isPasswordVisible);
        });

        toggleConfirmPasswordButton.setOnAction(e -> {
            isConfirmPasswordVisible = toggleVisibility(confirmPasswordField, confirmPasswordTextField, confirmPasswordIcon, isConfirmPasswordVisible);
        });
    }

    /**
     * Se llama al presionar el botón "Crear Cuenta".
     */
    @FXML
    void onRegisterButtonClick(ActionEvent event) {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();

        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("Todos los campos son obligatorios.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden.");
            return;
        }

        boolean success = userDAO.registerUser(username, password, fullName, "fas-user-circle");

        if (success) {
            showAlert("¡Registro Exitoso!", "El usuario " + username + " ha sido creado. Ya puedes iniciar sesión.");
            closeWindow();
        } else {
            showError("El nombre de usuario '" + username + "' ya está en uso. Por favor, elige otro.");
        }
    }

    /**
     * ¡NUEVO! Lógica para mostrar/ocultar la contraseña.
     */
    private boolean toggleVisibility(PasswordField passField, TextField textField, FontIcon icon, boolean isVisible) {
        if (!isVisible) {
            // MOSTRAR (Ojo Abierto)
            passField.setVisible(false);
            passField.setManaged(false);
            textField.setVisible(true);
            textField.setManaged(true);
            icon.setIconLiteral("fas-eye-slash");
            return true;
        } else {
            textField.setVisible(false);
            textField.setManaged(false);
            passField.setVisible(true);
            passField.setManaged(true);
            icon.setIconLiteral("fas-eye");
            return false;
        }
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        closeWindow();
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

    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}