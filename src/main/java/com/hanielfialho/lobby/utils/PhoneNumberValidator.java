package com.hanielfialho.lobby.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {

    private static final List<String> validDDDs = Arrays.asList(
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "21", "22", "24", "27", "28", "31", "32", "33", "34",
            "35", "37", "38", "41", "42", "43", "44", "45", "46", "47", "48", "49", "51", "53", "54", "55", "61", "62",
            "63", "64", "65", "66", "67", "68", "69", "71", "73", "74", "75", "77", "79", "81", "82", "83", "84", "85",
            "86", "87", "88", "89", "91", "92", "93", "94", "95", "96", "97", "98", "99");


    public boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^(" + String.join("|", validDDDs) + ")\\d{9}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


}