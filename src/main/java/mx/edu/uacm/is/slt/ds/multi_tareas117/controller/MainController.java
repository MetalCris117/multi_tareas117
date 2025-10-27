package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private VBox sidebar;

    @FXML
    private Button sidebarToggleButton; // El botón de hamburguesa
    @FXML
    private Button dashboardButton;
    @FXML
    private Button usuariosButton;

    @FXML
    private Label sidebarMenuLabel;


    private boolean isSidebarCollapsed = false;
    private final double COLLAPSED_WIDTH = 70.0;
    private final double EXPANDED_WIDTH = 220.0;

    // --- 3. Lógica ---

    @FXML
    public void initialize() {

        sidebarToggleButton.setOnAction(e -> toggleSidebar());
    }

    /**
     * Contrae o expande el menú lateral (sidebar).
     */
    private void toggleSidebar() {
        if (isSidebarCollapsed) {
            expandSidebar();
        } else {
            collapseSidebar();
        }
        isSidebarCollapsed = !isSidebarCollapsed;
    }

    /**
     * Contrae el menú para mostrar solo iconos.
     */
    private void collapseSidebar() {

        sidebar.setPrefWidth(COLLAPSED_WIDTH);

        sidebarMenuLabel.setVisible(false);

        dashboardButton.setText(null);
        dashboardButton.setAlignment(Pos.CENTER);

        usuariosButton.setText(null);
        usuariosButton.setAlignment(Pos.CENTER);
    }

    /**
     * Expande el menú para mostrar texto e iconos.
     */
    private void expandSidebar() {

        sidebar.setPrefWidth(EXPANDED_WIDTH);

        sidebarMenuLabel.setVisible(true);

        dashboardButton.setText("Dashboard");
        dashboardButton.setAlignment(Pos.BASELINE_LEFT);

        usuariosButton.setText("Usuarios");
        usuariosButton.setAlignment(Pos.BASELINE_LEFT);
    }
}