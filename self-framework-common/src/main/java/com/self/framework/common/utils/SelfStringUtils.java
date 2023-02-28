package com.self.framework.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 自定义字符工具类
 *
 * @author wenbo.zhuang
 * @date 2022/02/17 18:59
 **/
public class SelfStringUtils {

    private static final int DOUBLE_ESCAPED_INDEX = 2;

    private static final Pattern PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    private SelfStringUtils() {
    }

    public static String unicodeToString(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        } else {
            char ch;
            for(Matcher matcher = PATTERN.matcher(str); matcher.find(); str = str.replace(matcher.group(1), ch + "")) {
                ch = (char)Integer.parseInt(matcher.group(DOUBLE_ESCAPED_INDEX), 16);
            }
            return str;
        }
    }
}
