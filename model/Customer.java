package model;

import java.time.LocalDate;
import java.util.ArrayList;
import model.CustomExceptions.*;

public class Customer {
    private int customerId;
    private String name;
    private String email; 
    private Account account;
    
    private static int idCounter = 1;
    

    public static ArrayList<Customer> customers = new ArrayList<>(); 
    
    ArrayList<Booking> bookings = new ArrayList<>();

    public Customer(String name, String email, Account account){
        this.customerId = idCounter++; 
        this.name = name;
        this.email = email;
        this.account = account;
        

        customers.add(this); 
    }


    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; } 
    public Account getAccount() { return account; }
    

    public void bookVehicle(Vehicle vehicle, LocalDate startDate, LocalDate endDate) 
            throws InvalidBookingException, VehicleNotAvailableException, InvalidDateException {
        
        // Validate inputs
        if (vehicle == null) {
            throw new InvalidBookingException("Vehicle cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Start date and end date cannot be null");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Start date cannot be in the past");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException("End date cannot be before start date");
        }
        if (!vehicle.getIsAvailable()) {
            throw new VehicleNotAvailableException("Vehicle is not available for booking");
        }
        
        // Create booking if all validations pass
        bookings.add(new Booking(startDate, endDate, this, vehicle));
    }


    public void returnVehicle(Vehicle vehicle) {
        vehicle.setIsAvailable(true);
        System.out.println("Vehicle returned by customer: " + this.name);
    }
}