package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Customer {
    private int customerId;
    private String name;
    private String email; 
    private Account account;
    
    private static int idCounter = 1;
    
    // القائمة اللي بيتخزن فيها كل العملاء (عشان الجدول يشوفهم)
    public static ArrayList<Customer> customers = new ArrayList<>(); 
    
    ArrayList<Booking> bookings = new ArrayList<>();

    public Customer(String name, String email, Account account){
        this.customerId = idCounter++; 
        this.name = name;
        this.email = email;
        this.account = account;
        
        // بيضيف العميل لنفسه في القائمة أول ما يتنشأ
        customers.add(this); 
    }

    // --- Getters (عشان الجدول يعرض البيانات) ---
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; } 
    public Account getAccount() { return account; }
    
    // --- دالة الحجز ---
    public void bookVehicle(Vehicle vehicle, LocalDate startDate, LocalDate endDate) throws Exception{
        if(startDate.isBefore(LocalDate.now())) {
            throw new Exception("Start date error: Cannot book in the past.");
        }
        bookings.add(new Booking(startDate, endDate, this, vehicle));
    }

    // --- ⚠️ الدالة دي كانت ناقصة ومهمة جداً عشان Booking.java يشتغل ---
    public void returnVehicle(Vehicle vehicle) {
        vehicle.setIsAvailable(true);
        System.out.println("Vehicle returned by customer: " + this.name);
    }
    
    // --- إضافة: دالة displayInfo عشان RentalManager يشتغل ---
    public String displayInfo() {
        return "ID: " + customerId + " | Name: " + name + " | Email: " + email;
    }
}