package com.mple.seriestracker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Base64;

public class Base64Util {

    //Responsible for handling decoding

    public static String decodeString(String encodedString){
        return new String(Base64.getDecoder().decode(encodedString));
    }

    public static byte[] decodeBytes(String encodedString){
        return Base64.getDecoder().decode(encodedString);
    }

    public static String encodeString(String dataToEncode){
        return new String(Base64.getEncoder().encode(dataToEncode.getBytes()));
    }

    public static Bitmap decodeImage(String base64ImageString){
        try{
            byte[] bytes = decodeBytes(base64ImageString);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }catch (Exception e){}
        return null;
    }

}
