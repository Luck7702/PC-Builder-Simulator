package classes;

public class BenchmarkService {

    public static class Result {
    public final int score;
    public final String message;
    public Result(int score, String message) {
        this.score = score;
        this.message = message;
    }
}

    public Result run(PCBuild build) {
        int score = build.getTotalPerformance();
        double commission = score * 0.15;
return new Result(
    score,
    "Commission earned: $" + String.format("%.2f", commission)
);

    }
}
