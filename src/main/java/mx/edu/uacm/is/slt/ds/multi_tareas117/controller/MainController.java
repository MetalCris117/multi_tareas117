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

    // --- Inyección de Componentes ---

    // Contenedores
    @FXML
    private VBox sidebar;

    // ¡NUEVO! El contenedor central
    @FXML
    private AnchorPane mainContentArea;

    // Botones
    @FXML
    private Button sidebarToggleButton;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button usuariosButton;

    // Etiquetas
    @FXML
    private Label sidebarMenuLabel;

    // --- Variables de estado ---

    private boolean isSidebarCollapsed = false;
    private final double COLLAPSED_WIDTH = 70.0;
    private final double EXPANDED_WIDTH = 220.0;

    // --- Lógica de Inicialización ---

    @FXML
    public void initialize() {
        // 1. Asignamos el evento al botón de hamburguesa
        sidebarToggleButton.setOnAction(e -> toggleSidebar());

        // ¡NUEVO! 2. Cargamos la vista por defecto (el dashboard)
        loadView("dashboard-view.fxml");
    }

    // --- Lógica del Menú Lateral (Sidebar) ---

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


    // --- ¡NUEVO! Lógica de Navegación ---

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
            // Carga el FXML de la vista
            // Usamos HelloApplication.class para obtener la ruta base correcta
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/pages/" + fxmlFileName));
            Node view = loader.load();

            // Limpia el contenido anterior y añade la nueva vista
            mainContentArea.getChildren().setAll(view);

            // Ajusta la vista para que llene el AnchorPane
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            // Opcional: mostrar un diálogo de error
        }
    }
}