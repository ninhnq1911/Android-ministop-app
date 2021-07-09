package hcmute.edu.vn.mssv18110332.helper;

import android.util.Patterns;
import android.view.View;

import java.util.regex.Pattern;

public class DataValidate {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    public static String validateEmail(String emailInput)
    {
        if (emailInput.isEmpty())
             return "Field can't be empty";
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
             return "Please enter a valid email address";
        else return "OK";
    }

    public static String validateUsername(String usernameInput)
    {
        if (usernameInput.isEmpty())
            return "Field can't be empty";
        else if (usernameInput.length() > 15)
            return "Username too long";
        else if (!NAME_PATTERN.matcher(usernameInput).matches())
            return "Username invalid";
        return "OK";
    }

    public static String validatePassword(String passwordInput) {
        if (passwordInput.isEmpty())
            return "Field can't be empty";
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches())
            return "Password too weak";
        return "OK";
    }

    public static String validatePhone(String phoneInput) {
        if (phoneInput.isEmpty())
            return "Field can't be empty";
        if (!Patterns.PHONE.matcher(phoneInput).matches())
            return "Phone number is invalid";
        return "OK";
    }

    public static String validateFullname(String fullname) {
        if (fullname.isEmpty())
            return "Field can't be empty";
        return "OK";
    }
}
