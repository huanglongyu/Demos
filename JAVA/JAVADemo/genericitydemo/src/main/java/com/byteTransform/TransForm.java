package com.byteTransform;

/**
 * Created by hly on 2/8/17.
 */

public class TransForm {
    public static final long KB_INT = 1024;
    public static final float KB_FLOAT = 1024f;
    public static final long MB_INT = KB_INT * KB_INT;
    public static final float MB_FLOAT = KB_FLOAT * KB_FLOAT;
    public static final long GB_INT = KB_INT * KB_INT * KB_INT;
    public static final float GB_FLOAT = KB_FLOAT * KB_FLOAT * KB_FLOAT;


    public static float byteToKBExact(float bytes) {
        return bytes / KB_FLOAT;
    }

    public static float byteToMBExact(float bytes) {
        return bytes / MB_FLOAT;
    }

    public static String formatNum(float value, String format) {
        String result = String.format(format, value);
        return result;
    }

    public static float byteToGBExact(float bytes) {
        return bytes / GB_FLOAT;
    }

    public static void setTrashSize(float size) {
        String text;
        String unit;
        if (size < KB_INT) {
            if (size > 0) {
                text = "1";
            } else {
                text = "0";
            }
            unit = "KB";
        } else if (size < MB_INT) {

            float value = byteToKBExact(size);
            value += 0.9;
            text = ((long) value) + "";
            unit = "KB";

        } else if (size < GB_INT) {
            float value = byteToMBExact(size);
            if (value < 10) {
                text = formatNum(value, "%.2f");
            } else if (value < 100) {
                text = formatNum(value, "%.1f");
            } else {
                value += 0.9;
                text = ((long) value) + "";
            }
            unit = "MB";
        } else {
            float value = byteToGBExact(size);

            if (value < 10) {
                text = formatNum(value, "%.2f");
            } else {
                text = formatNum(value, "%.1f");
            }
            unit = "GB";
        }
        System.out.println(text + "" + unit);
    }
}
