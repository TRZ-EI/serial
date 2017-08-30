package trzpoc.comunication;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import trzpoc.utils.ConfigurationHolder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/08/17
 * Time: 16.29
 */
public class SerialListenerForTest {

    private SerialDataManager sut;

    private SerialListenerForTest(SerialDataManager serialDataManager) {
        this.sut = serialDataManager;
        this.addListenerForDataChanged();
        ConfigurationHolder.createSingleInstanceByConfigUri(this.getClass().getClassLoader().getResource("application.properties").getFile());
    }
    public static SerialListenerForTest getNewInstanceBySerialDataManager(SerialDataManager serialDataManager){
        return new SerialListenerForTest(serialDataManager);
    }
    private void addListenerForDataChanged(){
        this.sut.getIsDataAvalaible().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    while(!sut.getSerialBuffer().isEmpty()){
                        String[] y = sut.getSerialBuffer().toArray(new String[0]);
                        sut.getSerialBuffer().clear();


                        //List<String> messages = new ArrayList<>();
                        //sut.getSerialBuffer().removeAll(messages);
                        System.out.println("MESSAGES READ: " + y.length);
                        for (String m: y) {
                            System.out.print(m);
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        sut.setIsDataAvalaible(false);
                    }
                }

            }
        });
    }

    public static void main(String[] args) throws NoSuchPortException, PortInUseException, IOException {
        SerialDataManager sut = SerialDataManager.createNewInstance();
        SerialListenerForTest sl = SerialListenerForTest.getNewInstanceBySerialDataManager(sut);
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String input = "run";
        boolean isConnected = sut.connectToSerialPort();
        if (isConnected){
            System.out.println("Run Arduino");
        }
        while ((input = reader.readLine()).length() != 0) {
            if (input.equalsIgnoreCase("stop")){
                sut.disconnectFromSerialPort();
                System.exit(0);
            }
        }
    }
}
