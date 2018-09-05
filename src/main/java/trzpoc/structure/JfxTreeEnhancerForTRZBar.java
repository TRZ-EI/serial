package trzpoc.structure;

import javafx.scene.Node;
import trzpoc.gui.DrawingText;
import trzpoc.gui.TRZBar;

public class JfxTreeEnhancerForTRZBar implements Runnable {
    private final Node node;
    private final DrawingText window;

    public JfxTreeEnhancerForTRZBar(Node bar, DrawingText mainWindow) {
        this.node = bar;
        this.window = mainWindow;
    }
    @Override
    public void run() {
        TRZBar trzBar = (TRZBar)this.node;
        // ZERO POS: 70% width (800 px)
        trzBar.setupZeroBar(70d, this.window.getRoot());
        trzBar.calculateBarParams();
        this.window.getRoot().getChildren().add(node);

    }
}
