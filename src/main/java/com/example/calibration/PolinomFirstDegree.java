package com.example.calibration;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PolinomFirstDegree {
    int k = 0;
    public ArrayList<Float> getLagrange (LinkedHashMap<Float,Float> mapCalibration){
        //вычисление коэффициентов полинома первой степени
        int n = mapCalibration.size();
        float[] arrX = new float[n];
        float[] arrY = new  float[n];
        float sumX=0;
        float sumY=0;
        float sumX2=0;
        float sumXY=0;
        float a;
        float b;
        ArrayList<Float> coefficient = new ArrayList<>();
        mapCalibration.forEach((key,value)->{
            arrX [k] = key;
            arrY [k] = value;
            k++;
        });
        for (int i = 0; i < n; i++) {
            sumX += arrX[i];
            sumY += arrY[i];
            sumX2 += arrX[i]*arrX[i];
            sumXY += arrX[i]*arrY[i];
        }
        a = (n*sumXY-sumX*sumY)/(n*sumX2-sumX*sumX);
        b = (sumY-a*sumX)/n;
        coefficient.add(a);
        coefficient.add(b);
        System.out.println("y = "+a+"*x"+"+"+b);
        return coefficient;
    }
}
