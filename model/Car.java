package model;

// Concrete Subclass from the superclass vehicle
public class Car extends Vehicle{

    // Unique datafield for the car to check if it's automatic or not
    private boolean isAutomatic;

    // Constructor to initiaize the datafields
    public Car(String model, String liscenseNumber, double rate, boolean isAuto){
        //Calling the Superclass Constructor to initialize the model, license and rate
        super(model, liscenseNumber, rate);
        //Initialize Car-specific attributes
        this.isAutomatic = isAuto;
    }

    //getters to get the taxrate as a string and as a double
    public String getTaxRate()
    {
        return "30%";
    }
    public double getTaxRateFraction()
    {
        return carTax;
    }

    // Getter for the specific attribute of the car
    public boolean getIsAutmatic(){
        return isAutomatic;
    }
}