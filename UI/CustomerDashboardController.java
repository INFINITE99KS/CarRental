package UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;


// Controller class for the Customer Dashboard.
// This handles the logic for the screen where customers view cars, make bookings,
// and manage their existing reservations.
public class CustomerDashboardController implements Initializable {

    // Labels and buttons.
    @FXML private Label welcomeLabel;
    @FXML private Button bookButton;
    @FXML private Button cancelBookingButton;
    @FXML private Button logoutButton;

    // Vehicle Table Setup
    @FXML private TableView<Vehicle> vehiclesTable;
    @FXML private TableColumn<Vehicle, String> vehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML private TableColumn<Vehicle, String> vehicleLicenseColumn;
    @FXML private TableColumn<Vehicle, String> vehicleRateColumn;
    @FXML private TableColumn<Vehicle, String> vehicleStatusColumn;
    @FXML private TableColumn<Vehicle, String> taxRate;

    // Booking Table Setup
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> bookingVehicleColumn;
    @FXML private TableColumn<Booking, String> bookingStartColumn;
    @FXML private TableColumn<Booking, String> bookingEndColumn;
    @FXML private TableColumn<Booking, String> bookingCostColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    // Tracks who is currently logged in
    private Customer currentCustomer;

     // Called automatically when the FXML file is loaded.
     // We use this to configure the table columns immediately.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupVehiclesTable();
        setupBookingsTable();
    }

     //Receives the logged-in customer data from the Login screen.
     // Updates the welcome message and loads the specific data for this user.
    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        welcomeLabel.setText("Welcome, " + customer.getName() + "!");

        // Refresh the data in the tables
        loadVehicles();
        loadBookings();
    }


    // Configures the Vehicle table columns.
    // Usage: new PropertyValueFactory<>("model") looks for the method getModel() in Vehicle class.
    private void setupVehiclesTable() {
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type")); // Looks for getType()
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        vehicleLicenseColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        vehicleRateColumn.setCellValueFactory(new PropertyValueFactory<>("rateFormatted")); // Looks for getRateFormatted()
        vehicleStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusFormatted")); // Looks for getStatusFormatted()
        // Looks for getTaxRate(), returns the tax rate of the designated vehicle.
        taxRate.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
    }

     // Configures the Booking table columns.
     // Note: The properties (like "vehicleModel") must exist as getters in the Booking class.
    private void setupBookingsTable() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
        bookingStartColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingEndColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        bookingCostColumn.setCellValueFactory(new PropertyValueFactory<>("costFormatted"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusFormatted"));
    }

     // Filters the global list of vehicles to show ONLY available ones.
    private void loadVehicles() {
        ObservableList<Vehicle> availableVehicles = FXCollections.observableArrayList();

        // Loop through all vehicles in the system
        for (Vehicle vehicle : Vehicle.allVehicles) {
            if (vehicle.getIsAvailable()) {
                availableVehicles.add(vehicle);
            }
        }
        java.util.Collections.sort(availableVehicles);
        vehiclesTable.setItems(availableVehicles);
    }

     // Filters the global list of bookings to show ONLY this customer's history.
    private void loadBookings() {
        ObservableList<Booking> customerBookings = FXCollections.observableArrayList();

        // Loop through all bookings in the system
        for (Booking booking : Booking.bookings) {
            // Check if this booking belongs to the current user
            if (booking.getCustomer().equals(currentCustomer)) {
                customerBookings.add(booking);
            }
        }
        bookingsTable.setItems(customerBookings);
    }

     // Logic for the "Book Vehicle" button.
     // Handles selection validation, user input dialog, and saving the booking.
    @FXML
    void handleBookVehicle(ActionEvent event) {
        // 1. Get the row the user clicked on
        Vehicle selectedVehicle = vehiclesTable.getSelectionModel().getSelectedItem();

        // 2. Validation Checks
        if (selectedVehicle == null) {
            showAlert("Please select a vehicle to book!");
            return;
        }

        if (!selectedVehicle.getIsAvailable()) {
            showAlert("Selected vehicle is not available!");
            return;
        }

        // 3. Create a Popup Dialog to ask for rental duration
        TextInputDialog dialog = new TextInputDialog("1"); // Default value is "1"
        dialog.setTitle("Book Vehicle");
        dialog.setHeaderText("Book " + selectedVehicle.getModel());
        dialog.setContentText("Enter number of days:");

        // 4. Show the dialog and wait for response
        // Optional<String> handles the case where user might click "Cancel" (returning empty)
        Optional<String> result = dialog.showAndWait();

        // 5. If user clicked OK (result is present)
        if (result.isPresent()) {
            try {
                // Parse the input string to a number
                int days = Integer.parseInt(result.get());

                if (days <= 0) {
                    showAlert("Please enter a valid number of days!");
                    return;
                }

                // Calculate dates
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(days);

                // Perform the booking logic (updates model)
                currentCustomer.bookVehicle(selectedVehicle, startDate, endDate);

                // Calculate cost for the success message
                double cost = selectedVehicle.calculateRentalCost(days, selectedVehicle.getDailyRate());

                // Show Success Message
                showAlert("Booking Confirmed!\n" +
                        "Vehicle: " + selectedVehicle.getModel() + "\n" +
                        "Duration: " + days + " days\n" +
                        "Tax Rate: " + selectedVehicle.getTaxRate() +"\n"+
                        "Total Cost: $" + cost + "\n" +
                        "Start Date: " + startDate + "\n" +
                        "End Date: " + endDate);

                // Refresh UI and Save to File/DB
                loadVehicles();
                loadBookings();
                DataManager.saveAllData();

            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number!"); // Caught if user typed "abc"
            } catch (Exception e) {
                showAlert("Booking failed: " + e.getMessage()); // Catch-all for other errors
            }
        }
    }

     // Logic for "Cancel Booking" button.
     // Validates selection, asks for confirmation, and updates availability.
    @FXML
    void handleCancelBooking(ActionEvent event) {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();

        // 1. Validation Checks
        if (selectedBooking == null) {
            showAlert("Please select a booking to cancel!");
            return;
        }

        if (!selectedBooking.isActive()) {
            showAlert("This booking is already completed and cannot be cancelled!");
            return;
        }

        // 2. Confirmation Dialog (Are you sure?)
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Booking");
        confirmAlert.setHeaderText("Cancel Booking Confirmation");
        confirmAlert.setContentText("Are you sure you want to cancel this booking?\n" +
                "Vehicle: " + selectedBooking.getBookedVehicle().getModel() + "\n" +
                "Start Date: " + selectedBooking.getStartDate() + "\n" +
                "End Date: " + selectedBooking.getEndDate());

        Optional<ButtonType> result = confirmAlert.showAndWait();

        // 3. If User clicked OK
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // Make the car available again
            selectedBooking.getBookedVehicle().setIsAvailable(true);

            // Remove the booking from the system list
            Booking.bookings.remove(selectedBooking);

            showAlert("Booking cancelled successfully!\nVehicle is now available for other customers.");

            // Refresh UI
            loadVehicles();
            loadBookings();
            DataManager.saveAllData();
        }
    }

     // Logs the user out and returns to the main Dashboard/Login screen.
    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Car Rental Management System");
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     // Helper method to reduce code duplication for simple popup messages.
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Car Rental System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}