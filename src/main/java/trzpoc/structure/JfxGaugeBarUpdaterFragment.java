package trzpoc.structure;

import eu.hansolo.medusa.Gauge;
import javafx.scene.paint.Color;

public class JfxGaugeBarUpdaterFragment implements RunnableFragment {
    private final Color color;
    private final double value;
    private final Gauge gauge;

    public JfxGaugeBarUpdaterFragment(Color color, double finalValue, Gauge t) {
        this.color = color;
        this.value = finalValue;
        this.gauge = t;
    }

    @Override
    public void executeFragment() {
        this.gauge.barColorProperty().set(color);
        this.gauge.setValue(value);
    }
}


