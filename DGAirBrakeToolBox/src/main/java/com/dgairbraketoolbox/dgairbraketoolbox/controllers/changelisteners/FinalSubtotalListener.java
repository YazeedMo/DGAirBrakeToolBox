package com.dgairbraketoolbox.dgairbraketoolbox.controllers.changelisteners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FinalSubtotalListener implements ChangeListener<String> {

    private final ArrayList<TextField> finalTextFields;
    private final TextField totalField;

    public FinalSubtotalListener(ArrayList<TextField> finalTextFields, TextField totalField) {
        this.finalTextFields = finalTextFields;
        this.totalField = totalField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
        try {
            BigDecimal total = new BigDecimal(0);
            for (TextField textField : finalTextFields) {
                BigDecimal valueToAdd = new BigDecimal(textField.getText());
                total = total.add(valueToAdd);
            }
            totalField.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            totalField.setText("0");
        }
    }
}
