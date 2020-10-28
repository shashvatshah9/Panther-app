package com.crypt.panther;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by lovlin-thakkar on 4/25/2017.
 */

public class Compressor {

    public elem compressData(byte[] data) throws Exception {
        byte[] output;
        elem element = new elem();

        // Encode a String into bytes
        byte[] input = data;

        // Compress the bytes
        output = new byte[data.length];

        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        int compressedDataLength = compresser.deflate(output);
        compresser.end();

        element.data = output;
        element.size = compressedDataLength;
        // Decompress the bytes

        // Decode the bytes into a String
        //String outputString = new String(result, 0, resultLength, "UTF-8");

        return element;
    }

    public byte[] decompressData(byte[] output, int compressedDataLength) throws Exception{
        Inflater decompresser = new Inflater();
        decompresser.setInput(output, 0, compressedDataLength);

        byte[] result = new byte[100];
        int resultLength = decompresser.inflate(result);
        decompresser.end();

        return result;
    }

}
