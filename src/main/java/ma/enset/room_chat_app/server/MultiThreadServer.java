package ma.enset.room_chat_app.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiThreadServer extends Thread{
    private static int counter;
    private List<Conversation> conversations = new ArrayList<>();

    public static void main(String[] args) {
        new MultiThreadServer().start();
    }

    @Override
    public void run() {
        super.run();

        try {
            ServerSocket serverSocket = new ServerSocket(2331);
            while (true){
                Socket socket = serverSocket.accept();
                String name = getClientName(socket);
                counter++;
                Conversation conversation = new Conversation(socket, counter, conversations,name);
                conversations.add(conversation);
                conversation.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String getClientName(Socket socket){
        try {
            InputStream in = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            return br.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
