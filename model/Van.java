package model;

// Concrete Subclass (OOP Concept: Inheritance)
// The Van class extends the abstract 'Vehicle' class.
// It inherits common attributes (model, rate, availability) but adds its own specific data.
public class Van extends Vehicle{

    // Unique Attribute (Specialization)
    // Only Vans track "Load Capacity". Cars and Bikes do not need this field.
    private double loadCapacity;

    // Constructor
    public Van(String model, String licenseNumber, double rate, double loadCapacity) {
        // Step 1: Call the Superclass Constructor (Vehicle)
        // This executes the logic in Vehicle.java:
        // - Generates the ID
        // - Sets isAvailable = true
        // - Adds this object to the global 'allVehicles' list
        super(model, licenseNumber, rate);

        // Step 2: Initialize Van-specific attributes
        this.loadCapacity = loadCapacity;
    }

    // Getter for the specific attribute
    // Used by DataManager to save the capacity to the CSV file.
    public double getLoadCapacityInclude() {
        return loadCapacity;
    }

    public String getTaxRate()
    {
        return "15%";
    }
    public double getTaxRateFraction()
    {
        return vanTax;
    }
}