package ru.tetris.utils;

import java.util.List;

public class Utils {

    public static double roundToSignificant(double val, int points) {
        if (points <= 0 ) {
            return Math.round(val);
        }
        double denominator = 1. / (points * 10);

        System.out.println("val " + val);

        System.out.println("denominator " + denominator);

        double residual = val % denominator;

        System.out.println("residual " + residual);

        return val - residual;

    }

    public static <T> T randomChoice(T[] arr) {
        int i = (int) (Math.random() * arr.length);
        return arr[i];
    }

    public static <T> T randomChoice(List<T> list) {
        int i = (int) (Math.random() * list.size());
        return list.get(i);
    }


}
