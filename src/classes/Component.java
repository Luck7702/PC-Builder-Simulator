package classes;
import java.io.Serializable;
public abstract class Component implements Serializable{

    protected String brand;
    protected String name;
    protected double price;
    protected int score;
    protected int power;

    public Component(String brand, String name, double price, int score, int power) {
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.score = score;
        this.power = power;
    }

    public abstract String getType();
    public abstract String toCSV();

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getScore() {
        return score;
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {
        return brand + " " + name;
    }
    public abstract String getFormattedDetails();
}
