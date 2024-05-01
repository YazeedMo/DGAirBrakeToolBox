module com.dgairbraketoolbox.dgairbraketoolbox {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.dgairbraketoolbox.dgairbraketoolbox to javafx.fxml;
    exports com.dgairbraketoolbox.dgairbraketoolbox;
    opens com.dgairbraketoolbox.dgairbraketoolbox.controllers to javafx.fxml;
    exports com.dgairbraketoolbox.dgairbraketoolbox.controllers;
}