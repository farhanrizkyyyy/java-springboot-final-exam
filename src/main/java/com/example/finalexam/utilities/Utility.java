package com.example.finalexam.utilities;

public class Utility {
    public static String capitalizeFirstLetter(String value) {
        char firstLetter = Character.toUpperCase(value.charAt(0));
        return firstLetter + value.substring(1);
    }

    public static Boolean validatePhoneNumber(String value) {
        final String numbersOnly = "[0-9]+";

        return value.trim().matches(numbersOnly) && (value.trim().length() >= 10 && value.trim().length() <= 13);
    }
}
