package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Booking {
    private LocalDate startDate;
    private LocalDate endDate;
    private Customer customer;
    private boolean isActive;
    private Vehicle bookedVehicle;
    private int bookingID;
    
    public static ArrayList<Booking> bookings = new ArrayList<>();

    public Booking(LocalDate startDate, LocalDate endDate, Customer customer, Vehicle bookedVehicle) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.bookedVehicle = bookedVehicle;

        // Changes it to booked.
        bookedVehicle.setIsAvailable(false);
        
        this.isActive = true;
        this.bookingID = getNextId();
        bookings.add(this);
    }


    public static void checkExpiry() {
        for (Booking book : bookings) {
            // checks if today is strictly AFTER the end date
            if (book.isActive() && LocalDate.now().isAfter(book.getEndDate())) {
                book.setIsActive(false); // Or book.isActive = false;
                book.getCustomer().returnVehicle(book.getBookedVehicle());
                System.out.println("Booking " + book.getBookingId() + " expired.");
            }
        }
    }

    // Public Getters.
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Vehicle getBookedVehicle() { return bookedVehicle; }
    public Customer getCustomer() { return customer; }
    public int getBookingId() { return bookingID; }
    public boolean isActive() { return isActive; }
    public String getVehicleModel() {
        return bookedVehicle.getModel();
    }
    public String getCostFormatted() {
        // Calculate days using simple subtraction (No ChronoUnit needed)
        long days = endDate.toEpochDay() - startDate.toEpochDay();

        if (days <= 0) days = 1; // Minimum charge is 1 day

        double cost = bookedVehicle.calculateRentalCost((int) days, bookedVehicle.getDailyRate());
        return String.format("$%.2f", cost);
    }
    public String getStatusFormatted() {
        return isActive ? "Active" : "Completed";
    }
    public String getCustomerName() { return customer.getName(); }


    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String displayInfo() {
        return ("Booking ID: " + bookingID +
                "\n" + "Customer: " + customer.getName() + "\n"
                + "Vehicle: " + bookedVehicle.getModel() +
                "\nStart Date: " + startDate + "\nEnd Date: "
                + endDate);
    }

    // ID Generator
    private static int idCounter = 1;
    private static int getNextId() {
        return idCounter++;
    }
}