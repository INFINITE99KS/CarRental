package model;

// Concrete Subclass (OOP Concept: Inheritance)
// The 'Car' class inherits all the common traits from the abstract 'Vehicle' class
// (like model, license plate, and daily rate) but adds car-specific features.
public class Car extends Vehicle {

    // Unique Attribute (Specialization)
    // Only Cars need to track transmission type (Automatic vs. Manual).
    // Bikes and Vans in this system do not use this specific flag.
    private boolean isAutomatic;

    // Constructor
    public Car(String model, String liscenseNumber, double rate, boolean isAuto){
        // Step 1: Call the Superclass Constructor
        // This passes the core data up to the 'Vehicle' class to handle:
        // - ID generation
        // - Adding to the global 'allVehicles' list
        // - Setting default availability to true
        super(model, liscenseNumber, rate);

        // Step 2: Initialize Car-specific attributes
        this.isAutomatic = isAuto;
    }

    // Getter for the specific attribute.
    // This is used by:
    // 1. DataManager: To save the transmission type to the CSV file.
    // 2. AdminDashboard: To display the type in the table or details view.
    public boolean getIsAutmatic(){
        return isAutomatic;
    }

    // Overridden Method (OOP Concept: Polymorphism)
    // We MUST implement this method because 'Vehicle' defined it as abstract.
    //
    // Context:
    // Even though the calculation here is simple (days * rate), overriding it
    // allows us to easily add car-specific rules later (e.g., adding a surcharge
    // for Automatic cars) without breaking the rest of the application.
    @Override
    public double calculateRentalCost(int days, double dailyRate){
        return days * dailyRate;
    }
}