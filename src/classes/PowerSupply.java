package classes;

public class PowerSupply extends Component {

    private int wattage;
    private String rating;

    public PowerSupply(
            String brand,
            String name,
            double price,
            int score,
            int power,
            int wattage,
            String rating
    ) {
        super(brand, name, price, score, power);
        this.wattage = wattage;
        this.rating = rating;
    }

    public int getWattage() {
        return wattage;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String getType() {
        return "PSU";
    }

    @Override
    public String toCSV() {
        return String.join(",",
                getType(),
                brand,
                name,
                String.valueOf(price),
                String.valueOf(score),
                String.valueOf(wattage), // Spec1
                rating,                 // Spec2
                "",                     // Spec3 (reserved)
                String.valueOf(power)
        );
    }
    @Override
public String getFormattedDetails() {
    return String.format("Power Supply\n" +
            "Capacity: %d W\n" +
            "Efficiency: %s\n", 
            wattage, rating);
}
}
