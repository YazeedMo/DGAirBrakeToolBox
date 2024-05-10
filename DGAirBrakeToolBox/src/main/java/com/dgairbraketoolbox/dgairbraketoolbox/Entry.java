package com.dgairbraketoolbox.dgairbraketoolbox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Entry extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("QuoteForm-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
