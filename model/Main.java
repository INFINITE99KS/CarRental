package model;

import java.time.LocalDate;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        
        Account adminAccount = new Account("admin", "1234", 'A');
        RentalManager manager = new RentalManager(adminAccount, "ELnoby");
        System.out.println("Rental Manager Initialized.");

        manager.addVehicle("Toyota Corolla", "ABCD", 100, true, 'c');
        manager.addVehicle("BMW X5", "BM-999", 500.0, true, 'c');
        manager.addVehicle("Honda Bike", "MOTO-55", 50.0, true, 'b'); 
        manager.addVehicle("Mercedes Van", "VAN-001", 200.0, 1000.0);

        
        System.out.println("Vehicles added to database.\n");
        System.out.println(manager.displayAllVehicles());

        Collections.sort(Vehicle.allVehicles);
        System.out.println("List All Vehicles (Sorted by Price: Low to High) ---");
        System.out.println(manager.displayAllVehicles());

        
        Account userAccount = new Account("ahmed", "pass", 'c');
        Customer customer1 = new Customer("Ahmed Ali", "ahmed@gmail.com", userAccount);
        System.out.println("Customer '" + customer1.getName() + "' created.\n");
        System.out.println(customer1.displayInfo());
        
        // تجربة الحجز
        try {
            customer1.bookVehicle(Vehicle.allVehicles.get(0), LocalDate.now(), LocalDate.now().plusDays(3));
            System.out.println("\nBooking created successfully!");
            
            // عرض كل الحجوزات
            System.out.println("\nAll Bookings:");
            for (Booking booking : Booking.bookings) {
                System.out.println(booking.displayInfo());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
