package model;

// Concrete Subclass (OOP Concept: Inheritance)
// The 'Bike' class inherits all the common attributes (ID, Model, License, Rate)
// from the abstract 'Vehicle' class but adds bike-specific features.
public class Bike extends Vehicle{

    // Unique Attribute (Specialization)
    // Only Bikes track whether a helmet is provided.
    // This distinguishes it from Cars (isAutomatic) and Vans (loadCapacity).
    private boolean helmetInclude;

    // Constructor
    public Bike(String modle, String liscenseNumber, double rate, boolean helmetInclude){
        // Step 1: Call the Superclass Constructor
        // This passes the core data up to the 'Vehicle' parent class to:
        // - Generate the unique ID
        // - Add the bike to the global 'allVehicles' list
        // - Set default availability to 'true'
        super(modle, liscenseNumber, rate);

        // Step 2: Initialize Bike-specific attributes
        this.helmetInclude = helmetInclude;
    }

    public String getTaxRate()
    {
        return "10%";
    }
    public double getTaxRateFraction()
    {
        return bikeTax;
    }

    // Getter for the specific attribute.
    // Used by:
    // 1. DataManager: To save this specific detail to the CSV file.
    // 2. AdminDashboard: To display "Helmet: Yes/No" in the UI.
    public boolean getHelmetInclude(){
        return helmetInclude;
    }
}