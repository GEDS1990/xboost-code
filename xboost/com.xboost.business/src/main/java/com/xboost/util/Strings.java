package com.xboost.util;

import java.io.UnsupportedEncodingException;

public class Strings {

    public static String toUTF8(String str) {
        try {
            return new String(str.getBytes("ISO8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean isEmpty(Object obj) {
       String str = (obj==null ? "":obj.toString());
        if (str == null || str == "" || str == " " || str.length()<=0) {
            return true;
        } else {
            return false;
        }
    }
}
