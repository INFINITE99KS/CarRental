package model;

import java.time.LocalDate;
import java.util.ArrayList;
import model.CustomExceptions.*;

// Customer Entity Class which represents a client in the rental system.
public class Customer {

    // Data fields for the customer id, name and email and account on the system
    private int customerId;
    private String name;
    private String email;
    private Account account;

    // a static arraylist (static database) to store all the customers in the system
    public static ArrayList<Customer> customers = new ArrayList<>();

    //Personal Booking History as a static arraylist
    ArrayList<Booking> bookings = new ArrayList<>();

    // Static counter for Auto-Incrementing IDs (1, 2, 3...)
    private static int idCounter = 1;

    // Constructor to initialize datafields and adding the customer to the database
    public Customer(String name, String email, Account account){
        this.customerId = idCounter++; // Assign unique ID and increment
        this.name = name;
        this.email = email;
        this.account = account;
        // When a new Customer is created, immediately add them to the global system list.
        customers.add(this);
    }

    // Getters for the id, name, email, account on our system
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Account getAccount() { return account; }

    // Delegate Method: Convenience access to username via the Account object
    public String getUsername() {
        return account.getUsername();
    }

    // Making a Booking for the customer
    // This method demonstrates the "Exception Handling" requirement as it throws specific exceptions
    public void bookVehicle(Vehicle vehicle, LocalDate startDate, LocalDate endDate)
            throws InvalidBookingException, VehicleNotAvailableException, InvalidDateException {

        // We check every possible error condition before creating the booking to make sure it's valid
        if (vehicle == null) {
            throw new InvalidBookingException("Vehicle cannot be null");
        }
        //start date and end date can't be null
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date cannot be null");
        }
        //  start date can't be  in the past
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Start date cannot be in the past");
        }
        //End date must be after Start date
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException("End date cannot be before start date");
        }
        // Vehicle must be available not booked by another customer
        if (!vehicle.getIsAvailable()) {
            throw new VehicleNotAvailableException("Vehicle is not available for booking");
        }

        //Create Booking after all checks have passed
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