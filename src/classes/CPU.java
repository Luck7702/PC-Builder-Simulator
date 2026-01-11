package classes;

public class CPU extends Component {

    private int cores;
    private double speedGHz;
    private String socket;

    public CPU(
            String brand,
            String name,
            double price,
            int score,
            int cores,
            double speedGHz,
            String socket,
            int power
    ) {
        super(brand, name, price, score, power);
        this.cores = cores;
        this.speedGHz = speedGHz;
        this.socket = socket;
    }

    @Override
    public String getType() {
        return "CPU";
    }

    public int getCores() {
        return cores;
    }

    public double getSpeedGHz() {
        return speedGHz;
    }

    public String getSocket() {
        return socket;
    }

    @Override
    public String toCSV() {
        return String.join(",",
                "CPU",
                brand,
                name,
                String.valueOf(price),
                String.valueOf(score),
                String.valueOf(cores),
                String.valueOf(speedGHz),
                socket,
                String.valueOf(power)
        );
    }
    @Override
public String getFormattedDetails() {
    return String.format("CPU\nCores: %d\nSpeed: %.1f GHz\nSocket: %s\nPower Draw: %sW", cores, speedGHz, socket,power);
}
}
