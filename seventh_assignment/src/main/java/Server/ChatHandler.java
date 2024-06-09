package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ChatHandler {
    public static void sendToAll(Socket sender, String massage) throws IOException {
        for (Socket socket : Server.clients) {
            if (socket != sender) {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(massage);
                out.flush();
            }
        }
    }

    public static ArrayList<String> getMessages(){
        if (Server.massages.size() <= Server.n) {
            return Server.massages;
        }
        else {
            ArrayList<String> messagesList = new ArrayList<>();
            for (int i = Server.massages.size() - 15; i < Server.massages.size(); i++) {
                messagesList.add(Server.massages.get(i));
            }
            return messagesList;
        }
    }
}
