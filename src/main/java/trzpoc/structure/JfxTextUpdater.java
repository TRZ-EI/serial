package trzpoc.structure;

import javafx.scene.text.Text;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/08/18
 * Time: 11:29
 */
public class JfxTextUpdater implements Runnable {
    private final Text text;
    private final String value;

    public JfxTextUpdater(Text t, String value) {
        this.text = t;
        this.value = value;
    }

    @Override
    public void run() {
        this.text.setText(this.value);
    }
}
