public class LowPassFilter {
    private double alpha;  // The smoothing factor (between 0 and 1)
    private double prevValue;

    public LowPassFilter(double alpha) {
        this.alpha = alpha;
        this.prevValue = 0;  // Initial value can be 0 or the first sensor reading
    }

    public double filter(double newValue) {
        prevValue = prevValue + alpha * (newValue - prevValue);
        return prevValue;
    }
}
