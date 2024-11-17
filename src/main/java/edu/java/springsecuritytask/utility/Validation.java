package edu.java.springsecuritytask.utility;


import edu.java.springsecuritytask.exception.InvalidDataException;

import java.util.regex.Pattern;

public class Validation {
    private final static String REGEX_PASSWORD = "^([\\w\\W]{10,})$";
    private final static String REGEX_NAME = "^[a-zA-Zа-яА-Яє-їЄ-Ї\\s]{2,20}$";
    private final static String REGEX_LOGIN = "^[A-Za-z]+\\.[A-Za-z0-9]*$";
    private final static String REGEX_DATE = "^[1-9][0-9][0-9]{2}-([0][1-9]|[1][0-2])-([1-2][0-9]|[0][1-9]|[3][0-1])";

    private Validation() {
    }

    public static boolean validatePassword(String password) throws InvalidDataException {
        if (password == null || !Pattern.matches(REGEX_PASSWORD, password)) {
            throw new InvalidDataException("Invalid password");
        }
        return true;
    }

    public static boolean validateName(String name) throws InvalidDataException {
        if (name == null || !Pattern.matches(REGEX_NAME, name)) {
            throw new InvalidDataException("Invalid name");
        }
        return true;
    }

    public static boolean validateLogin(String login) throws InvalidDataException {
        if (login == null || !Pattern.matches(REGEX_LOGIN, login)) {
            throw new InvalidDataException("Invalid login");
        }
        return true;
    }

    public static boolean validateDate(String date) throws InvalidDataException {
        if (date == null || !Pattern.matches(REGEX_DATE, date)) {
            throw new InvalidDataException("Invalid Date format");
        }
        return true;
    }
}
