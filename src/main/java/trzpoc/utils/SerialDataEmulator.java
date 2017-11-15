package trzpoc.utils;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 09/11/17
 * Time: 10.41
 */

import trzpoc.structure.serial.MultipleCommandSplitter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 23/03/17
 * Time: 11.38
 */
public class SerialDataEmulator {
    private BlockingQueue<String> serialBuffer;
    private final int waitingTime;

    private final Byte fineStringa = Byte.valueOf("0A", 16).byteValue();





    private SerialDataEmulator(BlockingQueue<String> queue, int waitingTime) {
        this.serialBuffer = queue;
        this.waitingTime = waitingTime;
    }

    public static SerialDataEmulator getNewInstanceBySerialBufferAndWaitingTime(BlockingQueue<String> queue, int waitingTime) {
        return new SerialDataEmulator(queue, waitingTime);
    }
    public void runScenario(String fileName) throws IOException, InterruptedException {
        String realFileName = this.getClass().getClassLoader().getResource(fileName).getFile();
        Scanner linReader = new Scanner(new File(realFileName));
        String line = null;
        while(linReader.hasNextLine()){
            Thread.sleep(this.waitingTime);
            line = linReader.nextLine();
            this.serialBuffer.addAll(MultipleCommandSplitter.getNewInstance().splitMultipleCommand(line));
            System.out.println(line);
        }

    }
    public static void main(String[] args) throws Exception {
        SerialDataEmulator sdm = SerialDataEmulator.getNewInstanceBySerialBufferAndWaitingTime(new LinkedBlockingQueue<>(), 1000);
        sdm.runScenario("serialInputs/prova1-fragment1.txt");
    }





}
