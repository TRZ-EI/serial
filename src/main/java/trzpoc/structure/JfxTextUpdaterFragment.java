package trzpoc.structure;

import javafx.scene.text.Text;

public class JfxTextUpdaterFragment implements RunnableFragment {
    private final Text text;
    private final String value;

    public JfxTextUpdaterFragment(Text t, String value) {
        this.text = t;
        this.value = value;
    }
    @Override
    public void executeFragment() {
        this.text.setText(this.value);
    }
}
