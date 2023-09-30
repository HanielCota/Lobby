package com.hanielfialho.lobby.utils.validations;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailValidator {
    @Getter
    private static final List<String> validEmailDomains = Arrays.asList(
            "gmail.com",
            "hotmail.com",
            "yahoo.com",
            "outlook.com",
            "aol.com",
            "icloud.com",
            "protonmail.com",
            "mail.com",
            "yandex.com",
            "zoho.com",
            "gmx.com",
            "live.com");

    public static boolean isValidEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String domain = parts[1].toLowerCase();
        return validEmailDomains.contains(domain);
    }
}
