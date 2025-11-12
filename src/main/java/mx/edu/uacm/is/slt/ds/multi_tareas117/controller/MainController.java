package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import mx.edu.uacm.is.slt.ds.multi_tareas117.Main;

import java.io.IOException;

public class MainController {

    @FXML
    private VBox sidebar;
    @FXML
    private AnchorPane mainContentArea;
    @FXML
    private Button sidebarToggleButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button usuariosButton;
    @FXML
    private Button profileButton;
    @FXML
    private Label sidebarMenuLabel;

    // --- Variables de estado ---
    private boolean isSidebarCollapsed = false;
    private final double COLLAPSED_WIDTH = 70.0;
    private final double EXPANDED_WIDTH = 220.0;

    @FXML
    public void initialize() {
        sidebarToggleButton.setOnAction(e -> toggleSidebar());
        // Cargar la vista por defecto (Dashboard)
        loadView("dashboard-view.fxml");
    }

    private void toggleSidebar() {
        if (isSidebarCollapsed) {
            expandSidebar();
        } else {
            collapseSidebar();
        }
        isSidebarCollapsed = !isSidebarCollapsed;
    }

    private void collapseSidebar() {
        sidebar.setPrefWidth(COLLAPSED_WIDTH);
        sidebarMenuLabel.setVisible(false);
        dashboardButton.setText(null);
        dashboardButton.setAlignment(Pos.CENTER);
        usuariosButton.setText(null);
        usuariosButton.setAlignment(Pos.CENTER);
        profileButton.setText(null);
        profileButton.setAlignment(Pos.CENTER);
    }

    private void expandSidebar() {
        sidebar.setPrefWidth(EXPANDED_WIDTH);
        sidebarMenuLabel.setVisible(true);
        dashboardButton.setText("Dashboard");
        dashboardButton.setAlignment(Pos.BASELINE_LEFT);
        usuariosButton.setText("Usuarios");
        usuariosButton.setAlignment(Pos.BASELINE_LEFT);
        profileButton.setText("Mi Perfil");
        profileButton.setAlignment(Pos.BASELINE_LEFT);
    }

    @FXML
    private void onDashboardClick() {
        loadView("dashboard-view.fxml");
    }

    @FXML
    private void onUsuariosClick() {
        loadView("usuarios-view.fxml");
    }

    @FXML
    private void onProfileClick() {
        loadView("profile-view.fxml");
    }

    /**
     * Método reutilizable para cargar un FXML en el AnchorPane central.
     * (¡ARREGLADO CON LA RUTA ABSOLUTA!)
     */
    private void loadView(String fxmlFileName) {
        try {
            String fullPath = "/mx/edu/uacm/is/slt/ds/multi_tareas117/views/pages/" + fxmlFileName;
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fullPath));
            Node view = loader.load();
            mainContentArea.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + fxmlFileName);
            e.printStackTrace();
        }
    }
}