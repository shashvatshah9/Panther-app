package com.crypt.panther;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SSHAH on 4/25/2017.
 */

public class Uploader {

    private Context context;

    public Uploader(Context context){
        this.context = context;
    }

    public elem Upload(byte[] msg) throws IOException {
         impSteg obj = new impSteg(context);
         return obj.encSteg(msg);
    }

    public File downloader(File file, String fileName, int size) throws IOException {
        impSteg obj = new impSteg(context);
        File myFile = null;
        
        try {
            myFile= obj.decSteg(file, fileName, size);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myFile;
    }
}
