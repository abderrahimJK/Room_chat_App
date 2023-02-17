package ma.enset.room_chat_app.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Console_Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 2331);
        try{
            InputStream in = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            OutputStream out = socket.getOutputStream();
            PrintWriter pr = new PrintWriter(out,true);
            new Thread(()->{
                String response;
                try{
                    while ((response= br.readLine())!=null){
                        System.out.println(response);
                    }
                }catch (Exception e){
                    System.out.println("Server disconnected");
                }
            }).start();

            String req;
            Scanner sc  = new Scanner(System.in);
            while ((req= sc.nextLine())!=null){
                pr.println(req);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
