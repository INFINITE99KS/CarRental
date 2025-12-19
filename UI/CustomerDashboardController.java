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
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerDashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Vehicle> vehiclesTable;
    @FXML private TableColumn<Vehicle, String> vehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML private TableColumn<Vehicle, String> vehicleLicenseColumn;
    @FXML private TableColumn<Vehicle, String> vehicleRateColumn;
    @FXML private TableColumn<Vehicle, String> vehicleStatusColumn;
    
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> bookingVehicleColumn;
    @FXML private TableColumn<Booking, String> bookingStartColumn;
    @FXML private TableColumn<Booking, String> bookingEndColumn;
    @FXML private TableColumn<Booking, String> bookingCostColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    
    @FXML private Button bookButton;
    @FXML private Button refreshButton;
    @FXML private Button logoutButton;
    
    private Customer currentCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupVehiclesTable();
        setupBookingsTable();
    }
    
    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        welcomeLabel.setText("Welcome, " + customer.getName() + "!");
        loadVehicles();
        loadBookings();
    }
    
    private void setupVehiclesTable() {
        vehicleIdColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(data.getValue().getVehicleId())));
        vehicleTypeColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        vehicleModelColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getModel()));
        vehicleLicenseColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getlicenseNumver()));
        vehicleRateColumn.setCellValueFactory(data -> 
            new SimpleStringProperty("$" + data.getValue().getDailyRate() + "/day"));
        vehicleStatusColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getIsAvailable() ? "Available" : "Rented"));
    }
    
    private void setupBookingsTable() {
        bookingIdColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(data.getValue().getBookingId())));
        bookingVehicleColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getBookedVehicle().getModel()));
        bookingStartColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getStartDate().toString()));
        bookingEndColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getEndDate().toString()));
        bookingCostColumn.setCellValueFactory(data -> {
            Booking booking = data.getValue();
            long days = java.time.temporal.ChronoUnit.DAYS.between(
                booking.getStartDate(), booking.getEndDate());
            double cost = booking.getBookedVehicle().calculateRentalCost(
                (int)days, booking.getBookedVehicle().getDailyRate());
            return new SimpleStringProperty("$" + cost);
        });
        bookingStatusColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Completed"));
    }
    
    private void loadVehicles() {
        ObservableList<Vehicle> availableVehicles = FXCollections.observableArrayList();
        for (Vehicle vehicle : Vehicle.allVehicles) {
            if (vehicle.getIsAvailable()) {
                availableVehicles.add(vehicle);
            }
        }
        vehiclesTable.setItems(availableVehicles);
    }
    
    private void loadBookings() {
        ObservableList<Booking> customerBookings = FXCollections.observableArrayList();
        for (Booking booking : Booking.bookings) {
            if (booking.getCustomer().equals(currentCustomer)) {
                customerBookings.add(booking);
            }
        }
        bookingsTable.setItems(customerBookings);
    }

    @FXML
    void handleBookVehicle(ActionEvent event) {
        Vehicle selectedVehicle = vehiclesTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert("Please select a vehicle to book!");
            return;
        }
        
        if (!selectedVehicle.getIsAvailable()) {
            showAlert("Selected vehicle is not available!");
            return;
        }
        
        // Ask for number of days
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Book Vehicle");
        dialog.setHeaderText("Book " + selectedVehicle.getModel());
        dialog.setContentText("Enter number of days:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int days = Integer.parseInt(result.get());
                if (days <= 0) {
                    showAlert("Please enter a valid number of days!");
                    return;
                }
                
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(days);
                
                currentCustomer.bookVehicle(selectedVehicle, startDate, endDate);
                
                double cost = selectedVehicle.calculateRentalCost(days, selectedVehicle.getDailyRate());
                
                showAlert("Booking Confirmed!\n" +
                         "Vehicle: " + selectedVehicle.getModel() + "\n" +
                         "Duration: " + days + " days\n" +
                         "Total Cost: $" + cost + "\n" +
                         "Start Date: " + startDate + "\n" +
                         "End Date: " + endDate);
                
                loadVehicles();
                loadBookings();
                
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number!");
            } catch (Exception e) {
                showAlert("Booking failed: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        loadVehicles();
        loadBookings();
    }

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
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Car Rental System");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}