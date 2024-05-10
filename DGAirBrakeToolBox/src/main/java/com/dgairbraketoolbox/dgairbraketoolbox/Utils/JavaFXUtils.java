package com.dgairbraketoolbox.dgairbraketoolbox.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

public class JavaFXUtils {

    // Enum to store fxml paths
    public enum FXMLPath {

        QUOTE_FORM("/com/dgairbraketoolbox/dgairbraketoolbox/QuoteForm-view.fxml"),
        EMAIL_SUMMARY("/com/dgairbraketoolbox/dgairbraketoolbox/EmailSummary-view.fxml");

        private final String path;

        FXMLPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }

    }

    // Stack to store previous Scenes
    private static final Stack<Scene> sceneHistory = new Stack<>();

    public static Stage getCurrentStage (Node node) {
        return (Stage) node.getScene().getWindow();
    }

    // Method to show next scene that will be stored on the sceneHistory stack
    public static void showNextScene(Node node, String fxmlPath) {

        try {
            sceneHistory.push(getCurrentStage(node).getScene());
            Parent root = FXMLLoader.load(Objects.requireNonNull(JavaFXUtils.class.getResource(fxmlPath)));
            Scene scene = new Scene(root);
            getCurrentStage(node).setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Method to change to scene that should not be stored on the sceneHistory stack
    public static void changeScene(Node node, String fxmlPath) {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(JavaFXUtils.class.getResource(fxmlPath)));
            Scene scene = new Scene(root);
            getCurrentStage(node).setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Method to show previous scene and remove current scene from sceneHistory stack
    public static void showPreviousScene (Node node) {
        getCurrentStage(node).setScene(sceneHistory.pop());
    }

    // Show new window that disables previous window
    public static void showPopup(Stage stage, String fxmlPath) {

        // Create new window
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(JavaFXUtils.class.getResource(fxmlPath)));
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
