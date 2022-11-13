package com.example.calibration;

import java.io.*;
import java.util.LinkedHashMap;

public class ParserFile {
    public LinkedHashMap<Float, Float> getBinaryFile(String path) {
        float time;
        float value;
        LinkedHashMap<Float, Float> xyBinary = new LinkedHashMap<>();
        //парсер файла с данными
        try {
            FileInputStream fis = new FileInputStream(path);

            int len = fis.available();
            byte[] arr = new byte[len];
            fis.read(arr);
            String str = new String(arr);
            String[] strings = str.split("\n");
            for (String unit :
                    strings) {
                String[] data = unit.split("\t");
                time = Float.parseFloat(data[0]);
                value = Float.valueOf(data[1]);
                xyBinary.put(time, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return xyBinary;
    }
}
