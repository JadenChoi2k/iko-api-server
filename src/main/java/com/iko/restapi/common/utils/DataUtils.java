package com.iko.restapi.common.utils;

import com.iko.restapi.common.exception.InvalidParameterException;

import java.time.LocalDate;

public class DataUtils {
    public static LocalDate parseDate(String s) {
        return LocalDate.parse(s);
    }

    public static LocalDate parseBirthday(String s) throws InvalidParameterException {
        if (s == null) return null;
        LocalDate date = parseDate(s);
        if (!validateBirthday(date)) {
            throw new InvalidParameterException("invalid birthday");
        }
        return date;
    }

    public static boolean validateBirthday(LocalDate date) {
        return date.isBefore(LocalDate.now())
                && date.isAfter(LocalDate.of(1900, 1, 1));
    }
}
