package classes;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
public class PCBuild implements Serializable{

    private String name;
    private final List<Component> installedComponents;

   

    public PCBuild(String name) {
        this.name = name;
        this.installedComponents = new ArrayList<>();
    }

   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

    public void install(Component c) {
        if (c != null) {
            installedComponents.add(c);
        }
    }

    public void remove(Component c) {
        installedComponents.remove(c);
    }

    public void clear() {
        installedComponents.clear();
    }

    public List<Component> getInstalledComponents() {
        return installedComponents;
    }

   

    public int getTotalPower() {
        return installedComponents.stream()
                .mapToInt(Component::getPower)
                .sum();
    }

    public double getTotalCost() {
        return installedComponents.stream()
                .mapToDouble(Component::getPrice)
                .sum();
    }

    public int getTotalPerformance() {
        return installedComponents.stream()
                .mapToInt(Component::getScore)
                .sum();
    }

   

    public boolean hasCPU() {
        return installedComponents.stream().anyMatch(c -> c instanceof CPU);
    }

    public boolean hasGPU() {
        return installedComponents.stream().anyMatch(c -> c instanceof GPU);
    }

    public boolean hasRAM() {
        return installedComponents.stream().anyMatch(c -> c instanceof RAM);
    }

    public boolean hasStorage() {
        return installedComponents.stream().anyMatch(c -> c instanceof Storage);
    }

    public boolean hasPSU() {
        return installedComponents.stream().anyMatch(c -> c instanceof PowerSupply);
    }

    public boolean hasMotherboard() {
        return installedComponents.stream().anyMatch(c -> c instanceof Motherboard);
    }

    /**
     * Minimum requirements for a PC to "turn on"
     */
    public boolean isOperational() {
        return hasCPU() && hasRAM() && hasPSU();
    }

    /**
     * Full PC — required for benchmarking & selling
     */
    public boolean isComplete() {
        return hasCPU()
            && hasGPU()
            && hasRAM()
            && hasStorage()
            && hasPSU()
            && hasMotherboard();
    }
}
