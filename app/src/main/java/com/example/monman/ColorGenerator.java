package com.example.monman;

import android.graphics.Color;
import java.util.Random;

public class ColorGenerator {
    private static final Random random = new Random();

    public static int getRandomColor() {
        // Generate random RGB values
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        // Create Color object with random RGB values
        return Color.rgb(r, g, b);
    }
}
