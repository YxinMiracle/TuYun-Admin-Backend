package com.yxinmiracle.utils.common;

public class PhoneUtil {
    public static boolean isPhone(String phoneNum) {
        String regex = "[1][34578][0-9]{9}"; //手机号码的格式：第一位只能为1，第二位可以是3，4，5，7，8，第三位到第十一位可以为0-9中任意一个数字
        if (phoneNum.matches(regex)) {
            return true;
        }
        return false;
    }
}
