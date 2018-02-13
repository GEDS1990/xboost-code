package com.xboost.util;

import com.xboost.pojo.SiteInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

public class Validation {
    @Autowired
    SiteInfo siteInfo;

    /**
     * 检查数据是否整数，是整数返回true,不是整数返回false
     * @param str
     * @return
     */
    public static boolean isNotNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber1(String str) {// 判断整型
        return str.matches("^\\d+$$");
    }

    public static boolean isNumber2(String str) {// 判断小数，与判断整型的区别在与d后面的小数点（红色）
        return str.matches("\\d+\\.\\d+$");
    }


    /**
     * 检查网点是否存在
     * @param siteCode 不存在返回true,存在返回false
     * @return
     */
    public Boolean isNotExist(String siteCode) {

        if (siteCode.equals(siteInfo.getSiteCode())) {
            return false;
        } else {
            return true;
        }
    }


}
