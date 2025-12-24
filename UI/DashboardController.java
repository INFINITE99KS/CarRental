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

public class DashboardController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private Label statusLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password!");
            return;
        }
        
        // Check admin login
        if (username.equals("admin") && password.equals("1234")) {
            statusLabel.setText("Admin Login Success. Loading Admin Dashboard...");
            loadAdminDashboard();
            return;
        }
        
        // Check customer login
        for (Customer customer : Customer.customers) {
            if (customer.getAccount().getUsername().equals(username) && 
                customer.getAccount().checkPassword(password)) {
                statusLabel.setText("Customer Login Success. Loading Customer Dashboard...");
                loadCustomerDashboard(customer);
                return;
            }
        }
        
        statusLabel.setText("Invalid username or password!");
    }

    @FXML
    void handleSignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SignupDialog.fxml"));
            Stage signupStage = new Stage();
            signupStage.setTitle("Sign Up");
            signupStage.setScene(new Scene(root, 450, 550));
            signupStage.centerOnScreen();
            signupStage.show();
        } catch (Exception e) {
            statusLabel.setText("Error opening signup dialog!");
            e.printStackTrace();
        }
    }
    
    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Admin Dashboard - Car Rental System");
            stage.setMaximized(true);
        } catch (Exception e) {
            statusLabel.setText("Error loading admin dashboard!");
            e.printStackTrace();
        }
    }
    
    private void loadCustomerDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerDashboard.fxml"));
            Parent root = loader.load();
            
            // Pass customer data to controller
            CustomerDashboardController controller = loader.getController();
            controller.setCustomer(customer);
            
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