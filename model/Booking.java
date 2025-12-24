package model;

import java.time.LocalDate;
import java.util.ArrayList;

// The Booking Class (Transaction Entity)
// This class represents the "Rental Agreement" between a Customer and a Vehicle.
// It tracks the dates, the status (Active/Completed), and calculates the final cost.
public class Booking {

    // --- Instance Variables ---
    private LocalDate startDate;
    private LocalDate endDate;

    // Association: Linking the Booking to the specific User and Object
    private Customer customer;
    private Vehicle bookedVehicle;

    private boolean isActive; // True = Currently rented out, False = Returned
    private int bookingID;

    // Global Booking History (Static Database)
    // Stores every booking ever made in the system.
    // Used by DataManager to save history and by AdminDashboard to display reports.
    public static ArrayList<Booking> bookings = new ArrayList<>();

    // Constructor
    public Booking(LocalDate startDate, LocalDate endDate, Customer customer, Vehicle bookedVehicle) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.bookedVehicle = bookedVehicle;

        // CRITICAL LOGIC: Side Effect
        // When a booking is created, we MUST immediately mark the car as unavailable
        // so no one else can book it for the same dates.
        bookedVehicle.setIsAvailable(false);

        this.isActive = true;
        this.bookingID = getNextId();

        // Auto-Add to global history
        bookings.add(this);
    }

    // --- System Maintenance ---
    // This method is called by JavaFx.java at startup.
    // It scans all bookings to see if any rentals ended "Yesterday" or earlier.
    public static void checkExpiry() {
        for (Booking book : bookings) {
            // Logic: If the booking is still marked 'Active' BUT the end date has passed...
            if (book.isActive() && LocalDate.now().isAfter(book.getEndDate())) {

                // 1. Mark booking as closed
                book.setIsActive(false);

                // 2. Free up the vehicle so others can rent it
                book.getCustomer().returnVehicle(book.getBookedVehicle());

                System.out.println("Booking " + book.getBookingId() + " expired and was auto-returned.");
            }
        }
    }

    // --- Standard Getters ---
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Vehicle getBookedVehicle() { return bookedVehicle; }
    public Customer getCustomer() { return customer; }
    public int getBookingId() { return bookingID; }
    public boolean isActive() { return isActive; }

    // --- UI Helper Getters (For TableView) ---
    // JavaFX Tables often need strings, not Objects.
    // These wrapper methods allow the "Vehicle" column to show "Toyota Corolla"
    // instead of "model.Car@7ad041f3".

    public String getVehicleModel() {
        return bookedVehicle.getModel();
    }

    public String getCustomerName() {
        return customer.getName();
    }

    public String getStatusFormatted() {
        return isActive ? "Active" : "Completed";
    }

    // Calculates the total price dynamically
    // Formula: (Days Rented) * (Daily Rate of Vehicle)
    public String getCostFormatted() {
        // Calculate difference in days using Epoch days (simple long subtraction)
        long days = endDate.toEpochDay() - startDate.toEpochDay();

        if (days <= 0) days = 1; // Logic: Minimum charge is always 1 day

        // Polymorphism in action:
        // calculateRentalCost() behaves differently if the vehicle is a Van vs a Car.
        double cost = bookedVehicle.calculateRentalCost((int) days, bookedVehicle.getDailyRate());

        return String.format("$%.2f", cost);
    }

    // --- Setters ---
    // Used when loading data from CSV (DataManager) to restore the correct state
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // Debugging Helper
    public String displayInfo() {
        return ("Booking ID: " + bookingID +
                "\n" + "Customer: " + customer.getName() + "\n"
                + "Vehicle: " + bookedVehicle.getModel() +
                "\nStart Date: " + startDate + "\nEnd Date: "
                + endDate);
    }

    // --- ID Generator ---
    // Ensures every booking gets a unique number (1, 2, 3...)
    private static int idCounter = 1;
    private static int getNextId() {
        return idCounter++;
    }
}