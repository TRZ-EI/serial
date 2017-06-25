/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserverfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import socketfx.Constants;
import socketfx.FxSocketServer;
import socketfx.SocketListener;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class FXMLSocketController implements Initializable {

    private final int PORT = 20000;

    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private ObservableList<Byte> receivedMsgData;

    private boolean isConnected;

    public ObservableList<Byte> getReceivedMsgData() {
        if (this.receivedMsgData == null){
            this.receivedMsgData = FXCollections.observableList(new ArrayList<Byte>());
        }
        return receivedMsgData;
    }

    public void setReceivedMsgData(ObservableList<Byte> receivedMsgData) {
        this.receivedMsgData = receivedMsgData;
    }

    public enum ConnectionDisplayState {

        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketServer socket;

    private void connect() {
        socket = new FxSocketServer(new FxSocketListener(), PORT, Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    private void displayState(ConnectionDisplayState state) {
        switch (state) {
            case DISCONNECTED:
            case WAITING:
            case AUTOWAITING:
            case CONNECTED:
            case AUTOCONNECTED:
                break;
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
        displayState(ConnectionDisplayState.WAITING);
        connect();
    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");    
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {
                Byte[] bytes = new Byte[line.length() + 1];
                
                for (int i = 0; i < line.length(); i ++){
                    bytes[i] = Byte.valueOf(line.getBytes()[i]);
                }
                byte newLine = 0x0A;
                bytes[line.length()] = Byte.valueOf(newLine);
                receivedMsgData.addAll(bytes);
                System.out.println(line);
                // Not directly Call SerialDataFacade but use a listener on ObservableList<Byte> receivedMsgData.
            }
        }
        @Override
        public void onClosedStatus(boolean isClosed) {
        }
    }



    public void sendMessage(String message) {
        socket.sendMessage(message);
    }

    public void activateConnection() {
        displayState(ConnectionDisplayState.WAITING);
        connect();
        System.out.println("Ready to receive messages");
    }

    public void disconnect() {
        displayState(ConnectionDisplayState.DISCONNECTED);
        socket.shutdown();
    }

    private void activateAutoConnect(boolean value) {
        if (value) {
            if (isConnected) {
                displayState(ConnectionDisplayState.AUTOCONNECTED);
            } else {
                displayState(ConnectionDisplayState.AUTOWAITING);
                connect();
            }
        } else {
            if (isConnected) {
                displayState(ConnectionDisplayState.CONNECTED);
            } else {
                displayState(ConnectionDisplayState.WAITING);
            }
        }
    }

}
