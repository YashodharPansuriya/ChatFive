import java.net.*;
import java.io.*;

public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter pout;

    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pout = new PrintWriter(socket.getOutputStream());

            // Function for Read and write messages from server
            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read the message from the server (Serversocket)
    public void startReading() throws IOException {
        // Thread -- Read the message from the Server
        Runnable r1 = () -> {
            System.out.println("Read the  message from the server...");

            // Read the infinit message from the server
            while (true) {
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        break;
                    }
                    System.out.println("Server: " + msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //Start the thread of startreading
        Thread t1 = new Thread(r1);
        t1.start();
    }

    // write the message to the server (serversocket)
    public void startWriting() throws IOException {
        System.out.println(" ");

        // Thread -- Write the message to the server
        Runnable r2 = () -> {
            System.out.println("Write the  message from the server...");

            // write the infinit message to the server
            while (true) {
                try {
                        //Read the user data from console
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        String content = reader.readLine();

                        //Send the data to the server
                        pout.println(content);
                        pout.flush();
                    }	
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

         //Start the thread of startwriting
         Thread t2 = new Thread(r2);
         t2.start();
    }
    public static void main(String[] args) {
        System.out.println("Client is running...");
        new Client();
    }
}