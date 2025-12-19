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
import javafx.stage.Stage;
import model.*;

import java.net.URL;
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
            new SimpleStringProperty(data.getValue().getlicenseNumver()));
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
        showAlert("Add Vehicle feature will be implemented soon!");
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
            stage.setMaximized(true);
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