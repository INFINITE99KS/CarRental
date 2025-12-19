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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    // Vehicles Tab
    @FXML private TableView<Vehicle> adminVehiclesTable;
    @FXML private TableColumn<Vehicle, String> adminVehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleModelColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleLicenseColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleRateColumn;
    @FXML private TableColumn<Vehicle, String> adminVehicleStatusColumn;
    @FXML private Button addVehicleButton;
    @FXML private Button removeVehicleButton;
    @FXML private Button refreshVehiclesButton;
    
    // Customers Tab
    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, String> customerIdColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerEmailColumn;
    @FXML private TableColumn<Customer, String> customerUsernameColumn;
    
    // Bookings Tab
    @FXML private TableView<Booking> adminBookingsTable;
    @FXML private TableColumn<Booking, String> adminBookingIdColumn;
    @FXML private TableColumn<Booking, String> adminBookingCustomerColumn;
    @FXML private TableColumn<Booking, String> adminBookingVehicleColumn;
    @FXML private TableColumn<Booking, String> adminBookingStartColumn;
    @FXML private TableColumn<Booking, String> adminBookingEndColumn;
    @FXML private TableColumn<Booking, String> adminBookingCostColumn;
    @FXML private TableColumn<Booking, String> adminBookingStatusColumn;
    
    @FXML private Button adminLogoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupVehiclesTable();
        setupCustomersTable();
        setupBookingsTable();
        loadAllData();
    }
    
    private void setupVehiclesTable() {
        adminVehicleIdColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(data.getValue().getVehicleId())));
        adminVehicleTypeColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        adminVehicleModelColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getModel()));
        adminVehicleLicenseColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getlicenseNumber()));
        adminVehicleRateColumn.setCellValueFactory(data -> 
            new SimpleStringProperty("$" + data.getValue().getDailyRate() + "/day"));
        adminVehicleStatusColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getIsAvailable() ? "Available" : "Rented"));
    }
    
    private void setupCustomersTable() {
        customerIdColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(data.getValue().getCustomerId())));
        customerNameColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getName()));
        customerEmailColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getEmail()));
        customerUsernameColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getAccount().getUsername()));
    }
    
    private void setupBookingsTable() {
        adminBookingIdColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(data.getValue().getBookingId())));
        adminBookingCustomerColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getCustomer().getName()));
        adminBookingVehicleColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getBookedVehicle().getModel()));
        adminBookingStartColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getStartDate().toString()));
        adminBookingEndColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getEndDate().toString()));
        adminBookingCostColumn.setCellValueFactory(data -> {
            Booking booking = data.getValue();
            long days = java.time.temporal.ChronoUnit.DAYS.between(
                booking.getStartDate(), booking.getEndDate());
            double cost = booking.getBookedVehicle().calculateRentalCost(
                (int)days, booking.getBookedVehicle().getDailyRate());
            return new SimpleStringProperty("$" + cost);
        });
        adminBookingStatusColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Completed"));
    }
    
    private void loadAllData() {
        loadVehicles();
        loadCustomers();
        loadBookings();
    }
    
    private void loadVehicles() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(Vehicle.allVehicles);
        adminVehiclesTable.setItems(vehicles);
    }
    
    private void loadCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(Customer.customers);
        customersTable.setItems(customers);
    }
    
    private void loadBookings() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList(Booking.bookings);
        adminBookingsTable.setItems(bookings);
    }

    @FXML
    void handleAddVehicle(ActionEvent event) {
        // Create add vehicle dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Vehicle");
        dialog.setHeaderText("Enter vehicle details:");
        
        // Create form fields
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Car", "Bike", "Van");
        typeCombo.setValue("Car");
        
        TextField modelField = new TextField();
        modelField.setPromptText("Vehicle Model");
        
        TextField licenseField = new TextField();
        licenseField.setPromptText("License Number");
        
        TextField rateField = new TextField();
        rateField.setPromptText("Daily Rate");
        
        CheckBox optionBox = new CheckBox("Automatic (Car) / Helmet (Bike)");
        TextField capacityField = new TextField();
        capacityField.setPromptText("Load Capacity (Van only)");
        capacityField.setVisible(false);
        
        // Show/hide fields based on type
        typeCombo.setOnAction(e -> {
            String selected = typeCombo.getValue();
            if ("Van".equals(selected)) {
                optionBox.setVisible(false);
                capacityField.setVisible(true);
            } else {
                optionBox.setVisible(true);
                capacityField.setVisible(false);
            }
        });
        
        VBox content = new VBox(10);
        content.getChildren().addAll(
            new Label("Vehicle Type:"), typeCombo,
            new Label("Model:"), modelField,
            new Label("License:"), licenseField,
            new Label("Daily Rate ($):"), rateField,
            optionBox, capacityField
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String model = modelField.getText().trim();
                String license = licenseField.getText().trim();
                double rate = Double.parseDouble(rateField.getText().trim());
                String type = typeCombo.getValue();
                
                if (model.isEmpty() || license.isEmpty()) {
                    showAlert("Please fill all required fields!");
                    return;
                }
                
                // Check if license already exists
                for (Vehicle v : Vehicle.allVehicles) {
                    if (v.getlicenseNumber().equals(license)) {
                        showAlert("License number already exists!");
                        return;
                    }
                }
                
                // Create vehicle based on type
                if ("Car".equals(type)) {
                    new Car(model, license, rate, optionBox.isSelected());
                } else if ("Bike".equals(type)) {
                    new Bike(model, license, rate, optionBox.isSelected());
                } else if ("Van".equals(type)) {
                    double capacity = Double.parseDouble(capacityField.getText().trim());
                    new Van(model, license, rate, capacity);
                }
                
                showAlert("Vehicle added successfully!");
                loadVehicles();
                DataManager.saveAllData();
                
            } catch (NumberFormatException e) {
                showAlert("Please enter valid numbers for rate and capacity!");
            } catch (Exception e) {
                showAlert("Error adding vehicle: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleRemoveVehicle(ActionEvent event) {
        Vehicle selectedVehicle = adminVehiclesTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("Please select a vehicle to remove!");
            return;
        }
        
        // Check if vehicle is currently rented
        if (!selectedVehicle.getIsAvailable()) {
            showAlert("Cannot remove vehicle - it is currently rented!");
            return;
        }
        
        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Removal");
        confirmAlert.setHeaderText("Remove Vehicle");
        confirmAlert.setContentText("Are you sure you want to remove:\n" + 
                                   selectedVehicle.getModel() + " (" + selectedVehicle.getlicenseNumber() + ")?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Vehicle.allVehicles.remove(selectedVehicle);
            showAlert("Vehicle removed successfully!");
            loadVehicles();
            DataManager.saveAllData();
        }
    }

    @FXML
    void handleRefreshVehicles(ActionEvent event) {
        loadAllData();
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) adminLogoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Car Rental Management System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin Panel");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}