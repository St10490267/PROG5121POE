package prog5121poe;

import java.util.regex.Pattern;

public class Registration {

    private String username;
    private String password;
    private String cellphone;

    // ----- USERNAME CHECK -----
    // Must contain an underscore and be no more than 5 characters long
    public boolean checkUsername(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // ----- PASSWORD CHECK -----
    // Must be at least 8 characters, contain a capital letter,
    // a number, and a special character
    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasCapital = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();

        return hasCapital && hasNumber && hasSpecialChar;
    }

    // ----- CELLPHONE CHECK -----
    // Must be a South African number in the format +27 followed by 9 digits
    public boolean checkCellPhoneNumber(String cellphone) {
        String regex = "^\\+27[0-9]{9}$";
        return Pattern.matches(regex, cellphone);
    }

    // ----- REGISTER METHOD -----
    // Runs all three checks and returns an appropriate message
    public String registerUser(String username, String password, String cellphone) {
        this.username = username;
        this.password = password;
        this.cellphone = cellphone;

        if (!checkUsername(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkCellPhoneNumber(cellphone)) {
            return "Cell phone number is incorrectly formatted or does not contain the international code.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least 8 characters, a capital letter, a number, and a special character.";
        }

        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully captured.";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellphone() {
        return cellphone;
    }
}