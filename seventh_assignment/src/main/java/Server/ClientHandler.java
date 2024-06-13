package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler implements Runnable {
    private Socket client; // The client socket
    private DataInputStream in; // Input stream to receive data from the client
    private DataOutputStream out; // Output stream to send data to the client
    private String username;

    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        // Initialize input and output streams for communication with the client
        this.in = new DataInputStream(client.getInputStream());
        this.out = new DataOutputStream(client.getOutputStream());
    }

    public void sendMassage(String massage) throws IOException {
        out.writeUTF(massage);
        out.flush();
    }
    boolean c = true;
    boolean c2 = false;

    @Override
    public void run() {
        try {
            String request;
            // Continuously listen for client requests
            while (true) {
                // Read client request from the input stream
                request = this.in.readUTF();
                // Process client request
                if (request != null) {
                    if (request.startsWith("<connect>")) {
                        username = request.substring(10);
                        out.writeUTF("\n\n--------Menu--------\n" + "Hi, " + username + "\n\n1) Group Chat\n2) Download Music Lyrics\n3) Exit");
                        out.flush();
                        // Print the received request on the server console
                        System.out.println(">> " + username + " Joined!");
                    }
                    else if (request.startsWith("<menu>")) {
                        out.writeUTF("\n\n--------Menu--------\n" + "Hi, " + username + "\n\n1) Group Chat\n2) Download Music Lyrics\n3) Exit");
                        out.flush();
                    }
                    else if (request.startsWith("-Exit-")) {
                        out.writeUTF("-Exit-");
                        out.flush();
                        break;
                    }
                    else if (request.startsWith("<GetMessages>")) {
                        String massageStr = "\n( Print -Exit- to close the connection )";
                        ArrayList<String> messageList = ChatHandler.getMessages();
                        for (String massage : messageList){
                            massageStr += "\n" + massage;
                        }
                        out.writeUTF(massageStr);
                        out.flush();
                    }
                    else if (request.startsWith("<NewMessage>")) {
                        String massage = request.substring(13);
                        Server.massages.add(massage);
                        ChatHandler.sendToAll(client, massage);
                    }
                    else if (request.startsWith("<DownloadFile>")) {
                        File[] filesList = new File("data").listFiles();
                        String menu = "\n\n--------Download Menu--------\n";

                        int i = 1;
                        for (File file : filesList) {
                            menu += i + ". " + file.getName() + "\n";
                            i++;
                        }
                        out.writeUTF(menu);
                        out.flush();
                    }
                }
            }
        }
        catch (IOException e) {
            // Handle any I/O exceptions that occur during communication with the client
            System.err.println("IO Exception in client handler!!!!!!");
            e.printStackTrace();
        }
        finally {
            try {
                System.out.println(">> " + username + " left!");
                // Close input and output streams and the client socket when done
                in.close();
                out.close();
                client.close();
                for (int i = 0; i < Server.clients.size(); i++) {
                    if (Server.clients.get(i) == client){
                        Server.clients.remove(i);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());;
            }
        }
    }

}
