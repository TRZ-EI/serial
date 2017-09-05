package trzpoc.gui;

import javafx.concurrent.Task;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import trzpoc.comunication.SerialDataManager;
import trzpoc.structure.serial.SerialDataFacade;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/09/17
 * Time: 13.44
 */
public class CanvasTask extends Task<Canvas> {

    private final SerialDataFacade serialDataFacade;
    private final GraphicDesigner graphicDesigner;
    private boolean runningflag = true;
    private BlockingQueue<String> serialBuffer;
    private final int W = 800;
    private final int H = 480;

    public CanvasTask(GraphicDesigner graphicDesigner, SerialDataFacade serialDataFacade){
        this.serialDataFacade = serialDataFacade;
        this.serialBuffer = new LinkedBlockingQueue<String>(2048);
        this.graphicDesigner = graphicDesigner;
    }

    @Override
    protected Canvas call() throws Exception {
        SerialDataManager sdm = SerialDataManager.createNewInstanceByQueue(this.serialBuffer);
        sdm.connectToSerialPort();

        Canvas canvas = null;
        while (this.runningflag) {
            while(!this.serialBuffer.isEmpty()){
                String message = this.serialBuffer.take();
                canvas = new Canvas(W, H);
                canvas.setCache(false);
                canvas.setCacheHint(CacheHint.QUALITY);

                GraphicsContext gc = canvas.getGraphicsContext2D();
                this.serialDataFacade.onSerialDataInput(message.getBytes());
                this.graphicDesigner.drawOnCanvas(serialDataFacade.getDisplayManager(), gc);
                updateValue(canvas);
            }
        }
        sdm.disconnectFromSerialPort();
        return canvas;
    }
    public void setRunningflag(boolean runningflag) {
        this.runningflag = runningflag;
    }
}
