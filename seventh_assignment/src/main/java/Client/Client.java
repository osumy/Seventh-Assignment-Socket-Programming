package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

// Client Class
public class Client {
    // IP address of the server to connect to
    private static final String SERVER_IP = "127.0.0.1";
    // Port of the server to connect to
    private static final int SERVER_PORT = 5431;


    public static void main(String[] args) throws IOException, InterruptedException {
        String username;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        username = sc.next();

        Socket client = new Socket(SERVER_IP, SERVER_PORT); // Establish a connection to the server
        DataOutputStream out = new DataOutputStream(client.getOutputStream()); // Output stream to send data to the server

        out.writeUTF("<connect> " + username);
        out.flush();

        System.out.println("Connected!"); // Print a message indicating successful connection

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // Input stream reader to read user input from the console
        ServerResponseHandler responseHandler = new ServerResponseHandler(client); // Start a thread to handle server responses asynchronously
        new Thread(responseHandler).start();
        while (true) {
            String userInput = reader.readLine();

            if (userInput.equals("3")) {
                out.writeUTF("-Exit-");
                out.flush();
                return;
            } else if (userInput.equals("1")) {
                out.writeUTF("<GetMessages>");
                out.flush();
                while (true) {
                    userInput = reader.readLine();

                    if (userInput.equals("-Exit-")) {
                        out.writeUTF("-Exit-");
                        out.flush();
                        return;
                    }

                    out.writeUTF("<NewMessage> " + username + ": " + userInput);
                    out.flush();
                }
            } else if (userInput.equals("2")) {
                File[] filesList = new File("data").listFiles();
                out.writeUTF("<DownloadFile>");
                out.flush();

                String option = reader.readLine();

                assert filesList != null;
                File file = new File(filesList[Integer.parseInt(option) - 1].getName());
                FileWriter fw = new FileWriter(file);
                FileReader fr = new FileReader(filesList[Integer.parseInt(option) - 1]);

                int ch;
                while ((ch=fr.read()) != -1) {
                   fw.write((char)ch);
                }
                fr.close();
                fw.close();

                out.writeUTF("<menu>");
                out.flush();
            } else {
                out.writeUTF("-Exit-");
                out.flush();
                return;
            }
        }
    }
}