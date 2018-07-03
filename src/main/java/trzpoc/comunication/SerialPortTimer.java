package trzpoc.comunication;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 01/07/18
 * Time: 14.41
 */
public class SerialPortTimer{

    private boolean go;
    private SerialPortTimerTask timerTask;
    private long timeInterval; // millseconds
    private long startTime;

    private static SerialPortTimer instance;
    public static SerialPortTimer getSingleInstanceByTimerTask(SerialPortTimerTask task, long interval){
        if (instance == null){
            instance = new SerialPortTimer(task, interval);
        }
        return instance;
    }
    private SerialPortTimer(SerialPortTimerTask task, long interval){
        this.startTime = System.currentTimeMillis();
        this.timeInterval = interval;
        this.timerTask = task;
    }


    public void runTask(){
        long now = System.currentTimeMillis();
        if (now - this.startTime >= this.timeInterval){
            this.timerTask.run();
            this.startTime = now;
        }
    }

    
}
