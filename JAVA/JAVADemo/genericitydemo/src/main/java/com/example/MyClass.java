package com.example;



public class MyClass {

    public static void main(String[] args) {
        System.out.printf("main");
    }

//    public <K,V> void f(K k,V v) {
//        System.out.println(k.getClass().getSimpleName());
//        System.out.println(v.getClass().getSimpleName());
//    }

    public class Hodler<Z> {
        private Z z;
    }

}
