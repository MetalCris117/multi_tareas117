package mx.edu.uacm.is.slt.ds.multi_tareas117.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private GridPane kanbanGrid;

    private Node draggedCard;

    @FXML
    public void initialize() {
        for (Node node : kanbanGrid.getChildren()) {

            if (node instanceof VBox) {
                VBox cell = (VBox) node;

                setupDropTarget(cell);

                for (Node card : cell.getChildren()) {
                    if (card.getStyleClass().contains("kanban-card")) {
                        setupDraggable(card);
                    }
                }
            }
        }
    }

    /**
     * Configura un nodo (una tarjeta) para que se pueda arrastrar.
     */
    private void setupDraggable(Node card) {
        card.setOnDragDetected(event -> {
            Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            content.putString("kanban-card");
            db.setContent(content);

            draggedCard = card;
            event.consume();
        });
    }

    /**
     * Configura una celda (VBox) para que acepte tarjetas.
     */
    private void setupDropTarget(VBox cell) {

        // Evento 1: Cuando algo se arrastra SOBRE la celda
        cell.setOnDragOver(event -> {
            // Aceptamos el evento si viene de una tarjeta ("kanban-card")
            if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                if (event.getDragboard().getString().equals("kanban-card")) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
            event.consume();
        });

        cell.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString() && draggedCard != null) {

                VBox oldParent = (VBox) draggedCard.getParent();

                oldParent.getChildren().remove(draggedCard);

                cell.getChildren().add(draggedCard);

                success = true;
            }

            event.setDropCompleted(success);
            draggedCard = null;
            event.consume();
        });
    }
}
