package app;

import classes.*; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.util.*;


public class ShopView extends BorderPane {

    private final AppController controller;

    private final ListView<Component> listView = new ListView<>();
    private final ObservableList<Component> currentItems =
            FXCollections.observableArrayList();

    private final ComboBox<String> brandFilter = new ComboBox<>();
    private final TextField textSearch = new TextField();

    private boolean priceAscending = true;
    private Button sortPriceBtn;

    public ShopView(AppController controller) {
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {

        setPadding(new Insets(8));

        Label header = new Label("Shop");
        header.setFont(Font.font(18));

        HBox filters = new HBox(8);
        filters.setPadding(new Insets(8));
        filters.setAlignment(Pos.CENTER_LEFT);

        brandFilter.setPromptText("Brand");
        brandFilter.setPrefWidth(160);

        textSearch.setPromptText("Search by name...");

        Button apply = new Button("Apply");
        apply.setOnAction(e -> applyFilters());

        sortPriceBtn = new Button("Price ↑");
        sortPriceBtn.setOnAction(e -> togglePriceSort());

        filters.getChildren().addAll(
                brandFilter,
                textSearch,
                apply,
                sortPriceBtn
        );

        setTop(new VBox(6, header, filters, new Separator()));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        String[] cats = {"CPU","GPU","RAM","MOBO","STORAGE","PSU"};
        for (String cat : cats) {
            Tab t = new Tab(cat);
            t.setOnSelectionChanged(ev -> {
                if (t.isSelected()) loadCategory(cat);
            });
            tabPane.getTabs().add(t);
        }
        tabPane.getSelectionModel().select(0);

        VBox left = new VBox(tabPane, listView);
        left.setSpacing(6);
        left.setPadding(new Insets(6));

        listView.setItems(currentItems);

        listView.setCellFactory(lv -> new ListCell() {

            private final HBox root = new HBox(10);
            private final VBox meta = new VBox(4);
            private final Label title = new Label();
            private final Label price = new Label();

            {
                title.setFont(Font.font(14));
                price.setStyle("-fx-font-weight:700;");

                meta.getChildren().addAll(title);

                Pane s = new Pane();
                HBox.setHgrow(s, Priority.ALWAYS);

                root.setAlignment(Pos.CENTER_LEFT);
                root.getChildren().addAll(meta, s, price);
                root.setPadding(new Insets(8));
            }

            @Override
            protected void updateItem(Object obj, boolean empty) {
                super.updateItem(obj, empty);

                if (empty || obj == null) {
                    setGraphic(null);
                    return;
                }

                Component c = (Component) obj;
                title.setText(c.getBrand() + " " + c.getName());
                price.setText("$" + String.format("%.2f", c.getPrice()));
                setGraphic(root);
            }
        });

       

        VBox detail = new VBox(10);
        detail.setPadding(new Insets(16));
        detail.setPrefWidth(380);

        Label detailTitle = new Label("Component Details");
        detailTitle.setFont(Font.font(16));

        ImageView large = new ImageView();
        large.setFitWidth(260);
        large.setPreserveRatio(true);

        Label name = new Label("Select an item");
        Label priceLabel = new Label();
        Label specLabel = new Label();
        specLabel.setWrapText(true);

        Button buyBtn = new Button("Buy");
        buyBtn.setDisable(true);

        detail.getChildren().addAll(
                detailTitle,
                large,
                name,
                priceLabel,
                specLabel,
                buyBtn
        );

        SplitPane split = new SplitPane(left, detail);
        split.setDividerPositions(0.62);
        setCenter(split);

        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, o, n) -> {

            buyBtn.setText("Buy");

            if (n == null) {
                buyBtn.setDisable(true);
                name.setText("Select an item");
                priceLabel.setText("");
                specLabel.setText("");
                large.setImage(null);
                return;
            }

            Component c = (Component) n;

            buyBtn.setDisable(false);
            name.setText(c.getBrand() + " " + c.getName());
            priceLabel.setText("$" + String.format("%.2f", c.getPrice()));
            specLabel.setText(buildDetailText(c));
            large.setImage(loadImage(c));
        });

        buyBtn.setOnAction(e -> {

            Object o = listView.getSelectionModel().getSelectedItem();
            if (o == null) return;

            Component c = (Component) o;

            String res = controller.buyComponent(c);
            buyBtn.setText(res);

            if ("Purchased".equals(res)) {
                buyBtn.setDisable(true);
            }
        });

        loadCategory("CPU");
    }

   

    private void loadCategory(String cat) {

        List<Component> items =
                controller.getShopByCategory(cat);

        currentItems.setAll(items);

        applyPriceSort(currentItems);   
        listView.setItems(currentItems);

        priceAscending = true;
        sortPriceBtn.setText("Price ↑");

        brandFilter.getItems().clear();
        brandFilter.getItems().add("All");

        for (Component c : items) {
            if (!brandFilter.getItems().contains(c.getBrand()))
                brandFilter.getItems().add(c.getBrand());
        }

        brandFilter.setValue("All");
        textSearch.setText("");
    }

    private void applyFilters() {

        String brand = brandFilter.getValue();
        String term = textSearch.getText().toLowerCase();

        List<Component> filtered = new ArrayList<>();

        for (Component c : currentItems) {

            boolean okBrand =
                    brand == null ||
                    "All".equals(brand) ||
                    c.getBrand().equals(brand);

            boolean okText =
                    term.isEmpty() ||
                    c.getName().toLowerCase().contains(term) ||
                    c.getBrand().toLowerCase().contains(term);

            if (okBrand && okText)
                filtered.add(c);
        }

        applyPriceSort(filtered);
        listView.setItems(
                FXCollections.observableArrayList(filtered)
        );
    }

    private void togglePriceSort() {

        priceAscending = !priceAscending;
        sortPriceBtn.setText(
                priceAscending ? "Price ↑" : "Price ↓"
        );

        List<Component> items =
                (List<Component>) listView.getItems();

        applyPriceSort(items);
        listView.setItems(
                FXCollections.observableArrayList(items)
        );
    }

    private void applyPriceSort(List<Component> list) {

        Collections.sort(list,
                priceAscending
                        ? Comparator.comparingDouble(Component::getPrice)
                        : Comparator.comparingDouble(Component::getPrice).reversed()
        );
    }

   

   

private Image loadImage(Component c) {
    
    if (c instanceof CPU) {
     
        String brand = c.getBrand().toLowerCase();
        if (brand.contains("intel")) return load("/images/intel.png");
        if (brand.contains("amd")) return load("/images/amd.png");
        return load("/images/cpu_generic.png");
    }

    
    if (c instanceof GPU)         return load("/images/gpu.png");
    if (c instanceof Motherboard) return load("/images/motherboard.png");
    if (c instanceof RAM)         return load("/images/ram.png");
    if (c instanceof PowerSupply)         return load("/images/psu.png");
    if (c instanceof Storage)     return load("/images/storage.png");
 System.out.println(c instanceof GPU);
    
    return load("/images/placeholder.png");

   
}

    private Image load(String path) {
        try {
            return new Image(
                    getClass().getResourceAsStream(path)
            );
        } catch (Exception e) {
            return null;
        }
    }

   

    private String buildDetailText(Component c) {
    if (c == null) return "Select a component";
    return c.getFormattedDetails(); 
}
}
