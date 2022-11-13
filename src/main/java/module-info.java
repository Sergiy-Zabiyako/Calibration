module com.example.tarirovka {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calibration to javafx.fxml;
    exports com.example.calibration;
}