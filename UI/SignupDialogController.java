package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Account;
import model.Customer;
import model.DataManager;

public class SignupDialogController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField signupUsernameField;
    @FXML private PasswordField signupPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button createAccountButton;
    @FXML private Button cancelButton;
    @FXML private Label signupStatusLabel;

    @FXML
    void handleCreateAccount(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = signupUsernameField.getText().trim();
        String password = signupPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            signupStatusLabel.setText("Please fill all fields!");
            return;
        }
        
        if (!email.contains("@")) {
            signupStatusLabel.setText("Please enter a valid email address!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            signupStatusLabel.setText("Passwords do not match!");
            return;
        }
        
        // Check if username already exists
        for (Customer customer : Customer.customers) {
            if (customer.getAccount().getUsername().equals(username)) {
                signupStatusLabel.setText("Username already exists!");
                return;
            }
        }
        
        // Create new account
        try {
            Account newAccount = new Account(username, password, 'c');
            new Customer(name, email, newAccount);
            
            // Save data immediately
            DataManager.saveAllData();
            
            showAlert("Account created successfully!\nYou can now login with username: " + username);
            
            // Close the signup dialog
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.close();
            
        } catch (Exception e) {
            signupStatusLabel.setText("Error creating account: " + e.getMessage());
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}