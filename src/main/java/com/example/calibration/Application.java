package com.example.calibration;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("calibration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setTitle("Пересчет данных по тарировочной характеристике");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}