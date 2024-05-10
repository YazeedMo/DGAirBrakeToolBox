package com.dgairbraketoolbox.dgairbraketoolbox.controllers;

import com.dgairbraketoolbox.dgairbraketoolbox.Entry;
import com.dgairbraketoolbox.dgairbraketoolbox.Utils.JavaFXUtils;
import com.dgairbraketoolbox.dgairbraketoolbox.controllers.changelisteners.FinalSubtotalListener;
import com.dgairbraketoolbox.dgairbraketoolbox.controllers.changelisteners.QuantityPriceTotalListener;
import com.dgairbraketoolbox.dgairbraketoolbox.controllers.changelisteners.TotalDeductFinalListener;
import com.dgairbraketoolbox.dgairbraketoolbox.models.Email;
import com.dgairbraketoolbox.dgairbraketoolbox.services.fileservice.SaveQuote;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuoteFormController {

    private Stage thisStage;
    private Email currentEmail;

    @FXML
    private GridPane tblA1;

    @FXML
    private GridPane tblA2;

    @FXML
    private GridPane tblB;

    @FXML
    private TextField txtFieldDesc;

    @FXML
    private GridPane tblC;

    @FXML
    private GridPane tblD;

    @FXML
    private Button btnSaveAsPng;

    @FXML
    private Button btnEmail;


    @FXML
    public void initialize() {

        currentEmail = new Email();

        initTblA1();
        initTblA2();
        initTblB();
        txtFieldDesc.setText("");
        txtFieldDesc.setPromptText("Job Description");
        initTblC();
        initTblD();

    }

    private void initTblA1() {

        tblA1.setStyle("-fx-grid-lines-visible: false");

    }

    private void initTblA2() {

        tblA2.setStyle("-fx-grid-lines-visible: false");

    }

    private void initTblB() {

        tblB.getChildren().clear();

        // Add column names
        List<Label> labels = new ArrayList<>(Arrays.asList(
                new Label("BBEE\nSTATUS"),
                new Label("VEHICLE MAKE"),
                new Label("QUOTE\nNUMBER"),
                new Label("PAYMENT TERMS"),
                new Label("NOTIFICATION"),
                new Label("REG NO"),
                new Label("WO NO"),
                new Label("COMPANY\nREG NO")
        ));

        // Add style class to all Labels
        for (Label label : labels) {
            label.getStyleClass().add("tblHeadings");
        }

        // Add Table heading Labels to tblMiddle
        for (int colNum = 0; colNum < tblB.getColumnCount(); colNum++) {
            tblB.add(labels.get(colNum), colNum, 0);
        }

        // Add Text Fields to tblB
        for (int rowNum = 1; rowNum < tblB.getRowCount(); rowNum++) {
            for (int colNum = 0; colNum < tblB.getColumnCount(); colNum++) {
                TextField textField = new TextField();
                textField.getStyleClass().add("tblFields");
                tblB.add(textField, colNum, rowNum);
            }
        }
    }

    private void initTblC() {

        tblC.setStyle("-fx-grid-lines-visible: false");

        tblC.getChildren().clear();

        // Add Labels to tblC
        List<Label> labels = new ArrayList<>(Arrays.asList(
                new Label("QUANTITY"),
                new Label("PART NO"),
                new Label("UNIT"),
                new Label("PART NAME"),
                new Label("UNIT PRICE"),
                new Label("TOTAL"),
                new Label("ADD/\nDEDUCT"),
                new Label("FINAL AMOUNT")
        ));

        // Add tblHeadings css class to all Labels
        for (Label label : labels) {
            label.getStyleClass().add("tblHeadings");
        }

        // Add Labels to tblC
        for (int colNum = 0; colNum < tblC.getColumnCount(); colNum++) {
            tblC.add(labels.get(colNum), colNum, 0);
        }

        // Add Text Fields to tblC
        for (int rowNum = 1; rowNum < tblC.getRowCount(); rowNum++) {
            for (int colNum = 0; colNum < tblC.getColumnCount(); colNum++) {
                TextField textField = new TextField();
                textField.getStyleClass().add("tblFields");
                tblC.add(textField, colNum, rowNum);
            }
        }

        // Add listener to Quantity, Unit price, and Total column
        for (int rowNum = 1; rowNum < tblC.getRowCount(); rowNum++) {
            TextField quantity = (TextField) getNodeByColumnRowIndex(0, rowNum, tblC);
            TextField price = (TextField) getNodeByColumnRowIndex(4, rowNum, tblC);
            TextField total = (TextField) getNodeByColumnRowIndex(5, rowNum, tblC);
            total.setDisable(true);
            total.setStyle("-fx-opacity: 1;"); // Override disabled styling
            total.setText("0");

            quantity.textProperty().addListener(new QuantityPriceTotalListener(quantity, price, total));
            price.textProperty().addListener(new QuantityPriceTotalListener(quantity, price, total));

        }

        // Add listener to Total, Deductions, and Final Amount columns
        for (int rowNum = 1; rowNum < tblC.getRowCount(); rowNum++) {
            TextField total = (TextField) getNodeByColumnRowIndex(5, rowNum, tblC);
            TextField deductions = (TextField) getNodeByColumnRowIndex(6, rowNum, tblC);
            TextField finalAmount = (TextField) getNodeByColumnRowIndex(7, rowNum, tblC);

            deductions.setText("0");

            finalAmount.setDisable(true);
            finalAmount.setStyle("-fx-opacity: 1");
            finalAmount.setText("0");

            total.textProperty().addListener(new TotalDeductFinalListener(total, deductions, finalAmount));
            deductions.textProperty().addListener(new TotalDeductFinalListener(total, deductions, finalAmount));

        }

    }

    private void initTblD() {

        tblD.setStyle("-fx-grid-lines-visible: false");

        tblD.getChildren().clear();

        // Get width of last column in tblC
        double lastColumnWidth = tblC.getColumnConstraints().get(tblC.getColumnCount() - 1).getPrefWidth();
        tblD.getColumnConstraints().get(1).setPrefWidth(lastColumnWidth);

        // Get width of second last column in tblC
        double firstColumWidth = tblC.getColumnConstraints().get(tblC.getColumnCount() - 2).getPrefWidth();
        tblD.getColumnConstraints().get(0).setPrefWidth(firstColumWidth + 2.5);

        // Add Text fields to tblD
        for (int colNum = 0; colNum < tblD.getColumnCount(); colNum++) {
            for (int rowNum = 0; rowNum < tblD.getRowCount(); rowNum++) {
                if (colNum == 0 && rowNum == 0 || rowNum == (tblD.getRowCount() - 1)) continue;
                TextField textField = new TextField();
                textField.getStyleClass().add("tblFields");
                tblD.add(textField, colNum, rowNum);
            }
        }

        // Add Labels to tblD
        Label subtotalLabel = new Label("Subtotal:");
        subtotalLabel.getStyleClass().add("tblHeadings");
        tblD.add(subtotalLabel, 0, 0);

        Label balanceDueLabel = new Label("BALANCE\nDUE:");
        balanceDueLabel.getStyleClass().add("tblHeadings");
        tblD.add(balanceDueLabel, 0, tblD.getRowCount() - 1);

        Label balanceDueAmount = new Label("");
        balanceDueAmount.getStyleClass().add("tblHeadings");
        tblD.add(balanceDueAmount, 1, tblD.getRowCount() - 1);

        // Add listener to Total Amount and Subtotal column
        TextField subtotalField = (TextField) getNodeByColumnRowIndex(1, 0, tblD);
        subtotalField.setText("0");
        subtotalField.setDisable(true);
        subtotalField.setStyle("-fx-opacity: 1");

        // Get all total text fields
        ArrayList<TextField> totalTextFields = new ArrayList<>();
        for (int rowNum = 1; rowNum < tblC.getRowCount(); rowNum++) {
            TextField textField = (TextField) getNodeByColumnRowIndex(7, rowNum, tblC);
            totalTextFields.add(textField);
        }

        FinalSubtotalListener finalSubtotalListener = new FinalSubtotalListener(totalTextFields, subtotalField);

        // Add FinalSubTotalListener to all Final TextFields
        for (TextField textField : totalTextFields) {
            textField.textProperty().addListener(finalSubtotalListener);
        }

        // TODO Understand tblD purpose
        // Temporarily setting up Balance Due Label
        Label balanceDue = (Label) getNodeByColumnRowIndex(1, 4, tblD);
        balanceDue.setText("0");
        subtotalField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                balanceDue.setText(t1);
            }
        });

    }

    private Node getNodeByColumnRowIndex(int column, int row, GridPane gridPane) {

        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }


    @FXML
    void saveAsPng(ActionEvent event) {

        thisStage = JavaFXUtils.getCurrentStage(btnEmail);

        btnSaveAsPng.setVisible(false);
        btnEmail.setVisible(false);

        Stage stage = Entry.mainStage;

        SaveQuote.choosePngSaveLocation(stage);

        btnSaveAsPng.setVisible(true);
        btnEmail.setVisible(true);

    }

    @FXML
    void email(ActionEvent event) throws IOException {

        // get this stage
        thisStage = JavaFXUtils.getCurrentStage(btnEmail);

        // Set current Email
        currentEmail.setFromEmailAddress("dgairbraketest@gmail.com");
        currentEmail.setToEmailAddress("monkeymo0810@gmail.com");
        currentEmail.setSubject(txtFieldDesc.getText());
        currentEmail.setMessage("Quote attached");

        // Load view, root, and scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(JavaFXUtils.FXMLPath.EMAIL_SUMMARY.getPath()));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Get controller class and pass data to that view's controller
        EmailSummaryController summaryController = loader.getController();
        summaryController.setCurrentEmail(currentEmail);
        summaryController.setParentStage(thisStage);
        summaryController.setQuoteFormController(this);

        // Setup and show stage
        Stage emailSummaryStage = new Stage();
        emailSummaryStage.initModality(Modality.APPLICATION_MODAL);
        emailSummaryStage.initOwner(thisStage);

        emailSummaryStage.setScene(scene);
        emailSummaryStage.showAndWait();

    }

    public Button getBtnSaveAsPng() {
        return btnSaveAsPng;
    }

    public Button getBtnEmail() {
        return btnEmail;
    }
}
