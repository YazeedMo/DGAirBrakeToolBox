package com.dgairbraketoolbox.dgairbraketoolbox.services.fileservice;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SaveQuote {

    public static void saveAsPng(Stage stage) {

        try {
            // Create a writable image of the scene
            WritableImage writableImage = new WritableImage((int) stage.getWidth(), (int) stage.getHeight() - 50);
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.getScene().snapshot(writableImage);

            // Save the image to a file
            File file = new File("screenshot.png");
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
