package com.yxinmiracle.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {
    public static boolean isEmail(String email){
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    public static boolean isNotEmail(String email){
        return !isEmail(email);
    }
}
