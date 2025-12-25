package model;

// Concrete Subclass from the abstract vehicle
public class Bike extends Vehicle{

    // Unique datafield for the bike from the car and van
    private boolean helmetInclude;

    // Constructor
    public Bike(String modle, String liscenseNumber, double rate, boolean helmetInclude){
        //Calling the Superclass Constructor to initialize the model, license and rate
        super(modle, liscenseNumber, rate);

        // Step 2: Initialize Bike-specific attributes
        this.helmetInclude = helmetInclude;
    }
    // getters for getting tax rate as a string and as a double
    public String getTaxRate()
    {
        return "10%";
    }
    public double getTaxRateFraction()
    {
        return bikeTax;
    }
    // getter for if the helmet if included
    public boolean getHelmetInclude(){
        return helmetInclude;
    }
}