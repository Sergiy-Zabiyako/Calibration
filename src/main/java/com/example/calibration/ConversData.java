package com.example.calibration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

public class ConversData{

    FileOutputStream fos;
    public LinkedHashMap<Float,Float> getConversData(LinkedHashMap<Float,Float> xyBinary, float a, float b, float c, String path){
        LinkedHashMap<Float,Float> xyValue = new LinkedHashMap<>();
        try {

            fos = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //вычисление новых данных по тарировке и вывод в карту и файл
        xyBinary.forEach((key,value) -> {
            // y = a*x^2 + b*x + c
            value = a * value * value + b * value + c;
            xyValue.put(key,value);
            String strWrite = new String(key+"\t"+value+"\n");
            try {
                fos.write(strWrite.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return xyValue;
    }
}
