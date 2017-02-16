package com.example;

import java.util.ArrayList;

public class Sort {

    public static void main(String[] args) {
        int[] list = new int[]{1, 10, 4, 3, 9, 8, 100};
        QuickSort.quicksort(list, 0, list.length -1);
        printList(list);

        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1); list1.add(2); list1.add(3); list1.add(4); list1.add(5);
        ArrayList<Integer> shuffle = Shuffle.shuffle(list1);
        printList(shuffle);
    }

    private static void printList(int[] list) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < list.length; i++) {
            bf.append(list[i] + " ");
        }
        System.out.println(bf.toString());
    }

    private static void printList(ArrayList<Integer> list) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            bf.append(list.get(i) + " ");
        }
        System.out.println(bf.toString());
    }
}
