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

// Controller class for the Sign-Up Dialog.
// Handles the registration logic for new customers, including input validation,
// account creation, and saving data to the persistent storage.
public class SignupDialogController {

    // Input fields linked to the FXML layout
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField signupUsernameField;
    @FXML private PasswordField signupPasswordField;
    @FXML private PasswordField confirmPasswordField;

    // Buttons and status label
    @FXML private Button createAccountButton;
    @FXML private Button cancelButton;
    @FXML private Label signupStatusLabel;

    // Main logic for creating a new account.
    // Triggered when the user clicks the "Create Account" button.
    @FXML
    void handleCreateAccount(ActionEvent event) {
        // 1. Retrieve user input
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = signupUsernameField.getText().trim();
        String password = signupPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Validation Checks

        // Ensure no fields are left empty
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            signupStatusLabel.setText("Please fill all fields!");
            return;
        }

        // Basic email format check
        if (!email.contains("@")) {
            signupStatusLabel.setText("Please enter a valid email address!");
            return;
        }

        // Ensure password and confirm password match
        if (!password.equals(confirmPassword)) {
            signupStatusLabel.setText("Passwords do not match!");
            return;
        }

        // Duplicate Username Check
        // Loop through existing customers to ensure username is unique
        for (Customer customer : Customer.customers) {
            if (customer.getAccount().getUsername().equals(username)) {
                signupStatusLabel.setText("Username already exists!");
                return;
            }
        }

        // Create the Account and Customer objects
        try {
            // Create Account with role 'c' for Customer (Admin uses 'A')
            Account newAccount = new Account(username, password, 'c');

            // Create Customer (This automatically adds them to the global Customer.customers list)
            new Customer(name, email, newAccount);

            // 5. Persistence: Save to CSV immediately
            DataManager.saveAllData();

            // Show success message
            showAlert("Account created successfully!\nYou can now login with username: " + username);

            // Close the popup window
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            // Catch any unexpected errors during creation
            signupStatusLabel.setText("Error creating account: " + e.getMessage());
        }
    }

    // Closes the dialog without creating an account.
    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Helper method to display information popups.
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}