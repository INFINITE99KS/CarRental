package model;

// Successfully applies the inheritance pillar.
public class Van extends Vehicle{

    // Our unique attribution for Van.
    private double loadCapacity;

    // Constructor
    public Van(String model, String licenseNumber, double rate, double loadCapacity) {
        // Calls the parent class constructor to initialize the shared data fields.
        super(model, licenseNumber, rate);
        // Initializing the custom data fields.
        this.loadCapacity = loadCapacity;
    }

    // Getter for the specific attribute
    // Used by DataManager to save the capacity to the CSV file.
    public double getLoadCapacityInclude() {
        return loadCapacity;
    }

    // For our table view in CustomerDashboard.
    public String getTaxRate()
    {
        return "15%";
    }

    // Overriding our implemented interface of "Taxable".
    public double getTaxRateFraction()
    {
        return vanTax;
    }
}