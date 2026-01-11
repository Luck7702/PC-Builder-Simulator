package classes;

public class Motherboard extends Component {

    private int ramSlots;
    private String socket;
    private String supportedRamType; 

    public Motherboard(
            String brand,
            String name,
            double price,
            int score,
            int ramSlots,
            String socket,
            String supportedRamType,
            int power
    ) {
        super(brand, name, price, score, power);
        this.ramSlots = ramSlots;
        this.socket = socket;
        this.supportedRamType = supportedRamType;
    }

    public int getRamSlots() {
        return ramSlots;
    }

    public String getSocket() {
        return socket;
    }

    public String getSupportedRamType() {
        return supportedRamType;
    }

    @Override
    public String getType() {
        return "MOBO";
    }

    @Override
    public String toCSV() {
        return String.join(",",
                getType(),
                brand,
                name,
                String.valueOf(price),
                String.valueOf(score),
                String.valueOf(ramSlots),      
                supportedRamType,              
                socket,                        
                String.valueOf(power)
        );
    }
    @Override
public String getFormattedDetails() {
    return String.format("Motherboard\n" +
            "Socket: %s\n" +
            "Memory Type: %s\n" +
            "RAM Slots: %d\n" +
            "Power Draw: %dW", 
            socket, supportedRamType, ramSlots, power);
}
}
