package trzpoc.structure;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 28/05/17
 * Time: 16.14
 */
public class Bar extends Cell {
    private long minValue;
    private long maxValue;

    public long getMinValue() {
        return minValue;
    }

    public Bar setMinValue(long minValue) {
        this.minValue = minValue;
        return this;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public Bar setMaxValue(long maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public static Bar getInstance() {
        return new Bar();
    }
}