package com.xboost.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Strings {

    public static String toUTF8(String str) {
        try {
            return new String(str.getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean isEmpty(Object obj) {
        String str = (null == obj ? "" : obj.toString());
        if (null == str || "" == str || " " == str || str.length() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    //随机生成车牌号
    public static String getCarLicence(String province){

        Random random=new Random();
        String str="1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String strCity="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char city = strCity.charAt(random.nextInt(26));

        StringBuffer sb=new StringBuffer();

        for(int i=0;i<5;i++){

            int number =random.nextInt(46);

            sb.append(str.charAt(number));
        }

        String carLincence = province + city +  "." + sb.toString();
        return carLincence;
    }
}


