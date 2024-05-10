package com.dgairbraketoolbox.dgairbraketoolbox.services.fileservice;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SaveQuote {

    public static void choosePngSaveLocation(Stage stage) {

        try {

            // Create a writable image of the scene
            WritableImage writableImage = createQuoteSnapshot(stage);

            // Create a file choose
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Invoice as PNG");

            // Set initial directory (optional)
            String defaultFolder = System.getProperty("user.home") + File.separator + "Pictures";
            fileChooser.setInitialDirectory(new File(defaultFolder));

            // Set extension filter (optional)
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extensionFilter);

            // Set the default file as the initial selection
            fileChooser.setInitialFileName("InvoiceScreenshot.png");

            // Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            // Save the image to the selected file
            if (file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static WritableImage createQuoteSnapshot(Stage stage) {

        WritableImage writableImage = new WritableImage((int) stage.getWidth(), (int) stage.getHeight() - 50);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        stage.getScene().snapshot(writableImage);

        return writableImage;

    }

}
