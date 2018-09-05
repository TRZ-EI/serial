package trzpoc.structure;

import eu.hansolo.medusa.Gauge;
import javafx.scene.paint.Color;
import trzpoc.gui.TRZBar;

public class JfxGaugeBarUpdaterFragment implements RunnableFragment {
    private final Color color;
    private final double value;
    private final TRZBar gauge;

    public JfxGaugeBarUpdaterFragment(Color color, double finalValue, TRZBar t) {
        this.color = color;
        this.value = finalValue;
        this.gauge = t;
    }

    @Override
    public void executeFragment() {
        this.gauge.setColor(color);
        this.gauge.setValue(value);
    }
}


