package model;

public class Account {
    private String username;
    private String password;
    private char role;
    public Account(String username, String password, char role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public char getRole() { return role; }

    public boolean checkPassword(String inputPass) {
        return this.password.equals(inputPass);
    }
    

    public String getPassword() {
        return password;
    }
}
