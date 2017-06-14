package com.crypt.panther;

/**
 * Created by SSHAH on 4/25/2017.
 */
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by SSHAH on 4/14/2017.
 */

public class impSteg {
    static String cla ="impsteg";

    private Context context;
    public impSteg(Context context){
        this.context = context;
    }

    elem encSteg(byte[] data) throws IOException {
        steg ob=new steg(context);
        //File f = new File("huffmanencoding.pdf");

        //System.out.println(cla+": calling steganograph");
        elem element  = ob.steganograph(data);

        //System.out.println(cla+": calling Aencrpyt()");
        EncrypterClass ec= new EncrypterClass();
        element.data = EncrypterClass.Aencrypt(element.data);

        //System.out.println(cla+": creating special_music.wav");
        return element;
        //FileOutputStream fos = new FileOutputStream(f2);
        //System.out.println(cla+": writing it to the file");
        //fos.write(b);
        //System.out.println(" Name :"+f2.getName() +" .LEngth : "+f2.length());
        //fos.flush();
        //fos.close();
    }

    File decSteg(File f1, String fileName, int size) throws IOException {

        steg ob = new steg(context);
        //File f1 = new File("special_music.wav");
        //System.out.println(cla+": converting the special_music file to byte.");

        convFile cv = new convFile();
        byte[] b = convFile.fileconvbyte(f1);

        //System.out.println(cla+":decrypting the file special music");
        EncrypterClass ec = new EncrypterClass();
        byte[] s = EncrypterClass.Adecrypt(b);

        //System.out.println(cla+": storing the decypted file to the new file special music_D");
        f1 = convFile.byteconvfile(s, "special_music_D.wav");


        //System.out.println(cla+": calling desteganograph and storing it in huffmanencoding2.pdf");
        File m = ob.desteganograph(f1, fileName+".jpg" , size);

        return m;
    }
}

