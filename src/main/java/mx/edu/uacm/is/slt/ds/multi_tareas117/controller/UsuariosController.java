package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mx.edu.uacm.is.slt.ds.multi_tareas117.model.User;
import org.kordamp.ikonli.javafx.FontIcon;

public class UsuariosController {

    @FXML
    private ListView<User> userListView;

    @FXML
    private StackPane detailStackPane;

    @FXML
    private Label placeholderLabel;

    @FXML
    private VBox userDetailView;

    @FXML
    private Label selectedUserName;

    @FXML
    private Label selectedUserStatus;

    @FXML
    private FontIcon selectedUserAvatar;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Button sendMessageButton;


    private ObservableList<User> userList;
    private User currentlySelectedUser;

    @FXML
    public void initialize() {
        loadUsers();

        userListView.setCellFactory(param -> new ListCell<User>() {
            private final HBox graphic = new HBox();
            private final FontIcon icon = new FontIcon();
            private final Label name = new Label();

            {
                graphic.setSpacing(10);
                graphic.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                graphic.getChildren().addAll(icon, name);
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    name.setText(user.getName());
                    icon.setIconLiteral(user.getAvatar());

                    if (user.getStatus().equals("Online")) {
                        icon.getStyleClass().addAll("icon-success");
                    } else {
                        icon.getStyleClass().addAll("icon-muted");
                    }
                    setGraphic(graphic);
                }
            }
        });


        userListView.setItems(userList);

        userListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> displayUserDetails(newValue)
        );


        userDetailView.setVisible(false);
        placeholderLabel.setVisible(true);
    }

    /**
     * Muestra los detalles del usuario seleccionado
     */
    private void displayUserDetails(User user) {
        if (user == null) {
            userDetailView.setVisible(false);
            placeholderLabel.setVisible(true);
            currentlySelectedUser = null;
            return;
        }

        currentlySelectedUser = user;

        // Rellenar los campos
        selectedUserName.setText(user.getName());
        selectedUserStatus.setText(user.getStatus());
        selectedUserAvatar.setIconLiteral(user.getAvatar());

        selectedUserStatus.getStyleClass().removeAll("text-success", "text-muted");
        if (user.getStatus().equals("Online")) {
            selectedUserStatus.getStyleClass().add("text-success");
        } else {
            selectedUserStatus.getStyleClass().add("text-muted");
        }

        placeholderLabel.setVisible(false);
        userDetailView.setVisible(true);
    }

    /**
     * (Simulado) Carga la lista de usuarios.
     * En el futuro, esto vendría de la base de datos SQLite.
     */
    private void loadUsers() {
        userList = FXCollections.observableArrayList(
                new User("Cris", "Online", "fas-user-circle"),
                new User("Bryan", "Online", "fas-user-ninja"),
                new User("Francisco", "Offline", "fas-user-secret"),
                new User("Aramis", "Online", "fas-user-astronaut"),
                new User("El pollo", "Offline", "fas-user-md"),
                new User("Manuel Ignacio Castillo Lopez", "Offline", "fas-user-ninja")
        );
    }

    /**
     * Se llama cuando se presiona el botón "Enviar Mensaje".
     */
    @FXML
    private void onSendMessageClick() {
        String message = messageTextArea.getText();
        if (message.isEmpty() || currentlySelectedUser == null) {
            return; // No hacer nada si no hay mensaje o usuario
        }

        System.out.println("====== NUEVO MENSAJE ======");
        System.out.println("Para: " + currentlySelectedUser.getName());
        System.out.println("Mensaje: " + message);
        System.out.println("===========================");

        // Limpiar el área de texto
        messageTextArea.clear();

        // (Opcional) Mostrar una alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje Enviado");
        alert.setHeaderText(null);
        alert.setContentText("Tu mensaje para " + currentlySelectedUser.getName() + " ha sido enviado.");
        alert.showAndWait();
    }
}