package com.dgairbraketoolbox.dgairbraketoolbox.controllers;

import com.dgairbraketoolbox.dgairbraketoolbox.Utils.JavaFXUtils;
import com.dgairbraketoolbox.dgairbraketoolbox.models.Email;
import com.dgairbraketoolbox.dgairbraketoolbox.services.emailservice.EmailHandler;
import com.dgairbraketoolbox.dgairbraketoolbox.services.emailservice.GMailer;
import com.dgairbraketoolbox.dgairbraketoolbox.services.fileservice.SaveQuote;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class EmailSummaryController {

    private Stage parentStage;
    private QuoteFormController quoteFormController;

    @FXML
    private TextField txtFieldTo;

    @FXML
    private TextField txtFieldSubject;

    @FXML
    private TextArea txtAreaMessage;

    private Email currentEmail;



    @FXML
    void goBack(ActionEvent event) {

        Stage thisStage = JavaFXUtils.getCurrentStage(txtAreaMessage);
        thisStage.close();

    }

    @FXML
    void saveDraft(ActionEvent event) {

        saveChanges();

        quoteFormController.getBtnSaveAsPng().setVisible(false);
        quoteFormController.getBtnEmail().setVisible(false);

        WritableImage writableImage = SaveQuote.createQuoteSnapshot(parentStage);
        File file = new File("Quote.png");

        try {

            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            currentEmail.addAttachment(file);

            EmailHandler emailHandler = new GMailer();

            Stage loadingStage = createLoadingStage("Saving Email as Draft...\nPlease Wait");

            // Send Email on a separate thread
            Thread emailThread = new Thread(() -> {
                Platform.runLater(loadingStage::show);
                try {
                    emailHandler.createDraft(currentEmail);
                } catch (MessagingException | GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(loadingStage::close);
            });

            emailThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        quoteFormController.getBtnSaveAsPng().setVisible(true);
        quoteFormController.getBtnEmail().setVisible(true);

        goBack(event);

    }

    @FXML
    void sendDirectly(ActionEvent event) {

        saveChanges();

        quoteFormController.getBtnSaveAsPng().setVisible(false);
        quoteFormController.getBtnEmail().setVisible(false);

        WritableImage writableImage = SaveQuote.createQuoteSnapshot(parentStage);
        File file = new File("Quote.png");

        try {

            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            currentEmail.addAttachment(file);

            EmailHandler emailHandler = new GMailer();

            Stage loadingStage = createLoadingStage("Sending Email...\nPlease Wait");

            // Send email on a separate thread
            Thread emailThread = new Thread(() -> {
                Platform.runLater(loadingStage::show);
                try {
                    emailHandler.sendMessage(currentEmail);
                } catch (MessagingException | GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(loadingStage::close);
            });

            emailThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        quoteFormController.getBtnSaveAsPng().setVisible(true);
        quoteFormController.getBtnEmail().setVisible(true);

        goBack(event);

    }

    private void saveChanges() {

        currentEmail.setToEmailAddress(txtFieldTo.getText());
        currentEmail.setSubject(txtFieldSubject.getText());
        currentEmail.setMessage(txtAreaMessage.getText());

    }

    private Stage createLoadingStage(String loadingMessage) {

        // Creating Loading popup window
        Label label = new Label(loadingMessage);
        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-color: lightgray");
        Scene scene = new Scene(root, 300, 100);
        Stage loadingStage = new Stage();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setScene(scene);
        loadingStage.setResizable(false);
        loadingStage.setOnCloseRequest(Event::consume);
        loadingStage.setTitle("Saving Draft");

        return loadingStage;

    }

    public void setCurrentEmail(Email currentEmail) {
        this.currentEmail = currentEmail;
        txtFieldTo.setText(currentEmail.getToEmailAddress());
        txtFieldSubject.setText(currentEmail.getSubject());
        txtAreaMessage.setText(currentEmail.getMessage());
    }


    public void setParentStage(Stage stage) {
        this.parentStage = stage;
    }

    public void setQuoteFormController(QuoteFormController quoteFormController) {
        this.quoteFormController = quoteFormController;
    }

}
