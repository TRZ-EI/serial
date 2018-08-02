package trzpoc.structure;

import trzpoc.gui.DrawingText;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/08/18
 * Time: 12:35
 */
public class JfxCleaner implements Runnable {
    private final DrawingText window;

    public JfxCleaner(DrawingText mainWindow) {
        this.window = mainWindow;
    }

    @Override
    public void run() {
        this.window.getRoot().getChildren().clear();
        this.window.drawGridOnCanvas();
    }
}
