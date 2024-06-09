package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerResponseHandler implements Runnable {

    private DataInputStream in; // Input stream to receive server responses
    private Socket client;
    private boolean isDownloading = false;

    public ServerResponseHandler(Socket client) throws IOException {
        this.client = client;
        this.in = new DataInputStream(client.getInputStream()); // Initialize the input stream to receive server responses
    }

    @Override
    public void run() {
        // Continuously listen for server responses
        while (true) {
            // Read server response from the input stream and print it to the console
            try {
                String resp = this.in.readUTF();

                if (resp.equals("-Exit-")) {
                    in.close();
                    break;
                }
                else {
                    System.out.println(resp);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
