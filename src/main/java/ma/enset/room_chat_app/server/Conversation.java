package ma.enset.room_chat_app.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conversation extends  Thread{

    private final Socket socket;
    private final int clientId;
    private final List<Conversation> conversations;
    private final String name;

    public Conversation(Socket socket, int id, List<Conversation> conversations, String name) {
        this.socket = socket;
        this.clientId = id;
        this.conversations = conversations;
        this.name= name;
    }

    @Override
    public void run() {
        super.run();
        try {
            InputStream in = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            OutputStream out = socket.getOutputStream();
            PrintWriter pr = new PrintWriter(out, true);
            String ip = socket.getRemoteSocketAddress().toString();
            System.out.println("New Client ==>" + clientId + " - " + name + " IP : " + ip);

            String req;

            while ((req = br.readLine())!=null){
                System.out.println(req);
                List<Integer> clientsId = new ArrayList<>();
                String msg ="";
                if(req.contains("=>")){

                    String[] items = req.split("=>");
                    String clients= items[0];
                    msg= items[1];

                    if(clients.contains(",")){
                        String[] idsList = clients.split(",");

                        for (String s : idsList){
                            clientsId.add(Integer.parseInt(s));
                        }
                    }else{
                        clientsId.add(Integer.parseInt(clients));
                    }
                }else{
                    clientsId = conversations.stream().map(c->c.clientId).collect(Collectors.toList());
                    msg = req;
                }
                broadCastMessages(this,msg,clientsId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void broadCastMessages(Conversation from, String data, List<Integer> clientsId) {
        try {
            for(Conversation c : conversations){
                OutputStream out = c.socket.getOutputStream();
                PrintWriter pr = new PrintWriter(out,true);
                if(c!=from && clientsId.contains(c.clientId)) {
                    pr.println(data);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
