package model;

import java.util.ArrayList;

// Manager class, represents the "authority" over the program, adding vehicles and removing them, examining the bookings,
// etc...
public class RentalManager {

    // Attributes for the Manager
    private String name;
    private Account account; // The Admin account associated with this manager

    // Initializes the manager with specific Admin credentials.
    public RentalManager(Account account, String name){
        this.account = account;
        this.name = name;
    }

    // We have successfully applied the concept of Method Overloading via creating two addVehicle methods,
    // The first adds the van, the second has an extra "type" field and an option field.
    public void addVehicle(String model, String licenseId, double rate, double capacity){
        // We simply create the object. The Vehicle constructor automatically
        // adds it to the global allVehicles list.
        new Van(model, licenseId, rate, capacity);
    }

    // Adds a Car or Bike (Requires boolean option 'isAuto' or 'hasHelmet')
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
        // Remove the object from the global list
        Vehicle.allVehicles.remove(id);

        // Re-Indexing Loop
        // Since we store IDs as simple integers (0, 1, 2...), if we delete #1,
        // we must shift #2 down to become #1. This keeps the IDs sequential
        // and prevents errors in the TableView.
        for(int i = id; i < Vehicle.allVehicles.size(); i++){
            int used = i;
            Vehicle.allVehicles.get(i).setVehicleId(used--);
        }
    }
}