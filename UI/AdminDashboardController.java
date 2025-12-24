package UI;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

// Controller class for the Admin Dashboard UI
// Handles Vehicle management, Customer viewing, and Booking history
public class AdminDashboardController implements Initializable {

    // --- Vehicles Tab Components linked to FXML ---
    @FXML private TableView<Vehicle> adminVehiclesTable;
    @FXML private TableColumn<Vehicle, Integer> adminVehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleModelColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleLicenseColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleRateColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleStatusColumn;
    @FXML private TableColumn<Vehicle, String> taxRate;
    @FXML private Button addVehicleButton;
    @FXML private Button removeVehicleButton;

    // --- Customers Tab Components linked to FXML ---
    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, Integer> customerIdColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerEmailColumn;
    @FXML private TableColumn<Customer, String> customerUsernameColumn;

    // --- Bookings Tab Components linked to FXML ---
    @FXML private TableView<Booking> adminBookingsTable;
    @FXML private TableColumn<Booking, Integer> adminBookingIdColumn;
    @FXML private TableColumn<Booking, String> adminBookingCustomerColumn;
    @FXML private TableColumn<Booking, String> adminBookingVehicleColumn;
    @FXML private TableColumn<Booking, String> adminBookingStartColumn;
    @FXML private TableColumn<Booking, String> adminBookingEndColumn;
    @FXML private TableColumn<Booking, String> adminBookingCostColumn;
    @FXML private TableColumn<Booking, String> adminBookingStatusColumn;

    @FXML private Button adminLogoutButton;
    @FXML private Label actualRevenue;

    // This method runs automatically when the window opens
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Configure how the table columns read data
        setupVehiclesTable();
        setupCustomersTable();
        setupBookingsTable();
        handleRevenue();

        // 2. Load the actual data into the tables
        loadAllData();
    }

    // --- SETUP TABLES (Using PropertyValueFactory) ---
    // PropertyValueFactory looks for "getVariableName" in your model classes

    private void setupVehiclesTable() {
        // Looks for getVehicleId() in Vehicle.java
        adminVehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        // Looks for getType()
        adminVehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        // Looks for getModel()
        adminVehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        // Looks for getLicenseNumber()
        adminVehicleLicenseColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        // Looks for getRateFormatted() - returns string like "$50.00/day"
        adminVehicleRateColumn.setCellValueFactory(new PropertyValueFactory<>("rateFormatted"));
        // Looks for getStatusFormatted() - returns "Available" or "Rented"
        adminVehicleStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusFormatted"));
        // Looks for getTaxRate(), returns the tax rate of the designated vehicle.
        taxRate.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
    }

    private void setupCustomersTable() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        // Requires a helper method getUsername() in Customer.java
        customerUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void setupBookingsTable() {
        adminBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        // Requires a helper method getCustomerName() in Booking.java
        adminBookingCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        // Requires a helper method getVehicleModel() in Booking.java (if not present, add it)
        adminBookingVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
        adminBookingStartColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        adminBookingEndColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        adminBookingCostColumn.setCellValueFactory(new PropertyValueFactory<>("costFormatted"));
        adminBookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusFormatted"));
    }

    // --- LOAD DATA ---

    private void loadAllData() {
        loadVehicles();
        loadCustomers();
        loadBookings();
    }

    private void loadVehicles() {
        // Convert the ArrayList to an ObservableList so the UI can watch it
        java.util.Collections.sort(Vehicle.allVehicles);
        adminVehiclesTable.setItems(FXCollections.observableArrayList(Vehicle.allVehicles));
    }

    private void loadCustomers() {
        customersTable.setItems(FXCollections.observableArrayList(Customer.customers));
    }

    private void loadBookings() {
        adminBookingsTable.setItems(FXCollections.observableArrayList(Booking.bookings));
    }

    // --- BUTTON HANDLERS ---

    // Triggered when "Add Vehicle" button is clicked
    @FXML
    void handleAddVehicle(ActionEvent event) {
        // Create a popup dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Vehicle");
        dialog.setHeaderText("Enter vehicle details:");

        // Create input fields
        ComboBox<String> typeCombo = new ComboBox<>(FXCollections.observableArrayList("Car", "Bike", "Van"));
        typeCombo.setValue("Car"); // Default selection

        TextField modelField = new TextField(); modelField.setPromptText("Vehicle Model");
        TextField licenseField = new TextField(); licenseField.setPromptText("License Number");
        TextField rateField = new TextField(); rateField.setPromptText("Daily Rate");

        // Dynamic fields: Checkbox for Car/Bike, Text field for Van capacity
        CheckBox optionBox = new CheckBox("Automatic (Car) / Helmet (Bike)");
        TextField capacityField = new TextField();
        capacityField.setPromptText("Load Capacity (Van only)");
        capacityField.setVisible(false); // Hidden by default

        // Logic to toggle fields when Dropdown changes
        typeCombo.setOnAction(e -> {
            boolean isVan = "Van".equals(typeCombo.getValue());
            optionBox.setVisible(!isVan);      // Hide checkbox if Van
            capacityField.setVisible(isVan);   // Show capacity if Van
        });

        // Organize fields vertically
        VBox content = new VBox(10,
                new Label("Type:"), typeCombo,
                new Label("Model:"), modelField,
                new Label("License:"), licenseField,
                new Label("Rate ($):"), rateField,
                optionBox, capacityField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Wait for user to click OK or Cancel
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Read inputs
                String model = modelField.getText().trim();
                String license = licenseField.getText().trim();
                double rate = Double.parseDouble(rateField.getText().trim());
                String type = typeCombo.getValue();

                // Validate inputs
                if (model.isEmpty() || license.isEmpty()) throw new Exception("Empty fields");

                // Check if license already exists
                if (Vehicle.allVehicles.stream().anyMatch(v -> v.getLicenseNumber().equals(license))) {
                    throw new Exception("License already exists");
                }

                // Create the correct object based on type
                if ("Car".equals(type)) new Car(model, license, rate, optionBox.isSelected());
                else if ("Bike".equals(type)) new Bike(model, license, rate, optionBox.isSelected());
                else if ("Van".equals(type)) new Van(model, license, rate, Double.parseDouble(capacityField.getText()));

                // Success
                showAlert("Vehicle added successfully!");
                loadVehicles(); // Refresh table
                DataManager.saveAllData(); // Save to CSV immediately

            } catch (NumberFormatException e) {
                showAlert("Invalid number format for Rate or Capacity.");
            } catch (Exception e) {
                showAlert("Error: " + e.getMessage());
            }
        }
    }

    // Triggered when "Remove Vehicle" button is clicked
    @FXML
    void handleRemoveVehicle(ActionEvent event) {
        // Get the row currently selected by the user
        Vehicle selected = adminVehiclesTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select a vehicle to remove!");
            return;
        }

        // Business Rule: Cannot delete a car that is currently out on rent
        if (!selected.getIsAvailable()) {
            showAlert("Cannot remove rented vehicle!");
            return;
        }

        // Ask for confirmation before deleting
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selected.getModel() + "?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Vehicle.allVehicles.remove(selected); // Remove from list
            loadVehicles(); // Refresh table
            DataManager.saveAllData(); // Save to CSV immediately
        }
    }

    @FXML
    void handleRefreshVehicles(ActionEvent event) {
        loadAllData();
    }

    // Returns to the Login Screen
    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) adminLogoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to show simple popup messages
    private void showAlert(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }

    void handleRevenue()
    {
        double calculatedRevenue = 0;
        for(Booking book:Booking.bookings)
        {
            String getCost = book.getCostFormatted();
            getCost = getCost.substring(1, getCost.length()-1);
            calculatedRevenue += Double.parseDouble(getCost);
        }
        actualRevenue.setText(String.format("$%.2f", calculatedRevenue));
    }
}