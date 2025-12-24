package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

public class JavaFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load saved data first
        DataManager.loadAllData();

        // Updates Bookings.
        Booking.checkExpiry();

        // Re-saves the data.
        DataManager.saveAllData();

        // If no data exists, setup initial data
        if (Vehicle.allVehicles.isEmpty()) {
            setupInitialData();
        }
        
        // Load the main dashboard FXML
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Car Rental Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        // Save data when closing
        primaryStage.setOnCloseRequest(e -> DataManager.saveAllData());
    }
    
    private void setupInitialData() {
        // Create admin account
        Account adminAccount = new Account("admin", "1234", 'A');
        RentalManager manager = new RentalManager(adminAccount, "Car Rental Manager");
        
        // Add vehicles
        manager.addVehicle("Toyota Corolla", "ABC-123", 100.0, true, 'c');
        manager.addVehicle("BMW X5", "BMW-999", 200.0, true, 'c');
        manager.addVehicle("Honda Bike", "MOTO-55", 50.0, true, 'b');
        manager.addVehicle("Mercedes Van", "VAN-001", 150.0, 1000.0);
        
        // Add customers
        Account customer1 = new Account("ahmed", "pass", 'c');
        new Customer("Ahmed Ali", "ahmed@gmail.com", customer1);
        
        Account customer2 = new Account("sara", "123", 'c');
        new Customer("Sara Mohamed", "sara@yahoo.com", customer2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}