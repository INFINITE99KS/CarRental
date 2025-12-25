package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

// Controller class for the Main Login Dashboard.
public class DashboardController {

    // UI Components linked to FXML
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Label statusLabel; // Used to show error messages .

    // Logic for the "Login" button.
    // Determines if the user is an Admin or a Customer and routes them accordingly.
    @FXML
    void handleLogin(ActionEvent event) {
        // Get raw input from text fields
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Basic Validation: Ensure fields aren't empty
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password!");
            return;
        }

        // Check for Admin Login
        // Note: Admin credentials are hardcoded for simplicity in this project context.
        if (username.equals("admin") && password.equals("1234")) {
            statusLabel.setText("Admin Login Success. Loading Admin Dashboard...");
            loadAdminDashboard();
            return; // Exit method so we don't check for customers
        }

        // Check for Customer Login
        // We iterate through the global list of customers loaded from the CSV.
        for (Customer customer : Customer.customers) {
            // Check username AND verify password using the Account method
            if (customer.getAccount().getUsername().equals(username) &&
                    customer.getAccount().checkPassword(password)) {

                statusLabel.setText("Customer Login Success. Loading Customer Dashboard...");

                // Load the customer screen and pass the specific customer object
                loadCustomerDashboard(customer);
                return;
            }
        }

        // If loop finishes without finding a match:
        statusLabel.setText("Invalid username or password!");
    }

    // Logic for the "Sign Up" button.
    // Opens a separate popup window for registration.
    @FXML
    void handleSignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SignupDialog.fxml"));
            Stage signupStage = new Stage(); // Create a new window (Stage)
            signupStage.setTitle("Sign Up");
            signupStage.setScene(new Scene(root, 450, 550));
            signupStage.centerOnScreen();
            signupStage.show(); // Show non-blocking window
        } catch (Exception e) {
            statusLabel.setText("Error opening signup dialog!");
            e.printStackTrace();
        }
    }

    // Switches the scene to the Admin Dashboard
    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();

            // Get the current window (Stage) from the login button
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Replace the scene content
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Admin Dashboard - Car Rental System");
            stage.setMaximized(true);
        } catch (Exception e) {
            statusLabel.setText("Error loading admin dashboard!");
            e.printStackTrace();
        }
    }

    // Switches the scene to the Customer Dashboard
    // This method passes data (the logged-in customer) to the next controller.
    private void loadCustomerDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerDashboard.fxml"));
            Parent root = loader.load();

            // Get access to the controller of the new screen
            CustomerDashboardController controller = loader.getController();

            // Pass the specific customer object to it
            // This ensures the dashboard knows *who* is logged in.
            controller.setCustomer(customer);

            // Switch the scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Customer Dashboard - " + customer.getName());
            stage.setMaximized(true);
        } catch (Exception e) {
            statusLabel.setText("Error: File 'CustomerDashboard.fxml' not found!");
            e.printStackTrace();
        }
    }
}