package model;

// Account Class (Authentication Entity)
// This class represents a user's login credentials and system role.
// It is used inside the 'Customer' class (Composition) and by the 'RentalManager'.
//
// Key OOP Concept: Encapsulation
// The password field is private. While we do have a getter for saving to CSV,
// the primary way to verify a user is via the 'checkPassword()' method.
public class Account {

    // --- Instance Variables ---
    private String username;
    private String password;

    // Role Indicator:
    // 'A' = Admin (Has full access to add/remove vehicles and view reports)
    // 'c' = Customer (Can only browse and book vehicles)
    private char role;

    // Constructor
    public Account(String username, String password, char role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- Getters ---
    public String getUsername() { return username; }
    public char getRole() { return role; }

    // --- Security Logic ---
    // Checks if the provided password matches the stored one.
    // Used by DashboardController during the login process.
    public boolean checkPassword(String inputPass) {
        // Returns true only if the input matches the stored password exactly
        return this.password.equals(inputPass);
    }

    // Getter for the password.
    // NOTE: In a real-world enterprise app, you would never expose the raw password
    // like this. However, for this student project, we need it so the
    // DataManager can write the password back to the CSV file to save the account.
    public String getPassword() {
        return password;
    }
}