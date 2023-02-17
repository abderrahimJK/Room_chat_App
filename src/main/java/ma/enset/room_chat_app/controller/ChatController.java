package ma.enset.room_chat_app.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import ma.enset.room_chat_app.server.RoomSession;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    public HBox subscribe_field;
    public TextField userName_txt;
    public FontAwesomeIconView subscribe_btn;
    public TextField chat_txt;
    public FontAwesomeIconView send_btn;
    public VBox vBox;
    public ScrollPane scroll_pane;
    Socket socket;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        scroll_pane.setFitToHeight(true);
        scroll_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        subscribe_btn.setOnMouseClicked((MouseEvent event) -> onSubscribe());
        send_btn.setOnMouseClicked((MouseEvent e)-> onChat());
        try {
            this.socket = new Socket("localhost", 2331);
            RoomSession.socket = socket;
            RoomSession.vBox = vBox;
            InputStream in = this.socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onSubscribe(){
        try{
            OutputStream out = socket.getOutputStream();
            PrintWriter pr = new PrintWriter(out,true);
            String name = userName_txt.getText();
            pr.println(name);
            RoomSession.name = name;
            subscribe_field.setDisable(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onChat(){
        try{
            OutputStream out = socket.getOutputStream();
            PrintWriter pr = new PrintWriter(out,true);

            String req = chat_txt.getText();
            if(!req.isEmpty()){

                pr.println(userName_txt.getText()+"#"+req);

                Text text= new Text(req);
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-padding: 12;" +
                        "-fx-background-color: #D7FAD1;"+
                        "-fx-background-radius: 5 5 0 5;"+
                        "-fx-pref-width: 430;"+
                        "-fx-effect: dropshadow(three-pass-box, #AAA, 3, 0,0,3);"
                );

                HBox hBox = new HBox(textFlow);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                vBox.getChildren().add(hBox);
                chat_txt.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}