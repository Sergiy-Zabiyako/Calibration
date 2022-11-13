package com.example.calibration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

public class ParseFileCalibration {
    public LinkedHashMap<Float,Float> getFileCalibr(String path){
        //парсер таририровочного файла
        float dataFirst;
        float dataSecond;
        LinkedHashMap<Float,Float> mapCalibration = new LinkedHashMap<>();
        try {
            FileInputStream fisFileCalibr = new FileInputStream(path);
            int len = fisFileCalibr.available();
            byte [] arr = new byte[len];
            fisFileCalibr.read(arr);
            String str = new String(arr);
            String [] strings = str.split("\n");
            for (String unit :
                    strings) {
                String[] arrStr = unit.split("\t");
                dataFirst = Float.parseFloat(arrStr[0]);
                dataSecond = Float.parseFloat(arrStr[1]);
                mapCalibration.put(dataFirst,dataSecond);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mapCalibration;
    }

}
