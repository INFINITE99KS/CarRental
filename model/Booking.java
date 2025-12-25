package model;

import java.time.LocalDate;
import java.util.ArrayList;

// The Booking Class (Transaction Entity)
public class Booking {

    //Data fields for start and end dates
    private LocalDate startDate;
    private LocalDate endDate;
    // Data fields for the customer that made the booking and the booked vehicle
    private Customer customer;
    private Vehicle bookedVehicle;
    //Data fields to detect if the booking is active and for the bookingid
    private boolean isActive; // True = Currently rented out, False = Returned
    private int bookingID;

    // a static arraylist (Static Database) that stores all booking made
    public static ArrayList<Booking> bookings = new ArrayList<>();

    // Constructor to initialize all data fields
    public Booking(LocalDate startDate, LocalDate endDate, Customer customer, Vehicle bookedVehicle) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.bookedVehicle = bookedVehicle;
        // CRITICAL LOGIC: to make the vehicle not available for anyone to book as it's already booked
        bookedVehicle.setIsAvailable(false);
        //setting the booking to active and giving it an id
        this.isActive = true;
        this.bookingID = getNextId();
        // Auto-Add to static arraylist which would add it to the database too
        bookings.add(this);
    }

    // a method to check if the end date of the booking has come yet and therefore could make the booking
    // completed and the vehicle would be available again
    public static void checkExpiry() {
        for (Booking book : bookings) {
            // Logic: If the booking is still marked 'Active' BUT the end date has passed...
            if (book.isActive() && LocalDate.now().isAfter(book.getEndDate())) {
                // 1. Mark booking as completed
                book.setIsActive(false);
                // 2. Free up the vehicle so others can rent it
                book.getCustomer().returnVehicle(book.getBookedVehicle());
                System.out.println("Booking " + book.getBookingId() + " expired and was auto-returned.");
            }
        }
    }

    // getters for start date, end date, the booked vehicle, the customer which made the booking
    // also the booking id and if the booking is active
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Vehicle getBookedVehicle() { return bookedVehicle; }
    public Customer getCustomer() { return customer; }
    public int getBookingId() { return bookingID; }
    public boolean isActive() { return isActive; }

    // --- UI Helper Getters (For TableView) as JavaFX Tables need strings, not Objects
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

    //ID Generator to Ensures every booking gets a unique number (1, 2, 3...)
    private static int idCounter = 1;
    private static int getNextId() {
        return idCounter++;
    }
}