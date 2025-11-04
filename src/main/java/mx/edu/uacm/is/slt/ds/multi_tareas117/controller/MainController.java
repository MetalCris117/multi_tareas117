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

    // Botones
    @FXML
    private Button sidebarToggleButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button usuariosButton;

    @FXML
    private Label sidebarMenuLabel;



    private boolean isSidebarCollapsed = false;
    private final double COLLAPSED_WIDTH = 70.0;
    private final double EXPANDED_WIDTH = 220.0;



    @FXML
    public void initialize() {

        sidebarToggleButton.setOnAction(e -> toggleSidebar());

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
    }

    private void expandSidebar() {
        sidebar.setPrefWidth(EXPANDED_WIDTH);
        sidebarMenuLabel.setVisible(true);

        dashboardButton.setText("Dashboard");
        dashboardButton.setAlignment(Pos.BASELINE_LEFT);

        usuariosButton.setText("Usuarios");
        usuariosButton.setAlignment(Pos.BASELINE_LEFT);
    }


    /**
     * Carga la vista de Dashboard cuando se hace clic en el botón.
     */
    @FXML
    private void onDashboardClick() {
        System.out.println("Cargando vista: Dashboard");
        loadView("dashboard-view.fxml");
    }

    /**
     * Carga la vista de Usuarios cuando se hace clic en el botón.
     */
    @FXML
    private void onUsuariosClick() {
        System.out.println("Cargando vista: Usuarios");
        loadView("usuarios-view.fxml");
    }

    /**
     * Método reutilizable para cargar un FXML en el AnchorPane central.
     * @param fxmlFileName El nombre del archivo (ej. "dashboard-view.fxml")
     */
    private void loadView(String fxmlFileName) {
        try {

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/pages/" + fxmlFileName));
            Node view = loader.load();

            mainContentArea.getChildren().setAll(view);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}