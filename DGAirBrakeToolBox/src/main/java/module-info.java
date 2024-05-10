module com.dgairbraketoolbox.dgairbraketoolbox {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    requires com.google.auth;
    requires com.google.api.client;
    requires google.api.client;
    requires google.api.services.gmail.v1.rev110;
    requires java.mail;
    requires com.google.api.client.json.gson;
    requires com.google.auth.oauth2;
    requires com.google.api.client.auth;
    requires com.google.api.client.http.apache.v2;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires activation;
    requires org.apache.commons.codec;
    requires jdk.httpserver; // TODO: Why is this required in JavaFX apps but not others

    opens com.dgairbraketoolbox.dgairbraketoolbox to javafx.fxml;
    exports com.dgairbraketoolbox.dgairbraketoolbox;
    opens com.dgairbraketoolbox.dgairbraketoolbox.controllers to javafx.fxml;
    exports com.dgairbraketoolbox.dgairbraketoolbox.controllers;
    exports com.dgairbraketoolbox.dgairbraketoolbox.models;

}