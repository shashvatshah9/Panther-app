package com.crypt.panther;

/**
 * Created by SSHAH on 4/25/2017.
 */
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class convFile {
    static String cla = "convFile";

    public static byte[] fileconvbyte(File mfile) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(mfile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                // Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            fis.close();
        }

        return bos.toByteArray();
    }

    public static File byteconvfile(byte[] mbytes, String path) throws IOException {
        File someFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), path);
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(mbytes);
        fos.flush();
        fos.close();
        
        return someFile;
    }

}