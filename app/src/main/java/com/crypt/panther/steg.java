package com.crypt.panther;

/**
 * Created by SSHAH on 4/25/2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.*;
import java.util.*;

public class steg{

    private Context context;

    public steg(Context context){
        this.context = context;
    }

    private File selectWavFile(int file_size){
        File wav1 = new File("select path from res");
        File wav2 = new File("select path from res");
        File wav3 = new File("select path from res");

        if (file_size < 1024*1024*2) {
            // less than 5 mb
            return wav1;
        } else if (file_size > 1024*1024*2 && file_size < 1024*1024*5) {
            // more than 5 and less than 20
            return wav2;
        } else {
            // more than 20
            return wav3;
        }
    }

    static String cla = "steg";

    // mFile is the file selected by the user...
    elem steganograph(byte msg[]) throws IOException{
        FileInputStream fis = null;
        byte[] newdata = null;

        elem element = new elem();
        convFile obj;
        try{
            //System.out.println(cla+": converting the file- "+ mFile.getName() + " to bytes. Length : "+ mFile.length());
            //obj = new convFile();
            //byte[] msg = obj.fileconvbyte(mFile);
            //System.out.println(cla+": reading the data from file ");
            int size= msg.length;
            element.size = size;
            //File f = selectWavFile(size);

            // f is the wav file...
            //File f = new File("\\res\\raw\\music.wav");
            InputStream ins = context.getResources().openRawResource(R.raw.music);
            //fis = (FileInputStream) ins;
            byte[] filedata = new byte[ins.available()];
            Log.d("SIZE",String.valueOf(ins.available()-44));
            ins.read(filedata);
            //System.out.println("Name :  "+f.getName()+".. Length: " +f.length());

            byte[] a = {(byte)0b00000001, (byte)0b00000010, (byte)0b00000100, (byte)0b00001000, (byte)0b00010000, (byte)0b00100000,(byte)0b01000000, (byte)0b10000000};

            // taking message to hide in audio file as user input and converting it to byte array
            // converts the selected file to bytes
            byte[] header = new byte[44];
            System.arraycopy(filedata, 0, header, 0, 44);
            byte[] musicdata = new byte[filedata.length-44];
            System.arraycopy(filedata, 44, musicdata, 0, filedata.length-44);

            // LSB steganography
            for(int j=0; j<msg.length; j++){
                for(int i=0; i<8; i++){
                    musicdata[8*j+i] &= 0b11111110;
                    musicdata[8*j+i] |= (byte) (((byte) (msg[j] & a[i])) >> (i));
                }
            }
            
            System.out.println(cla+": data has been steganographed ");
            newdata = new byte[filedata.length];
            System.arraycopy(header, 0, newdata, 0, 44);
            System.arraycopy(musicdata, 0, newdata, 44, filedata.length-44);
            element.data = newdata;
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        // System.out.println(cla+": returning the steganographed bytes .. length : " +newdata.length +"SIze: "+size);
        return element;
    }

    File desteganograph(File steg_file, String pathToFile, int size) throws IOException{
        FileInputStream fis = null;
        //String message = null;
        String path = pathToFile;
        convFile obj = new convFile();
        File fileToReturn= null;

        try{
            //System.out.println(cla+": desteganograping the file Name : "+steg_file.getName()+"_ Length :"+steg_file.length()+" size :"+size);
            //desteganograph the wav file data..
            fis = new FileInputStream(steg_file);
            byte[] filedata = new byte[(int)(steg_file.length())];
            byte[] musicdata = new byte[(int)(steg_file.length()-44)];
            byte[] mes = new byte[size*8];

            fis.read(filedata);
            System.arraycopy(filedata, 44, musicdata, 0, (int)(steg_file.length()-44));

            for(int j=0; j<(size*8); j++){
                mes[(j/8)] |= (byte)(((byte)(musicdata[j] & (0b00000001)))<<(j%8));
            }

            // reconstruct bytes to file from desteganographed wav file data.
            //System.out.println(cla+": reconstructing the file after desteging .. calling byteconvfile() ..Length: "+mes.length);
            fileToReturn = obj.byteconvfile(mes, path);
            //System.out.println(cla+ ": Name: "+fileToReturn.getName()+" .. Length:"+fileToReturn.length());
            //message = new String(mes);
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            if (fis != null) {
                fis.close();
            }
        }

        //System.out.println(cla+": returing the deteganographed file Name: "+fileToReturn.getName()+"..Length :"+fileToReturn.length());
        //return to constructed file..
        return fileToReturn;
    }

}
