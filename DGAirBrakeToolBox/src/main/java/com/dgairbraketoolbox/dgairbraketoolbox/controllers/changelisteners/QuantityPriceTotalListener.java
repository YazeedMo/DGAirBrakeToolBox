package com.dgairbraketoolbox.dgairbraketoolbox.controllers.changelisteners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class QuantityPriceTotalListener implements ChangeListener<String> {

    private final TextField quantityField;
    private final TextField priceField;
    private final TextField totalField;

    public QuantityPriceTotalListener(TextField quantityField, TextField priceField, TextField totalField) {
        this.quantityField = quantityField;
        this.priceField = priceField;
        this.totalField = totalField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
        try {
            BigDecimal quantity = new BigDecimal(quantityField.getText());
            BigDecimal price = new BigDecimal(priceField.getText());
            BigDecimal total = quantity.multiply(price);
            totalField.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            totalField.setText("0");
        }
    }
}
