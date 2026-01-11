package app;

import classes.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;

public class WorkbenchView extends BorderPane {

    private final AppController controller;

    
    private final ListView<Component> buildList = new ListView<>();

    private final Label detailLabel = new Label("Select a component");
    private final Label totalCostLabel = new Label("Total PC Cost: $0.00");
    
    
    private final Label powerLabel = new Label("Power Usage: 0W / 0W");

    private final Button removeBtn = new Button("Remove Selected");
    private final Button clearBtn = new Button("Clear Build");

    private final Label pcLabel = new Label();

    public WorkbenchView(AppController controller) {
        this.controller = controller;
        buildUI();
        refresh();

        controller.getActiveBuildIndexProperty()
                .addListener((o, oldV, newV) -> refresh());

        controller.getBuildsVersionProperty()
                .addListener((o, oldV, newV) -> refresh());
    }

    private void buildUI() {
        setPadding(new Insets(12));

        Label title = new Label("Workbench");
        title.setFont(Font.font(20));

        Button prevBtn = new Button("◀");
        Button nextBtn = new Button("▶");
        Button renameBtn = new Button("Rename");
        Button newPcBtn = new Button("+ New PC");
        Button sellBtn = new Button("Sell PC");

        prevBtn.setOnAction(e -> goPrevPc());
        nextBtn.setOnAction(e -> controller.nextBuild());
        renameBtn.setOnAction(e -> renamePc());
        newPcBtn.setOnAction(e -> createPc());
        sellBtn.setOnAction(e -> {
            double earned = controller.sellActiveBuild();
            if (earned == -2) {
                new Alert(Alert.AlertType.ERROR, "PC is unfinished!").show();
                return;
            }
            if (earned < 0) return;
            new Alert(Alert.AlertType.INFORMATION, "PC sold for $" + String.format("%.2f", earned)).show();
        });

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox pcBar = new HBox(10, prevBtn, pcLabel, nextBtn, spacer, renameBtn, newPcBtn, sellBtn);

        VBox top = new VBox(8, title, pcBar, new Separator());
        setTop(top);

       
        buildList.setPrefWidth(360);
        buildList.setCellFactory(lv -> new ListCell<Component>() {
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

        buildList.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, c) -> updateDetails(c));

       
        
        VBox rightPane = new VBox(
                10,
                new Label("Details"),
                new Separator(),
                detailLabel,
                new Separator(),
                totalCostLabel,
                powerLabel,      
                new Separator(),
                removeBtn,
                clearBtn
        );

        rightPane.setPadding(new Insets(14));
        rightPane.setPrefWidth(420);
        rightPane.getStyleClass().add("card");

        removeBtn.setOnAction(e -> removeSelected());
        clearBtn.setOnAction(e -> controller.clearBuildAndReturnToInventory());

        SplitPane split = new SplitPane(buildList, rightPane);
        split.setDividerPositions(0.45);
        setCenter(split);
    }

    private void refresh() {
        PCBuild build = controller.getActiveBuild();

        if (build == null) {
            pcLabel.setText("No PCs");
            buildList.setItems(FXCollections.observableArrayList());
            detailLabel.setText("No active PC");
            totalCostLabel.setText("Total PC Cost: $0.00");
            powerLabel.setText("Power Usage: 0W / 0W");
            return;
        }

        pcLabel.setText(build.getName());
        buildList.setItems(FXCollections.observableArrayList(build.getInstalledComponents()));

        
        totalCostLabel.setText(String.format("Total PC Cost: $%.2f", build.getTotalCost()));

       
        int totalDraw = build.getTotalPower();
        int psuCapacity = 0;

        
        for (Component c : build.getInstalledComponents()) {
            if (c instanceof PowerSupply psu) {
                psuCapacity = psu.getWattage();
                break; 
            }
        }

        powerLabel.setText(String.format("Power Usage: %dW / %dW", totalDraw, psuCapacity));

        
        if (psuCapacity > 0 && totalDraw > psuCapacity) {
            powerLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else if (psuCapacity > 0) {
            powerLabel.setStyle("-fx-text-fill: green;");
        } else {
            powerLabel.setStyle("-fx-text-fill: gray;");
        }

        if (!build.getInstalledComponents().isEmpty()) {
            if (buildList.getSelectionModel().getSelectedIndex() < 0) {
                buildList.getSelectionModel().select(0);
            }
        } else {
            detailLabel.setText("No components installed");
        }
    }

    private void updateDetails(Component c) {
        if (c == null) {
            detailLabel.setText("Select a component");
            return;
        }
        detailLabel.setText(c.getFormattedDetails());
    }

    private void removeSelected() {
        int idx = buildList.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            controller.removeFromBuild(idx);
        }
    }

    private void createPc() {
        TextInputDialog d = new TextInputDialog("New PC");
        d.setHeaderText("Create New PC");
        d.setContentText("PC name:");
        d.showAndWait().ifPresent(controller::addNewBuild);
    }

    private void renamePc() {
        PCBuild build = controller.getActiveBuild();
        if (build == null) return;

        TextInputDialog d = new TextInputDialog(build.getName());
        d.setHeaderText("Rename PC");
        d.setContentText("New name:");
        d.showAndWait().ifPresent(build::setName);
    }

    private void goPrevPc() {
        int count = controller.getBuilds().size();
        if (count == 0) return;

        int cur = controller.getActiveBuildIndex();
        int prev = cur - 1;
        if (prev < 0) prev = count - 1;

        controller.setActiveBuild(prev);
    }
}