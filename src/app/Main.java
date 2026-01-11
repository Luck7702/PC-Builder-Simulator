package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppController controller = new AppController();
        DashboardView root = new DashboardView(controller);

        Scene scene = new Scene(root, 1200, 760);

        
        try {
            String css = getClass().getResource("/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("Warning: style.css not found on classpath.");
        }

        primaryStage.setTitle("PC Builder Simulator      @2026 Dennis Christian Hansell");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
