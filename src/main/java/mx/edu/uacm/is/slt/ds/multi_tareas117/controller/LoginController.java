package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.edu.uacm.is.slt.ds.multi_tareas117.DAO.UserDAO;
import mx.edu.uacm.is.slt.ds.multi_tareas117.Main;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import atlantafx.base.theme.PrimerDark;
import mx.edu.uacm.is.slt.ds.multi_tareas117.util.SessionManager;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Button registerLinkButton;

    private UserDAO userDAO = new UserDAO();

    /**
     * Este método se llama cuando se presiona el botón "Entrar".
     */
    @FXML
    void onLoginButtonClick(ActionEvent event) {
        validateAndLogin();
    }
    /**
     * Este método se llama cuando se presiona Enter en passwordField.
     */
    @FXML
    void onLoginEnterClick(ActionEvent event) throws IOException {
        validateAndLogin();
    }
    /**
     * Lógica de validación
     */
    private void validateAndLogin() {
        userDAO.createTestUser();

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userDAO.validateLogin(username, password)) {
            SessionManager.login(username);
            errorLabel.setVisible(false);
            openDashboard();
        } else {
            errorLabel.setText("Usuario o contraseña incorrectos.");
            errorLabel.setVisible(true);
        }
    }

    /**
     * ¡NUEVO! Se llama al hacer clic en "¿No tienes cuenta? Regístrate"
     */
    @FXML
    void onRegisterLinkClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/mx/edu/uacm/is/slt/ds/multi_tareas117/views/login/register-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene registerScene = new Scene(root);
            registerScene.getStylesheets().add(new PrimerDark().getUserAgentStylesheet());
            Stage registerStage = new Stage();
            registerStage.setTitle("Crear Nueva Cuenta");
            registerStage.setScene(registerScene);
            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.initOwner((Stage) loginButton.getScene().getWindow());
            registerStage.setResizable(false);

            registerStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error al abrir el formulario de registro.");
            errorLabel.setVisible(true);
        }
    }
    /**
     * Carga el FXML del dashboard en una nueva ventana.
     */
    private void openDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/layout/main-layout.fxml"));

            Parent root = fxmlLoader.load();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Dashboard");
            dashboardStage.setScene(new Scene(root, 1200, 800));
            dashboardStage.show();

            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error al cargar el dashboard.");
            errorLabel.setVisible(true);
        }
    }
}