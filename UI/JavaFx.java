package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

// Main Entry Point for the Car Rental Management System.
public class JavaFx extends Application {

    // The main entry point for JavaFX applications.
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Before showing any UI, we load the saved data from CSV files.
        // This ensures the system resumes exactly where the user left off.
        DataManager.loadAllData();

        // Check for any bookings that have expired (where today > end date).
        // This automatically marks them as completed and returns the vehicles.
        Booking.checkExpiry();

        // Save the updated state immediately (in case checkExpiry made changes).
        DataManager.saveAllData();

        // If the system is empty (for example, first time running the app),
        // we populate it with default data so the user isn't staring at a blank screen.
        if (Vehicle.allVehicles.isEmpty()) {
            setupInitialData();
            // Save this new initial data so it persists for the next run
            DataManager.saveAllData();
        }

        // Load the Login Screen (Dashboard.fxml)
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));

        // Configure the main window (Stage)
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Car Rental Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true); // Start full screen
        primaryStage.show();

        // Ensure data is saved automatically when the user exits.
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Application closing... Saving data.");
            DataManager.saveAllData();
        });
    }

    // Helper method to create default data for a fresh installation.
    private void setupInitialData() {
        System.out.println("First run detected. Setting up initial data...");

        // Create the Default Admin Account
        // Credentials: admin / 1234
        Account adminAccount = new Account("admin", "1234", 'A');

        // Using RentalManager here acts as our "System Manager" for initialization.
        // It adds vehicles directly to the global lists.
        RentalManager manager = new RentalManager(adminAccount, "Car Rental Manager");

        // Add Default Vehicles
        manager.addVehicle("Toyota Corolla", "ABC-123", 100.0, true, 'c'); // Car
        manager.addVehicle("BMW X5", "BMW-999", 200.0, true, 'c');         // Car
        manager.addVehicle("Honda Bike", "MOTO-55", 50.0, true, 'b');      // Bike
        manager.addVehicle("Mercedes Van", "VAN-001", 150.0, 1000.0);      // Van

        // Add Default Customers for testing
        // Customer 1: ahmed / pass
        Account customer1 = new Account("ahmed", "pass", 'c');
        new Customer("Ahmed Ali", "ahmed@gmail.com", customer1);

        // Customer 2: sara / 123
        Account customer2 = new Account("sara", "123", 'c');
        new Customer("Sara Mohamed", "sara@yahoo.com", customer2);
    }

    // Standard Java main method.
    // Launches the JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}