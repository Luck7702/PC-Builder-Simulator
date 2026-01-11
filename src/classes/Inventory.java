package classes;

import java.util.ArrayList;
import java.io.Serializable;

public class Inventory implements Serializable{

    private ArrayList<Component> parts;

    public Inventory() {
        this.parts = new ArrayList<>();
    }

    public ArrayList<Component> getParts() {
        return parts;
    }

    public void add(Component c) {
        parts.add(c);
    }

    public void remove(Component c) {
        parts.remove(c);
    }

    public boolean contains(Component c) {
        return parts.contains(c);
    }

    public void clear() {
        parts.clear();
    }
    

}
