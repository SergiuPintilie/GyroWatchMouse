import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class TestBtMouse {
    private static final String SPP_UUID = "0000110100001000800000805F9B34FB"; // RFCOMM UUID for serial communication
    private static StreamConnectionNotifier serverSocket;
    private static StreamConnection connection;
    public static double THRESHOLD=1;
    public static double CLICK_THRESHOLD=100;
    public static int SENSITIVITY= 4;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> UI.createUI());
        try {
            // Create the server socket
            String url = "btspp://localhost:" + SPP_UUID + ";authenticate=false;encrypt=false;master=false";
            serverSocket = (StreamConnectionNotifier) Connector.open(url);

            System.out.println("Server is waiting for connection...");

            // Wait for a connection
            connection = serverSocket.acceptAndOpen();
            System.out.println("Client connected!");

            // Read data from the input stream
            InputStream inputStream = connection.openInputStream();
            byte[] buffer = new byte[1024];  // Adjust buffer size as needed
            int bytesRead;

            while (true) {
                try {
                    //System.out.print("Sensitivity: " + SENSITIVITY+" Threshold: " + THRESHOLD+ " Click Threshold: " + CLICK_THRESHOLD);
                    // Check if data is available in the stream
                    bytesRead = inputStream.read(buffer);

                    // If no data is available, break out of the loop
                    if (bytesRead == -1) {
                        System.out.println("End of stream reached");
                        break;
                    }
                    Robot robot = new Robot();
                    // Convert the buffer to a string and print
                    String receivedData = new String(buffer, 0, bytesRead);
                    //System.out.println("Received data: " + receivedData);

                    if (receivedData.startsWith("X:")) {
                        // Parse accelerometer data
                        Point mousePos = MouseInfo.getPointerInfo().getLocation();
                        String[] parts = receivedData.split(", ");
                        double prevX=0;
                        double prevY=0;
                        double prevZ=0;
                        int newX=(int)mousePos.getX();
                        int newY=(int)mousePos.getY();
                        LowPassFilter lpf=new LowPassFilter(100);

                        double accelX = Double.parseDouble(parts[2].split(":")[1]);
                        double accelY = Double.parseDouble(parts[1].split(":")[1]);
                        double accelZ = Double.parseDouble(parts[0].split(":")[1]);
                        //System.out.println(" Math.abs: "+ Math.abs(accelZ-prevZ)+ " Diff: "+ (accelZ-prevZ));
                        if(Math.abs(accelZ-prevZ)>CLICK_THRESHOLD && accelZ-prevZ<CLICK_THRESHOLD){//todo
                            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        }
                        else if(Math.abs(accelZ-prevZ)>CLICK_THRESHOLD && (accelZ-prevZ) >CLICK_THRESHOLD){
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        }
                        else{//todo
                            if (Math.abs(accelX-prevX)<THRESHOLD){}
                            else{
                                lpf.filter(accelX);
                                int currentX = (int) mousePos.getX();
                                newX = currentX - (int) (accelX * SENSITIVITY);
                                prevX=newX;
                            }
                            if (Math.abs(accelY-prevY)<THRESHOLD){}
                            else{
                                lpf.filter(accelY);
                                int currentY = (int) mousePos.getY();
                                newY = currentY + (int) (accelY * SENSITIVITY);
                                prevY=newY;
                            }
                            robot.mouseMove(newX, newY);
                        }
                    }
                    // Additional logic to process received data can go here
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

            // Close the connection
            connection.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
