package model;

import java.util.ArrayList;

// Manager Class (System Logic)
// This class represents the Administrator's control over the system.
// It corresponds to the "LibraryManager" concept in the project requirements[cite: 1, 12].
// It handles high-level operations like adding/removing vehicles and managing global lists.
public class RentalManager {

    // Attributes for the Manager
    private String name;
    private Account account; // The Admin account associated with this manager

    // Centralized access points for system data.
    // These act as references to the global lists used throughout the app.
    public static ArrayList<Vehicle> vehicles;
    public static ArrayList<Customer> customers;
    public static ArrayList<Booking> bookings;

    // Constructor: Initializes the manager with specific Admin credentials.
    public RentalManager(Account account, String name){
        this.account = account;
        this.name = name;
    }

    // --- Method Overloading (OOP Concept) ---
    // We have two 'addVehicle' methods. The system knows which one to use
    // based on the parameters passed (Polymorphism/Overloading).

    // Version 1: Adds a Van (Requires 'capacity')
    public void addVehicle(String model, String licenseId, double rate, double capacity){
        // We simply create the object. The Vehicle constructor automatically
        // adds it to the global 'allVehicles' list.
        new Van(model, licenseId, rate, capacity);
    }

    // Version 2: Adds a Car or Bike (Requires boolean option 'isAuto' or 'hasHelmet')
    public void addVehicle(String model, String licenseId, double rate, boolean option, char type){
        // Factory-like logic to decide which subclass to instantiate
        if(type == 'c') {
            new Car(model, licenseId, rate, option); // option = isAutomatic
        }
        if(type == 'b') {
            new Bike(model, licenseId, rate, option); // option = helmetIncluded
        }
    }

    // Management Logic: Removing a vehicle safely
    public void removeVehicleById(int id){
        // 1. Remove the object from the global list
        Vehicle.allVehicles.remove(id);

        // 2. Re-Indexing Loop
        // Since we store IDs as simple integers (0, 1, 2...), if we delete #1,
        // we must shift #2 down to become #1. This keeps the IDs sequential
        // and prevents errors in the TableView.
        for(int i = id; i < Vehicle.allVehicles.size(); i++){
            int used = i;
            Vehicle.allVehicles.get(i).setVehicleId(used--);
        }
    }
}