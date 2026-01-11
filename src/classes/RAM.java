package classes;

public class RAM extends Component {

    private int sizeGB;
    private int speedMHz;      
    private String ramType;    

    public RAM(
            String brand,
            String name,
            double price,
            int score,
            int sizeGB,
            int speedMHz,      
            String ramType,    
            int power
    ) {
        super(brand, name, price, score, power);
        this.sizeGB = sizeGB;
        this.speedMHz = speedMHz;
        this.ramType = ramType;
    }

    
    @Override
    public String getFormattedDetails() {
        return String.format("RAM\nSize: %d GB\nSpeed: %d MHz\nType: %s\nPower Draw: %dW", 
                sizeGB, speedMHz, ramType, power);
    }

    @Override
    public String getType() {
        return "RAM";
    }

    @Override
    public String toCSV() {
        return String.join(",", "RAM", brand, name, 
                String.valueOf(price), String.valueOf(score), 
                String.valueOf(sizeGB), String.valueOf(speedMHz), 
                ramType, String.valueOf(power));
    }

    
    public int getSizeGB() { return sizeGB; }
    public int getSpeedMHz() { return speedMHz; }
    public String getRamType() { return ramType; }
}