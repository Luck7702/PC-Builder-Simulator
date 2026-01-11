package classes;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    private List<Component> catalog;

    public Shop() {
        this.catalog = FileHandler.loadCatalog();
    }

    
    public List<Component> getCatalog() {
        return catalog;
    }

    public ArrayList<Component> getCatalogByType(String type) {
        ArrayList<Component> filteredList = new ArrayList<>();
        for (Component c : catalog) {
            if (c.getType().equalsIgnoreCase(type)) {
                filteredList.add(c);
            }
        }
        return filteredList;
    }
}
