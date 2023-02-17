module ma.enset.room_chat_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;


    opens ma.enset.room_chat_app to javafx.fxml;
    exports ma.enset.room_chat_app;
    exports ma.enset.room_chat_app.controller;
    opens ma.enset.room_chat_app.controller to javafx.fxml;
}