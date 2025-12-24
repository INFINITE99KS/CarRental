package model;

import java.time.LocalDate;
import java.util.ArrayList;
import model.CustomExceptions.*;

// Customer Entity Class
// Represents a client in the rental system.
//
// Key OOP Concept: Composition
// Instead of inheriting from Account, the Customer "HAS-A" Account.
// This separates the Login Credentials (Account) from the Profile Data (Customer).
public class Customer {

    // --- Instance Variables ---
    private int customerId;
    private String name;
    private String email;

    // Composition: Links this profile to a specific login account (Username/Password)
    private Account account;

    // --- Data Storage ---

    // 1. Global Customer Database (Static)
    // Stores ALL customers in the system. Used for login checks and Admin viewing.
    public static ArrayList<Customer> customers = new ArrayList<>();

    // 2. Personal Booking History (Instance)
    // Stores only the bookings made by THIS specific customer.
    ArrayList<Booking> bookings = new ArrayList<>();

    // Static counter for Auto-Incrementing IDs (1, 2, 3...)
    private static int idCounter = 1;

    // Constructor
    public Customer(String name, String email, Account account){
        this.customerId = idCounter++; // Assign unique ID and increment
        this.name = name;
        this.email = email;
        this.account = account;

        // Automatic Registration:
        // When a new Customer is created, immediately add them to the global system list.
        customers.add(this);
    }

    // --- Getters ---
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Account getAccount() { return account; }

    // Delegate Method: Convenience access to username via the Account object
    public String getUsername() {
        return account.getUsername();
    }

    // --- Core Business Logic: Making a Reservation ---
    //
    // This method demonstrates the "Exception Handling" requirement.
    // Instead of returning false for errors, it "throws" specific exceptions
    // that the UI Layer (Controller) must catch and handle.
    public void bookVehicle(Vehicle vehicle, LocalDate startDate, LocalDate endDate)
            throws InvalidBookingException, VehicleNotAvailableException, InvalidDateException {

        // 1. Validation Chain
        // We check every possible error condition BEFORE creating the booking.

        if (vehicle == null) {
            throw new InvalidBookingException("Vehicle cannot be null");
        }

        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date cannot be null");
        }

        // Business Rule: Cannot book in the past
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Start date cannot be in the past");
        }

        // Business Rule: End date must be after Start date
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException("End date cannot be before start date");
        }

        // Business Rule: Vehicle must be available
        if (!vehicle.getIsAvailable()) {
            throw new VehicleNotAvailableException("Vehicle is not available for booking");
        }

        // 2. Create Booking
        // If all checks pass, we proceed. The Booking constructor will automatically
        // mark the vehicle as 'unavailable'.
        Booking newBooking = new Booking(startDate, endDate, this, vehicle);

        // Add to this customer's personal history
        bookings.add(newBooking);
    }

    // Helper method to return a vehicle
    // Usually called by Booking.checkExpiry() or the Admin Panel.
    public void returnVehicle(Vehicle vehicle) {
        vehicle.setIsAvailable(true); // Make car available for others
        System.out.println("Vehicle returned by customer: " + this.name);
    }
}