package com.example.finalexam.utilities;

public class Utility {
    public static String capitalizeFirstLetter(String value) {
        char firstLetter = Character.toUpperCase(value.charAt(0));
        return firstLetter + value.substring(1);
    }
}
