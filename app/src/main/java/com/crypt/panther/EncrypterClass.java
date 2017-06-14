package com.crypt.panther;

/**
 * Created by SSHAH on 4/25/2017.
 */
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncrypterClass {
    private static final String key = "Bar12345Bar12345"; // 128 bit key
    private static final String initVector = "RandomInitVector"; // 16 bytes IV

    String retKey(){
        return key;

    }

    String retInitVec(){
        return initVector;
    }

    static String cla = "EncrypterClass";
    synchronized public static byte[] Aencrypt(byte[] encodedBytes) {
        try {
            System.out.println(cla+": inside Aencrpyt class");

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(encodedBytes);

            System.out.println(cla+": returning from Aencrpyt class .. Length: "+encrypted.length);

            return encrypted;
            //return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    synchronized public static byte[] Adecrypt(byte[] encrypted) {
        try {
            //System.out.println("dec : " + Base64.getEncoder().encodeToString(encrypted));
            System.out.println(cla+": inside Aencrpyt class");

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(encrypted);
            System.out.println(cla+": returning  Adencrpyt class the decrypted bytes");

            return original;
            //return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}

