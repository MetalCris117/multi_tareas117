package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.edu.uacm.is.slt.ds.multi_tareas117.HelloApplication;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    /**
     * Este método se llama cuando se presiona el botón "Entrar".
     */
    @FXML
    void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if (username.equals("admin") && password.equals("1234")) {

            errorLabel.setVisible(false);
            openDashboard();
        } else {
            errorLabel.setText("Usuario o contraseña incorrectos.");
            errorLabel.setVisible(true);
        }
    }

    /**
     * Carga el FXML del dashboard en una nueva ventana.
     */
    private void openDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/main-layout.fxml"));
            Parent root = fxmlLoader.load();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Mi Dashboard");
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