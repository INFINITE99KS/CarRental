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
        
        // تغيير حالة السيارة لـ "محجوزة"
        bookedVehicle.setIsAvailable(false);
        
        this.isActive = true;
        this.bookingID = getNextId();
        bookings.add(this);
    }


    static void checkExpiry() {
        for (Booking book : bookings) {

            if (book.isActive && LocalDate.now().isAfter(book.getEndDate())) {
                book.isActive = false;
                

                book.getCustomer().returnVehicle(book.getBookedVehicle());
                
                System.out.println("Booking " + book.getBookingId() + " expired. Vehicle returned.");
            }
        }
    }

    // --- Public Getters ---
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Vehicle getBookedVehicle() { return bookedVehicle; }
    public Customer getCustomer() { return customer; }
    public int getBookingId() { return bookingID; }
    public boolean isActive() { return isActive; }


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