package trzpoc.structure;

import eu.hansolo.medusa.Gauge;
import javafx.scene.paint.Color;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/08/18
 * Time: 11:40
 */
public class JfxGaugeBarUpdater implements Runnable {
    private final Color color;
    private final double value;
    private final Gauge gauge;

    public JfxGaugeBarUpdater(Color color, double finalValue, Gauge t) {
        this.color = color;
        this.value = finalValue;
        this.gauge = t;
    }

    @Override
    public void run() {
        this.gauge.barColorProperty().set(color);
        this.gauge.setValue(value);
    }
}
