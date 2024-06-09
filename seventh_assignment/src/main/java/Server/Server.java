package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Server Class
public class Server {
    // Port to listen for incoming connections
    private static final int PORT = 5431;
    // List to hold connected clients
    static ArrayList<Socket> clients = new ArrayList<>();
    // List to hold connected client usernames
    static ArrayList<String> usernames = new ArrayList<>();
    // Thread pool to manage client connections efficiently
    private static ExecutorService clientsTP = Executors.newFixedThreadPool(10);
    static final int n = 15;
    static ArrayList<String> massages = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        ServerSocket listener = null;
        try {

            // Start listening on the specified port
            listener = new ServerSocket(PORT);
            System.out.println("Server started...");

            // Continuously accept incoming client connections
            while (true) {
                // Accept a new client connection
                Socket client = listener.accept();

                // Create a new client handler thread to handle client requests
                ClientHandler clientThread = new ClientHandler(client);
                // Add the client socket to the list of connected clients
                clients.add(client);
                // Execute the client handler thread in the thread pool
                clientsTP.execute(clientThread);
            }
        } catch (IOException e) {
            // Handle IOExceptions (e.g., socket errors)
            System.out.println(e.getMessage());
        } finally {
            // Close the server socket when the server is shutting down
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            // Shutdown the thread pool to release resources
            clientsTP.shutdown();
        }
    }
}