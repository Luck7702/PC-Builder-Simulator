package classes;

public class GPU extends Component {

    private int vramGB;
    private int coreClockMHz;
    private int memoryClockMHz;

    public GPU(
            String brand,
            String name,
            double price,
            int score,
            int vramGB,
            int coreClockMHz,
            int memoryClockMHz,
            int power
    ) {
        super(brand, name, price, score, power);
        this.vramGB = vramGB;
        this.coreClockMHz = coreClockMHz;
        this.memoryClockMHz = memoryClockMHz;
    }

    @Override
    public String getType() {
        return "GPU";
    }

    public int getVramGB() {
        return vramGB;
    }

    public int getCoreClockMHz() {
        return coreClockMHz;
    }

    public int getMemoryClockMHz() {
        return memoryClockMHz;
    }

    @Override
    public String toCSV() {
        return String.join(",",
                "GPU",
                brand,
                name,
                String.valueOf(price),
                String.valueOf(score),
                String.valueOf(vramGB),
                String.valueOf(coreClockMHz),
                String.valueOf(memoryClockMHz),
                String.valueOf(power)
        );
    }
    @Override
public String getFormattedDetails() {
    return String.format("GPU\nVRAM: %d GB\nCore: %d MHz\nPower Draw : %sW", vramGB, coreClockMHz,power);
}
}
