package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class DashboardView extends BorderPane {

    private final AppController controller;

    private final Label sectionTitle = new Label();
    private final Label activePcLabel = new Label();
    private final Button saveBtn = new Button("Save");
    private final Button loadBtn = new Button("Load");

    
    private final Label budgetLabel = new Label();

    private final StackPane contentArea = new StackPane();

    
    private final List<Button> navButtons = new ArrayList<>();

    public DashboardView(AppController controller) {
        this.controller = controller;
        buildUI();

        
        setSection("Shop", new ShopView(controller));

        
        controller.getActiveBuildIndexProperty()
                .addListener((obs, o, n) -> updateActivePcLabel());

        
        controller.getBuildsVersionProperty()
                .addListener((obs, o, n) -> {
                    updateActivePcLabel();
                    updateBudget(); 
                });

        updateActivePcLabel();
        updateBudget(); 
    }

    private void buildUI() {
        
        VBox sidebar = new VBox(16);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(20));

        Label brand = new Label("PC Builder");
        brand.getStyleClass().add("brand");
        brand.setFont(Font.font("System", FontWeight.BOLD, 18));

        Separator sep = new Separator();

        Button shopBtn = navButton("Shop", "🏬", () -> setSection("Shop", new ShopView(controller)));
        Button inventoryBtn = navButton("Inventory", "📦", () -> setSection("Inventory", new InventoryView(controller)));
        Button workbenchBtn = navButton("Workbench", "🛠", () -> setSection("Workbench", new WorkbenchView(controller)));
        Button benchmarkBtn = navButton("Benchmark", "⚡", () -> setSection("Benchmark", new BenchmarkView(controller)));

        sidebar.getChildren().addAll(sep, shopBtn, inventoryBtn, workbenchBtn, benchmarkBtn, saveBtn, loadBtn);
        sidebar.setPrefWidth(240);

        
        HBox header = new HBox(16);
        header.getStyleClass().add("app-header");
        header.setPadding(new Insets(14, 20, 14, 20));
        header.setAlignment(Pos.CENTER_LEFT);

        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setFont(Font.font("System", FontWeight.SEMI_BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        activePcLabel.getStyleClass().add("active-pc");

        
        header.getChildren().addAll(
                sectionTitle,
                spacer,
                budgetLabel,    
                activePcLabel
        );
        
        saveBtn.setOnAction(e -> {
            controller.saveGame();
            new Alert(Alert.AlertType.INFORMATION, "Data Saved Successfully!").show();
        });

        loadBtn.setOnAction(e -> {
            controller.loadGame();
            updateBudget(); 
            updateActivePcLabel();
            new Alert(Alert.AlertType.INFORMATION, "Data Loaded Successfully!").show();
        });

        saveBtn.getStyleClass().add("save-btn");
        loadBtn.getStyleClass().add("load-btn");

        
        contentArea.getStyleClass().add("content-area");

        setLeft(sidebar);
        setTop(header);
        setCenter(contentArea);
    }

    
    private Button navButton(String text, String icon, Runnable action) {
        Button btn = new Button(icon + "  " + text);
        btn.getStyleClass().add("nav-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            action.run();
        });
        navButtons.add(btn);
        return btn;
    }

    private void setSection(String title, Node content) {
        
        sectionTitle.setText(title);

        
        contentArea.getChildren().clear();
        VBox card = new VBox(content);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(14));
        contentArea.getChildren().add(card);

        
        markActiveButtonByLabel(title);
    }

    private void markActiveButtonByLabel(String label) {
        
        for (Button b : navButtons) {
            String txt = b.getText();
            boolean active = txt.trim().endsWith(label);
            if (active) {
                if (!b.getStyleClass().contains("nav-btn-active"))
                    b.getStyleClass().add("nav-btn-active");
            } else {
                b.getStyleClass().remove("nav-btn-active");
            }
        }
    }

    private void updateActivePcLabel() {
        int idx = controller.getActiveBuildIndex() + 1;
        var build = controller.getActiveBuild();
        if (build == null) {
            activePcLabel.setText("No active PC");
        } else {
            activePcLabel.setText("Active PC Profile: " + idx + " — " + build.getName());
        }
    }

    
    private void updateBudget() {
        budgetLabel.setText("Budget: $" + String.format("%.2f", controller.getBudget()));
    }
}
