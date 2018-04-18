package com.example.tarun.assigment6final;


public class Abc {
    public static String getFileNameFromURL(String url) {
        if(url == null)
            return null;
        else

            return url.substring(url.lastIndexOf('/')+1, url.length());
    }
}

