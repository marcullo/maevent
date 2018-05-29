package com.devmarcul.maevent.utils;

public class StringUtils {
    public static String getNewLine() {
        return java.lang.System.getProperty("line.separator");
    }

    public static String getKeyValueString(String key, String value) {
        return key + ": " + value;
    }
}
