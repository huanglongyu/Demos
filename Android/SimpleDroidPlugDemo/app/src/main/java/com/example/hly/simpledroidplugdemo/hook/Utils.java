package com.example.hly.simpledroidplugdemo.hook;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hly on 9/20/16.
 */
public class Utils {
    public static void copyFile(String src, String dst) throws IOException {
        BufferedInputStream in = null;
        BufferedOutputStream ou = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src));
            ou = new BufferedOutputStream(new FileOutputStream(dst));
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                ou.write(buffer, 0, read);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }

            if (ou != null) {
                try {
                    ou.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
