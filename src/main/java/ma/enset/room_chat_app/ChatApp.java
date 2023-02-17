package ma.enset.room_chat_app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import ma.enset.room_chat_app.server.RoomSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader subscribeView = new FXMLLoader(ChatApp.class.getResource("/fxml/chatViewApp.fxml"));
        Scene scene1 = new Scene(subscribeView.load());
        stage.setTitle("Hello!");
        stage.setScene(scene1);
        stage.show();
        Socket socket = RoomSession.socket;
        if(socket != null){

            new Thread(()->{
                String serverMsg;
                try{
                    InputStream in = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(isr);
                    while((serverMsg=br.readLine())!=null){
                        String[] finalServerMsg = serverMsg.split("#");
                        Platform.runLater(()->{
                                HBox hBox = new HBox();
                                Text text= new Text();
                                text.setText(finalServerMsg[1]);
                                Text from = new Text(finalServerMsg[0]+" : ");
                                from.setStyle("-fx-font-weight: bold");
                                TextFlow textFlow = new TextFlow(from,text);
                                textFlow.setStyle("-fx-padding: 12;" +
                                        "-fx-background-color: #fff;"+
                                        "-fx-background-radius: 5 5 5 0;"+
                                        "-fx-pref-width: 430;"+
                                        "-fx-effect: dropshadow(three-pass-box, #AAA, 3, 0,0,3);"
                                );
                                hBox.getChildren().add(textFlow);
                                hBox.setAlignment(Pos.CENTER_LEFT);
                                RoomSession.vBox.getChildren().add(hBox);
                            }
                        );
                    }
                }catch (Exception e){
                }
            }).start();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}