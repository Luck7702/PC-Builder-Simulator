package app;

import classes.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;

public class BenchmarkView extends BorderPane {

    private final AppController controller;

    private final ProgressBar progressBar = new ProgressBar(0);
    private final Label statusLabel = new Label("Ready to benchmark");
    private final TextArea resultArea = new TextArea();
    private final Button runButton = new Button("Run Benchmark");

    public BenchmarkView(AppController controller) {
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {

        Label title = new Label("System Benchmark");
        title.setFont(Font.font(18));

        setTop(new VBox(8, title, new Separator()));

        VBox center = new VBox(10);
        center.setPadding(new Insets(15));

        progressBar.setPrefWidth(400);
        resultArea.setEditable(false);
        resultArea.setPrefHeight(300);

        center.getChildren().addAll(
                statusLabel,
                progressBar,
                runButton,
                resultArea
        );

        setCenter(center);

        runButton.setOnAction(e -> startBenchmark());
    }

   

    private void startBenchmark() {

        
        BenchmarkService.Result validation = controller.runBenchmark();
        if (validation == null) {
            alert(
                "Benchmark cannot start.\n\n" +
                "Your PC build is INCOMPLETE or INVALID.\n\n" +
                "Required:\n" +
                "- Motherboard\n" +
                "- CPU\n" +
                "- RAM\n" +
                "- Power Supply\n\n" +
                "PSU must also provide sufficient wattage."
            );
            return; 
        }

        

        List<Component> installed = controller.getInstalledComponents();

        runButton.setDisable(true);
        resultArea.clear();
        statusLabel.setText("Starting benchmark...");
        progressBar.setProgress(0);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {

                simulatePhase("CPU Benchmark", 0.25);
                simulatePhase("GPU Benchmark", 0.50);
                simulatePhase("Memory Benchmark", 0.75);
                simulatePhase("Storage Benchmark", 1.00);

                Platform.runLater(() -> showResults(installed));

                return null;
            }
        };

        new Thread(task).start();
    }

    private void simulatePhase(String label, double progress) throws InterruptedException {
        Platform.runLater(() -> statusLabel.setText(label));
        Thread.sleep(900);
        Platform.runLater(() -> progressBar.setProgress(progress));
    }

   

    private void showResults(List<Component> installed) {

    int cpuScore = 0;
    int gpuScore = 0;
    int ramScore = 0;
    int storageScore = 0;

    for (Component c : installed) {
        switch (c.getType()) {
            case "CPU" -> cpuScore += c.getScore();
            case "GPU" -> gpuScore += c.getScore();
            case "RAM" -> ramScore += c.getScore();
            case "STORAGE" -> storageScore += c.getScore();
        }
    }

    int totalScore =
            cpuScore * 3 +
            gpuScore * 4 +
            ramScore +
            storageScore;

    
    String tier;
    String description;

    if (totalScore < 1500) {
        tier = "Basic Desktop";
        description = "Best suited for office work, browsing, and school tasks.";
    } else if (totalScore < 3000) {
        tier = "Mainstream PC";
        description = "Handles daily tasks smoothly with light gaming support.";
    } else if (totalScore < 5000) {
        tier = "Gaming PC";
        description = "Designed for modern games at high settings.";
    } else if (totalScore < 8000) {
        tier = "Workstation";
        description = "Optimized for rendering, editing, and heavy multitasking.";
    } else {
        tier = "Extreme / Enthusiast";
        description = "No compromises. Maximum performance for any workload.";
    }

    
    resultArea.appendText("=== BENCHMARK RESULTS ===\n\n");
    resultArea.appendText("CPU Score: " + cpuScore + "\n");
    resultArea.appendText("GPU Score: " + gpuScore + "\n");
    resultArea.appendText("Memory Score: " + ramScore + "\n");
    resultArea.appendText("Storage Score: " + storageScore + "\n\n");

    resultArea.appendText("OVERALL SCORE: " + totalScore + "\n\n");

    resultArea.appendText("SYSTEM CLASSIFICATION: \n");
    resultArea.appendText(description + "\n");

    statusLabel.setText("Benchmark complete");
    runButton.setDisable(false);
}


    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText("Benchmark");
        a.showAndWait();
    }
}
