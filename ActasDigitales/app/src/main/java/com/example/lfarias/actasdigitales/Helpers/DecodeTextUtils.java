package com.example.lfarias.actasdigitales.Helpers;

/**
 * Created by lfarias on 9/11/17.
 */

public class DecodeTextUtils {

    public static String decodeStringText(String undecodedText){
        String str = undecodedText.split(" ")[0];
        str = str.replace("\\","");
        String[] arr = str.split("u");
        String text = "";
        for(int i = 1; i < arr.length; i++){
            int hexVal = Integer.parseInt(arr[i], 16);
            text += (char)hexVal;
        }
        return undecodedText;
    }
}
