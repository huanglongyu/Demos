package com.example;


import com.byteTransform.TransForm;
import com.staticField.StaticField;
import com.zip.AddChannel;

public class MyClass {

    public static void main(String[] args) {
//        System.out.println("main");
//        System.out.println(Double.MAX_VALUE);
//        System.out.println(Float.MAX_VALUE);
//        System.out.println(Integer.MAX_VALUE);
//        TransForm.setTrashSize((float) 2.7144896E7);
//
//        AddChannel.doChange();
        StaticField a = new StaticField();
        StaticField b = new StaticField();
        System.out.println("before:" + a.field + " " + b.field);
        a.field++;
        System.out.println("after:" + a.field + " " + b.field);
    }
}
