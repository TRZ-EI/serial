package trzpoc.comunication;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 01/07/18
 * Time: 14.08
 */
public class SerialPortTimerTask extends TimerTask {
    private SerialDataManager serialDataManager;

    public SerialPortTimerTask(SerialDataManager serialDataManager) {
        this.serialDataManager = serialDataManager;
    }
    @Override
    public void run() {
        this.serialDataManager.disconnectFromSerialPort();
        try {
            this.serialDataManager.connectToSerialPort();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
