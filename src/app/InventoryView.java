package app;

import classes.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class InventoryView extends BorderPane {

    private final AppController controller;

    private final ListView<Component> listView = new ListView<>();

    private final Label detailLabel = new Label("Select a component");
    private final Button installBtn = new Button("Install to Active PC");

    public InventoryView(AppController controller) {
        this.controller = controller;
        buildUI();
        refresh();

        controller.getBuildsVersionProperty()
                .addListener((o, a, b) -> refresh());
    }

    private void buildUI() {

        setPadding(new Insets(12));

        Label title = new Label("Inventory");
        title.setFont(Font.font(20));

        setTop(new VBox(8, title, new Separator()));

       
        listView.setCellFactory(lv -> new ListCell<>() {

            private final HBox root = new HBox(10);
            private final VBox meta = new VBox(4);
            private final Label name = new Label();
            private final Label type = new Label();

            {
                name.setStyle("-fx-font-weight:600;");
                type.setStyle("-fx-font-size:11px;-fx-text-fill:#6b7280;");
                meta.getChildren().addAll(name, type);

                Pane s = new Pane();
                HBox.setHgrow(s, Priority.ALWAYS);

                root.getChildren().addAll(meta, s);
                root.setPadding(new Insets(8));
            }

            @Override
            protected void updateItem(Component c, boolean empty) {
                super.updateItem(c, empty);

                if (empty || c == null) {
                    setGraphic(null);
                    return;
                }

                name.setText(c.getBrand() + " " + c.getName());
                type.setText(c.getType());
                setGraphic(root);
            }
        });

        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, o, c) -> updateDetails(c));

       
        VBox right = new VBox(
                10,
                new Label("Details"),
                new Separator(),
                detailLabel,
                new Separator(),
                installBtn
        );

        right.setPadding(new Insets(14));
        right.setPrefWidth(420);
        right.getStyleClass().add("card");

        installBtn.setDisable(true);
        installBtn.setOnAction(e -> installSelected());

        SplitPane split = new SplitPane(listView, right);
        split.setDividerPositions(0.45);
        setCenter(split);
    }

    private void refresh() {
        listView.setItems(
                FXCollections.observableArrayList(
                        controller.getInventory().getParts()
                )
        );

        if (!listView.getItems().isEmpty()) {
            listView.getSelectionModel().select(0);
        }
    }

    private void updateDetails(Component c) {
        if (c == null) {
            detailLabel.setText("Select a component");
            installBtn.setDisable(true);
            return;
        }

        detailLabel.setText(c.getFormattedDetails());
        installBtn.setDisable(false);
    }

    private void installSelected() {

        int idx = listView.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;

        String res = controller.installFromInventory(idx);

        if (!"Installed".equalsIgnoreCase(res)
                && !res.startsWith("Installed")) {

            new Alert(Alert.AlertType.ERROR, res).show();
        }

        refresh();
    }
}
