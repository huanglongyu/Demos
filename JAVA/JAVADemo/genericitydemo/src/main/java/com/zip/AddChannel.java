package com.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by hly on 1/18/17.
 */

public class AddChannel {

    public static void doChange() {
        File variant = new File("/home/hly/work/final.apk");
        File source = new File("/home/hly/work/normol.apk");

        if (!source.exists()) {
            return;
        }

        try {
            variant.delete();
            variant.createNewFile();

            ZipOutputStream to = new ZipOutputStream(new FileOutputStream(variant));
            int temp;
            ZipFile from = new ZipFile(source);
            Enumeration<? extends ZipEntry> entries = from.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                System.out.println("entry:" + entry.getName());
                InputStream in = from.getInputStream(entry);
                to.putNextEntry(entry);
                while ((temp = in.read()) != -1) {
                    to.write(temp);
                }
            }
            ZipEntry chZipEntry = new ZipEntry("META-INF/CHANNEL");
            to.putNextEntry(chZipEntry);
            to.write("360".getBytes());
            to.closeEntry();
            to.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("AddChannel done!");
    }
}
