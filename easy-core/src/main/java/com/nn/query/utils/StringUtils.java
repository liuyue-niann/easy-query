package com.nn.query.utils;

public class StringUtils {
    public static String camelCaseToUnderscore(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                // Add underscore before uppercase letters except for the first character
                if (i != 0) {
                    result.append("_");
                }
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String orderId = "UserName";
        String underscoreString = camelCaseToUnderscore(orderId);
        System.out.println(underscoreString); // 输出为 "order_id"
    }
}
