package com.cotemig.fluo.helper;

import com.cotemig.fluo.app.FluoApp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    public static void unzip(String fileName, String location) throws Exception {

        final int BUFFER = 1024;

        BufferedOutputStream dest = null;

        FileInputStream fis = new FileInputStream(fileName);

        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

        ZipEntry entry;

        boolean bar = location.endsWith("/");

        while ((entry = zis.getNextEntry()) != null) {

            String entryName = entry.getName();

            if (entry.isDirectory()) {

                createDir(location + (!bar ? "/" : "")
                        + entryName);

            } else {

                int count;
                byte data[] = new byte[BUFFER];

// File file = new File(location, entryName.substring(0, entryName.lastIndexOf('/')));
// file.mkdirs();

                FileOutputStream fos = new FileOutputStream(location
                        + (!bar ? "/" : "") + entryName);

                dest = new BufferedOutputStream(fos, BUFFER);

                while ((count = zis.read(data, 0, BUFFER)) != -1) {

                    dest.write(data, 0, count);
                }

                dest.flush();
                dest.close();
            }
        }

        zis.close();
    }


    public static void zip(String fileName, String fileZipped) throws Exception {

        byte[] buffer = new byte[1024];

        FileOutputStream fos = new FileOutputStream(fileZipped);
        ZipOutputStream zos = new ZipOutputStream(fos);
        ZipEntry ze = new ZipEntry(FluoApp.Companion.getDATABASE_UPDATE());
        zos.putNextEntry(ze);
        FileInputStream in = new FileInputStream(fileName);

        int len;
        while ((len = in.read(buffer)) > 0) {
            zos.write(buffer, 0, len);
        }

        in.close();
        zos.closeEntry();

//remember close it
        zos.close();

        System.out.println("Done");
    }

    public static File createDir(String dirName) {
        File dir = new File(dirName);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }
}
