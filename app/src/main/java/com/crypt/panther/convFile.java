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

        //System.out.println(cla+": inside fileconvbyte with the " + mfile.getName() +"length :"+ mfile.length());

        FileInputStream fis = new FileInputStream(mfile);
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                //System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        finally{
            fis.close();
        }
        //System.out.println(cla+": returing the bytes of file :" + mfile.getName()+"length: "+ mfile.length());
        return bos.toByteArray();


    }

    public static File byteconvfile(byte[] mbytes, String path) throws IOException {
        //below is the different part

        //System.out.println(cla + ": converting the bytes to file and file passed :" + path);
        String pathToFile = path;       // wav file is passed from steg
        File someFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), path);
        //File someFile = new File(pathToFile);
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(mbytes);
        fos.flush();
        fos.close();
        //System.out.println(cla+": returing the file from byteconvfile. Name: " + someFile.getName() +"Length:" + someFile.length());
        return someFile;
    }

}

