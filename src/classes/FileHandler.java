package classes;

import java.io.*;
import java.util.*;

public class FileHandler {

    private static final String INVENTORY_FILE = "data/inventory_data.txt";
    private static final String BUILDS_FILE    = "data/builds_data.txt";
    private static final String CATALOG_FILE   = "data/component_catalog.csv";

    public static void saveAll(double budget, Inventory inv, ArrayList<PCBuild> builds) {
        saveData(inv);
        saveBuilds(budget, builds);
    }

    public static Object[] loadAll() {
        return new Object[]{ loadBudget(), loadData(), loadBuilds() };
    }

   

    public static void saveData(Inventory inventory) {
        try (PrintWriter out = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            for (Component c : inventory.getParts()) {
                out.println(c.toCSV()); 
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static Inventory loadData() {
        Inventory inv = new Inventory();
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) return inv;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) inv.add(parseCSV(line));
            }
        } catch (Exception e) { System.out.println("Inv Load Error: " + e.getMessage()); }
        return inv;
    }

   

    public static void saveBuilds(double budget, ArrayList<PCBuild> builds) {
        try (PrintWriter out = new PrintWriter(new FileWriter(BUILDS_FILE))) {
            out.println("BUDGET:" + budget);
            for (PCBuild build : builds) {
                out.println("START_BUILD:" + build.getName());
                for (Component c : build.getInstalledComponents()) {
                    out.println("COMP:" + c.toCSV());
                }
                out.println("END_BUILD");
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static ArrayList<PCBuild> loadBuilds() {
        ArrayList<PCBuild> builds = new ArrayList<>();
        File file = new File(BUILDS_FILE);
        if (!file.exists()) return builds;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            PCBuild current = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("START_BUILD:")) {
                    current = new PCBuild(line.split(":")[1]);
                    builds.add(current);
                } else if (line.startsWith("COMP:") && current != null) {
                    current.install(parseCSV(line.substring(5)));
                }
            }
        } catch (Exception e) { System.out.println("Build Load Error: " + e.getMessage()); }
        return builds;
    }

    public static double loadBudget() {
        File file = new File(BUILDS_FILE);
        if (!file.exists()) return 9999.0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && line.startsWith("BUDGET:")) return Double.parseDouble(line.split(":")[1]);
        } catch (Exception e) { return 9999.0; }
        return 9999.0;
    }

   

    private static Component parseCSV(String line) {
        String[] p = line.split(",");
        String type = p[0].trim();

        return switch (type) {
            case "CPU" -> new CPU(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), 
                                 Integer.parseInt(p[5]), Double.parseDouble(p[6]), p[7], Integer.parseInt(p[8]));
            
            case "GPU" -> new GPU(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), 
                                 Integer.parseInt(p[5]), Integer.parseInt(p[6]), Integer.parseInt(p[7]), Integer.parseInt(p[8]));
            
            case "RAM" -> new RAM(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), 
                                 Integer.parseInt(p[5]), Integer.parseInt(p[6]), p[7], Integer.parseInt(p[8]));
            
            case "STORAGE" -> new Storage(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), 
                                         Integer.parseInt(p[8]), Integer.parseInt(p[5]), p[7],p[6]);
            
            case "PSU" -> new PowerSupply(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), 
                                         Integer.parseInt(p[8]), Integer.parseInt(p[5]), p[6]);
            
            case "MOBO" -> new Motherboard(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), 
                                          Integer.parseInt(p[5]), p[7], p[6], Integer.parseInt(p[8]));
            
            default -> throw new RuntimeException("Unknown type: " + type);
        };
    }

    public static ArrayList<Component> loadCatalog() {
        ArrayList<Component> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CATALOG_FILE))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) list.add(parseCSV(line));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}