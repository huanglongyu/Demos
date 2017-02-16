package com.example;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hly on 2/15/17.
 */

public class Shuffle {
    public static ArrayList shuffle(ArrayList<Integer> list) {
        ArrayList<Integer> after = new ArrayList<>();
        Random random = new Random();

        while (list.size() > 0) {
            int size = list.size();
            int index = random.nextInt(size);
            int value = list.remove(index);
            after.add(value);
        }
        return after;
    }
}
