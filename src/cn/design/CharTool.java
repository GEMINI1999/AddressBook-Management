package cn.design;

import java.util.regex.Pattern;

public class CharTool {
    public static boolean isNumeric(String str) {//判断字符串是否全是数字 正则表达式
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isEmpty(String str) {//判断空格
        Pattern pattern = Pattern.compile(" *");
        return pattern.matcher(str).matches();
    }

    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
        return str.matches(regex);
    }
}
