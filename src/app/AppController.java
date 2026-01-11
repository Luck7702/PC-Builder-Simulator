package app;

import classes.*;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AppController {

    private Shop shop;
    private Inventory inventory;
    private ArrayList<PCBuild> builds;
    private BenchmarkService benchmarkService;

    private final IntegerProperty activeBuildIndexProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty buildsVersionProperty = new SimpleIntegerProperty(0);
    private double budget = 9000.0;

    public AppController() {
        inventory = new Inventory();
        builds = new ArrayList<>();
        benchmarkService = new BenchmarkService();
        shop = new Shop();
        builds.add(new PCBuild("MyPC"));
    }

    public void nextBuild() {
        if (builds.isEmpty()) return;
        activeBuildIndexProperty.set((activeBuildIndexProperty.get() + 1) % builds.size());
    }

    public PCBuild getActiveBuild() {
        if (builds.isEmpty()) return null;
        return builds.get(activeBuildIndexProperty.get());
    }

    public IntegerProperty getBuildsVersionProperty() { return buildsVersionProperty; }
    public IntegerProperty getActiveBuildIndexProperty() { return activeBuildIndexProperty; }
    public int getActiveBuildIndex() { return activeBuildIndexProperty.get(); }
    public void setActiveBuild(int index) { activeBuildIndexProperty.set(index); }
    public Inventory getInventory() { return inventory; }
    public ArrayList<PCBuild> getBuilds() { return builds; }
    public double getBudget() { return budget; }

    private void bumpBuildsVersion() {
        buildsVersionProperty.set(buildsVersionProperty.get() + 1);
    }

    public void saveAll() {
        FileHandler.saveAll(budget, inventory, builds);
        System.out.println("Readable save successful!");
    }

    public void loadAll() {
        Object[] data = FileHandler.loadAll();
        
        this.budget = (double) data[0];
        this.inventory = (Inventory) data[1];
        
        ArrayList<PCBuild> loadedBuilds = (ArrayList<PCBuild>) data[2];
        this.builds.clear();
        this.builds.addAll(loadedBuilds);

        if (this.builds.isEmpty()) {
            this.builds.add(new PCBuild("MyPC"));
        }

        activeBuildIndexProperty.set(0);
        bumpBuildsVersion();
        System.out.println("Readable load successful!");
    }

    
    public void saveData() { saveAll(); }
    public void loadData() { loadAll(); }
    public void saveGame() { saveAll(); }
    public void loadGame() { loadAll(); }


    public String installFromInventory(int index) {

    if (index < 0 || index >= inventory.getParts().size())
        return "Invalid selection";

    PCBuild build = getActiveBuild();
    if (build == null)
        return "No active PC build";

    Component c = inventory.getParts().get(index);

    if (c instanceof CPU && hasComponentType(CPU.class, build))
        return "Only ONE CPU allowed";

    if (c instanceof GPU && hasComponentType(GPU.class, build))
        return "Only ONE GPU allowed";

    if (c instanceof Motherboard && hasComponentType(Motherboard.class, build))
        return "Only ONE Motherboard allowed";

    if (c instanceof PowerSupply && hasComponentType(PowerSupply.class, build))
        return "Only ONE PSU allowed";

    if (c instanceof Storage && hasComponentType(Storage.class, build))
        return "Only ONE Storage allowed";

    String error = checkCompatibility(c, build);
    if (error != null) return error;

    inventory.getParts().remove(index);
    build.install(c);
    bumpBuildsVersion();

    return "Installed " + c.getName();
}
    public String buyComponent(Component c) {
        if (budget >= c.getPrice()) {
            budget -= c.getPrice();
            inventory.add(c);
            bumpBuildsVersion();
            return "Purchased";
        }
        return "Insufficient funds";
    }

   public double sellActiveBuild() {
    PCBuild build = getActiveBuild();
    
    if (build == null) return -1;
    
    
    if (!build.isComplete()) {
        return -2; 
    }
    
    double total = 0;
    for (Component c : build.getInstalledComponents()) {
        total += c.getPrice();
    }
    
    double earned = total * 1.2;
    budget += earned;
    build.getInstalledComponents().clear();
    bumpBuildsVersion();
    
    return earned;
}

public void clearBuildAndReturnToInventory() {
    PCBuild build = getActiveBuild();
    if (build == null) return;
    for (Component c : build.getInstalledComponents()) {
        inventory.add(c);
    }
    build.getInstalledComponents().clear();
    bumpBuildsVersion();
}

public void removeFromBuild(int index) {
    PCBuild build = getActiveBuild();
    if (build != null && index >= 0 && index < build.getInstalledComponents().size()) {
        Component c = build.getInstalledComponents().remove(index);
        inventory.add(c);
        bumpBuildsVersion();
    }
}

public void addNewBuild(String name) {
    if (name != null && !name.trim().isEmpty()) {
        builds.add(new PCBuild(name));
        bumpBuildsVersion();
    }
}

public List<Component> getInstalledComponents() {
    PCBuild build = getActiveBuild();
    return (build != null)
        ? build.getInstalledComponents()
        : new ArrayList<>();
}

public BenchmarkService.Result runBenchmark() {

    PCBuild build = getActiveBuild();
    if (build == null) return null;

    boolean hasCPU = false;
    boolean hasMOBO = false;
    boolean hasRAM = false;
    PowerSupply psu = null;

    for (Component c : build.getInstalledComponents()) {

        if (c instanceof CPU) hasCPU = true;
        else if (c instanceof Motherboard) hasMOBO = true;
        else if (c instanceof RAM) hasRAM = true;
        else if (c instanceof PowerSupply) psu = (PowerSupply) c;
    }
    if (!hasCPU || !hasMOBO || !hasRAM || psu == null) {
        return null;
    }

    int totalDraw = 0;
    for (Component c : build.getInstalledComponents()) {
        if (!(c instanceof PowerSupply)) {
            totalDraw += c.getPower();
        }
    }

    if (psu.getWattage() < totalDraw) {
        return null;
    }


    return benchmarkService.run(build);
}

public List<Component> getShopByCategory(String cat) {
    return shop.getCatalogByType(cat);
}

private String checkCompatibility(Component c, PCBuild build) {

    Motherboard mobo = null;
    CPU cpu = null;

    for (Component comp : build.getInstalledComponents()) {
        if (comp instanceof Motherboard) mobo = (Motherboard) comp;
        if (comp instanceof CPU) cpu = (CPU) comp;
    }

    if (c instanceof CPU newCpu) {

        if (mobo != null &&
            !mobo.getSocket().equalsIgnoreCase(newCpu.getSocket())) {

            return "CPU socket incompatible (" +
                    newCpu.getSocket() + " vs " + mobo.getSocket() + ")";
        }
    }

    if (c instanceof Motherboard newMobo) {

        if (cpu != null &&
            !cpu.getSocket().equalsIgnoreCase(newMobo.getSocket())) {

            return "Motherboard socket incompatible (" +
                    newMobo.getSocket() + " vs " + cpu.getSocket() + ")";
        }
    }

    if (c instanceof RAM newRam) {

        if (mobo != null &&
            !newRam.getRamType()
                   .equalsIgnoreCase(mobo.getSupportedRamType())) {

            return "RAM type incompatible (" +
                    newRam.getRamType() + " vs " +
                    mobo.getSupportedRamType() + ")";
        }
    }

    if (c instanceof Motherboard newMobo) {

        for (Component comp : build.getInstalledComponents()) {

            if (comp instanceof RAM ram) {

                if (!ram.getRamType()
                        .equalsIgnoreCase(newMobo.getSupportedRamType())) {

                    return "Installed RAM incompatible (" +
                            ram.getRamType() + " vs " +
                            newMobo.getSupportedRamType() + ")";
                }
            }
        }
    }

    return null;
}

private boolean hasComponentType(Class<?> type, PCBuild build) {
    for (Component c : build.getInstalledComponents()) {
        if (type.isInstance(c)) return true;
    }
    return false;
}

}