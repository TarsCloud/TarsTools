package com.yuewen.intellij.jce.util;

import java.io.InputStream;
import java.util.Scanner;

public class Utils {
    public static String readFile(Class<?> clazz, String name) {
        try (InputStream is = clazz.getResourceAsStream(name)) {
            if (is == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(is, "UTF-8");
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String strip(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        } else {
            str = stripStart(str, stripChars);
            return stripEnd(str, stripChars);
        }
    }

    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            int start = 0;
            if (stripChars == null) {
                while (start != strLen && Character.isWhitespace(str.charAt(start))) {
                    ++start;
                }
            } else {
                if (stripChars.isEmpty()) {
                    return str;
                }

                while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                    ++start;
                }
            }

            return str.substring(start);
        } else {
            return str;
        }
    }

    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str != null && (end = str.length()) != 0) {
            if (stripChars == null) {
                while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                    --end;
                }
            } else {
                if (stripChars.isEmpty()) {
                    return str;
                }

                while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                    --end;
                }
            }

            return str.substring(0, end);
        } else {
            return str;
        }
    }
}
