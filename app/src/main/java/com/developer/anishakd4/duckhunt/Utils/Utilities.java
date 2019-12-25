package com.developer.anishakd4.duckhunt.Utils;

import java.util.Random;

public class Utilities {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static int getRandomNumberInt(int bound) {

        Random r = new Random();
        return r.nextInt(bound);
    }

}
