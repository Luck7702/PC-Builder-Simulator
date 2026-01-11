package classes;

public class Storage extends Component {

    private int capacityGB;
    private String type;
    private String interfaceType;

    

    public Storage(String brand, String name, double price, int score,
                   int power, int capacityGB, String type,String interfaceType) {
        super(brand, name, price, score, power);
        this.capacityGB = capacityGB;
        this.type = type;
        this.interfaceType = interfaceType;
    }

    public int getCapacityGB() { return capacityGB; }
    public String getStorageType() { return type; }
    public String getInterfaceType() {
        return interfaceType;
    }

    @Override
    public String getType() { return "STORAGE"; }

    @Override
    public String toCSV() {
        return String.join(",",
                getType(), brand, name,
                String.valueOf(price),
                String.valueOf(score),
                String.valueOf(capacityGB),
                interfaceType,
                type,
                String.valueOf(power)
        );
    }
    @Override
public String getFormattedDetails() {
    return String.format("Storage\n" +
            "Capacity: %d GB\n" +
            "Type: %s\n" +
            "Interface: %s\n" +
            "Power Draw: %dW", 
            capacityGB, type,interfaceType, power);
}
}
