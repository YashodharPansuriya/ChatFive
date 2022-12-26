import java.net.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter pout;

    //Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);

    //constructor
    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pout = new PrintWriter(socket.getOutputStream());

            //Build GUI for client side
            createGUI();

            //Handle Events
            handleEvents();

            // Function for Read and write messages from server
            startReading();
            // startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Implementation of GUI
    public void createGUI() {
        this.setTitle("Client Messanger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Code for components
        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);

        //Set heading to center position, border, image
        heading.setIcon(new ImageIcon("chat.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        ///Set messageinput text to center position and make messageArea input disabled
        messagArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //Set frame layout
        this.setLayout(new BorderLayout());

        //Adding the components to frame layout
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    //Handle Events -- send the type message to body
    public void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode()==10)
                {
                    String contentToSend = messageInput.getText();
                    messagArea.append("Me :" + contentToSend + "\n");
                    pout.println(contentToSend);
                    pout.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            } 
        });
    }

    // Read the message from the server (Serversocket)
    public void startReading() throws IOException {
        // Thread -- Read the message from the Server
        Runnable r1 = () -> {
            System.out.println("Read the  message from the server...");

            // Read the infinit message from the server
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server: " + msg);
                    messagArea.append("Server: " + msg + "\n");
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed...!!!");
            }
        };

        // Start the thread of startreading
        Thread t1 = new Thread(r1);
        t1.start();
    }

    // write the message to the server (serversocket)
    public void startWriting() throws IOException {

        // Thread -- Write the message to the server
        Runnable r2 = () -> {
            System.out.println("Write the  message from the server...");

            // write the infinit message to the server
            try {
                while (!socket.isClosed()) {
                    // Read the user data from console
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String content = reader.readLine();

                    // Send the data to the server
                    pout.println(content);
                    pout.flush();

                    // if ser ver write exit then break the connection
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed...!!!");
            }
        };

        // Start the thread of startwriting
        Thread t2 = new Thread(r2);
        t2.start();
    }

    public static void main(String[] args) {
        System.out.println("Client is running...");
        new Client();
    }
}