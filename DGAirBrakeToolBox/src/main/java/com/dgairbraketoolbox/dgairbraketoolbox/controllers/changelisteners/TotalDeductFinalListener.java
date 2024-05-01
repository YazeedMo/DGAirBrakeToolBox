package com.dgairbraketoolbox.dgairbraketoolbox.controllers.changelisteners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class TotalDeductFinalListener implements ChangeListener<String> {

    private final TextField totalField;
    private final TextField deductionsField;
    private final TextField finalAmountField;

    public TotalDeductFinalListener(TextField totalField, TextField deductionsField, TextField finalAmountField) {
        this.totalField = totalField;
        this.deductionsField = deductionsField;
        this.finalAmountField = finalAmountField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

        if (deductionsField.getText().isEmpty() && !deductionsField.isFocused()) {
            deductionsField.setText("0");
        }

        try {
            BigDecimal total = new BigDecimal(totalField.getText());
            BigDecimal deductionsAmount = new BigDecimal(deductionsField.getText());
            BigDecimal finalAmount = total.add(deductionsAmount);
            finalAmountField.setText(String.valueOf(finalAmount));
        } catch (NumberFormatException e) {
            finalAmountField.setText("0");
        }

    }
}
