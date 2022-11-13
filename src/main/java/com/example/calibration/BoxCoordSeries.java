package com.example.calibration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class BoxCoordSeries {
    public LinkedHashMap<Float, Float> getBoxCoord(String path) {
        //количество отображаемых точек на графике уменьшим до 1200 чтобы график не тормозил при обработке
        //большого массива данных. Создадим 1200 боксов и найдем в них точки с max и min значениями
        //с помощью TreeMap, которые и отобразим на графике.
        int width = 1200;
        float time;
        float value;
        LinkedHashMap<Float, Float> mapData = new LinkedHashMap<>();
        TreeMap<Float, Float> dataTree = new TreeMap<>();
        FileInputStream fisDataFile = null;
        try {
            fisDataFile = new FileInputStream(path);
            int lenData = fisDataFile.available();
            byte[] arrData = new byte[lenData];
            fisDataFile.read(arrData);
            String stringData = new String(arrData);
            String[] strData = stringData.split("\n");

                            /* //Отображение всех точек на графике займет очень много времени
                for (String unit :
                        strData) {
                    String[] str = unit.split("\t");
                    time = Float.parseFloat(str[0]);
                    value = Float.parseFloat(str[1]);
                    series.getData().add(new XYChart.Data<>(time,value));
                }   */

            //определим количество точек в боксе
            int box = strData.length / width;
            for (int i = 0; i < strData.length; i += box) {
                for (int j = 0; j <= box; j++) {
                    if ((i + j) < strData.length) {
                        String[] str = strData[i + j].split("\t");
                        time = Float.parseFloat(str[0]);
                        value = Float.parseFloat(str[1]);
                        //в TreeMap добавили в качестве ключа значения параметра, а не время
                        dataTree.put(value, time);
                    }
                }
                //добавляем в график min и max значения
                mapData.put(dataTree.get(dataTree.firstKey()), dataTree.firstKey());
                mapData.put(dataTree.get(dataTree.lastKey()), dataTree.lastKey());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapData;
    }
}
