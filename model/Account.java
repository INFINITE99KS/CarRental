package model;

// Account Class (Authentication Entity)
public class Account {

    // Datafields for username and password and private for encapsulation
    private String username;
    private String password;

    // Role Indicator if it's 'a' then it's for admin (Rental Manager), if 'c' then it's customer
    private char role;

    // Constructor to initiate the datafields
    public Account(String username, String password, char role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // getters for username, role and password
    public String getUsername() { return username; }
    public char getRole() { return role; }
    public String getPassword() {
        return password;
    }

    // Checks if the provided password matches the stored one Used during the login process.
    public boolean checkPassword(String inputPass) {
        // Returns true only if the input matches the stored password exactly
        return this.password.equals(inputPass);
    }
}