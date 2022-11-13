package com.example.calibration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox vb;


    @FXML
    private TextField a;

    @FXML
    private TextField b;

    @FXML
    private TextField c;
    @FXML
    private Button butChoiseFile;

    @FXML
    private Text patchCalibration;
    @FXML
    private Button butProcess;
    private Window stage;
    private String path;
    @FXML
    private Text equantion;
    @FXML
    private Button butSolution;
    @FXML
    private Text patchFile;
    private LinkedHashMap<Float, Float> xyBinary;
    private LinkedHashMap<Float, Float> xyValue;
    @FXML
    private LineChart<Number, Number> chartCalibr;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    @FXML
    private Button butFileNewData;
    @FXML
    private Text pathNewData;

    @FXML
    void initialize() {
        butChoiseFile.setOnAction(actionEvent -> {
            //выбираем файл с данными для преобразования по тарировочной характеристики
            FileChooser fc = new FileChooser();
            fc.setTitle("Opening the location");
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                path = file.getAbsolutePath();
                patchFile.setText(path);
            }
            //читаем файл и строим график
            XYChart.Series series = new XYChart.Series();
            series.setName("parameter");
            xAxis.setLabel("Time, s");
            yAxis.setLabel("Value");
            chartCalibr.setTitle("Data Chart");
            chartCalibr.setCreateSymbols(false);
            xAxis.setAutoRanging(false);
            xAxis.setLowerBound(-300);
            xAxis.setUpperBound(200);
            xAxis.setTickUnit(50);
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(25);
            yAxis.setUpperBound(35);
            yAxis.setTickUnit(0.50);
            BoxCoordSeries boxCoordSeries = new BoxCoordSeries();
            LinkedHashMap<Float, Float> mapData = boxCoordSeries.getBoxCoord(path);
            mapData.forEach((key, value) -> {
                series.getData().add(new XYChart.Data<>(key, value));
            });
            chartCalibr.getData().add(series);
        });

        butProcess.setOnAction(actionEvent -> {
            //Задаем имя файла и директорию для сохранение новых данных, обработанных по тарировке
            String pathNewDataFile = "C:\\Users\\sergi\\---Java---\\-Example_Intellij_Ultimate-\\tarirovka\\src\\main\\resources\\com\\example\\calibration\\DataNewFile.txt";
            Task<Void> task1 = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (path != null) {
                        ParserFile parserFile = new ParserFile();
                        xyBinary = parserFile.getBinaryFile(path);
                        System.out.println(xyBinary);

                        float af = Float.parseFloat(a.getText());
                        float bf = Float.parseFloat(b.getText());
                        float cf = Float.parseFloat(c.getText());
                        ConversData conversData = new ConversData();
                        xyValue = conversData.getConversData(xyBinary, af, bf, cf, pathNewDataFile);
                        System.out.println(xyValue);
                        pathNewData.setText(pathNewDataFile);
                    }
                    return null;
                }
            };
            new Thread(task1).start();
        });

        butSolution.setOnAction(actionEvent -> {
            //находим файл с тарировочной характеристикой
            FileChooser fcCalibration = new FileChooser();
            fcCalibration.setTitle("Open File with calibration");
            File file = fcCalibration.showOpenDialog(stage);
            if (file != null) {
                String pathCalibration = file.getAbsolutePath();
                patchCalibration.setText(pathCalibration);
            }
            //парсим файл и тарировочные точки записываем в карту с сохранением последовательности
            ParseFileCalibration parserFileCalibr = new ParseFileCalibration();
            LinkedHashMap<Float, Float> mapCalibration = parserFileCalibr.getFileCalibr(patchCalibration.getText());
            //на основании точек тарировки определяем полином первой степени для интерполяции
            PolinomFirstDegree polinomFirstDegree = new PolinomFirstDegree();
            ArrayList<Float> coeffPolinom = polinomFirstDegree.getLagrange(mapCalibration);
            String str = "y = " + coeffPolinom.get(0) + "*x + " + coeffPolinom.get(1);
            equantion.setText(str);
            a.setText("0");
            b.setText(String.valueOf(coeffPolinom.get(0)));
            c.setText(String.valueOf(coeffPolinom.get(1)));
            //строим график тарировочных точек и уравнение интерполяции
            XYChart.Series series1 = new XYChart.Series();
            XYChart.Series series2 = new XYChart.Series();
            xAxis.setLabel("X value");
            yAxis.setLabel("Y value");
            chartCalibr.setTitle("Calibration");
            xAxis.setAutoRanging(true);
            yAxis.setAutoRanging(true);
            ArrayList<Float> xCoord = new ArrayList<>();
            ArrayList<Float> yCoord = new ArrayList<>();
            mapCalibration.forEach((key, value) -> {
                xCoord.add(key);
                yCoord.add(value);
            });
            for (int i = 0; i < xCoord.size(); i++) {
                series1.getData().add(new XYChart.Data<>(xCoord.get(i), yCoord.get(i)));
                series2.getData().add(new XYChart.Data<>(xCoord.get(i), (xCoord.get(i) * coeffPolinom.get(0) + coeffPolinom.get(1))));
            }
            chartCalibr.getData().clear();
            chartCalibr.getData().add(series1);
            chartCalibr.getData().add(series2);
            series1.setName("Calibration series");
            series2.setName("Polinom");
            chartCalibr.setHorizontalGridLinesVisible(true);
            chartCalibr.setVerticalGridLinesVisible(true);
            chartCalibr.setCreateSymbols(true);
            chartCalibr.setCache(true);
        });

        butFileNewData.setOnAction(actionEvent -> {
            //строим график новых данных после преобразования по тарировочной зависимости
            chartCalibr.getData().clear();
            XYChart.Series series3 = new XYChart.Series();
            series3.setName("New Data Parameter");
            xAxis.setLabel("Time, s");
            yAxis.setLabel("Value");
            chartCalibr.setTitle("New Data Chart");
            chartCalibr.setCreateSymbols(false);
            xAxis.setAutoRanging(false);
            xAxis.setLowerBound(-300);
            xAxis.setUpperBound(200);
            xAxis.setTickUnit(50);
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(250);
            yAxis.setUpperBound(350);
            yAxis.setTickUnit(50);
            LinkedHashMap<Float, Float> mapNewData = new LinkedHashMap<>();
            BoxCoordSeries boxNewCoordSeries = new BoxCoordSeries();
            mapNewData = boxNewCoordSeries.getBoxCoord(pathNewData.getText());
            mapNewData.forEach((key, value) -> {
                series3.getData().add(new XYChart.Data<>(key, value));
            });
            chartCalibr.getData().add(series3);
        });
    }
}
