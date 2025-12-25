package model;

import java.util.ArrayList;


// An abstract class, serves as the blueprint for our specific vehicle types (Car, Bike, Van).
// Implements the Comparable interface in order to implement sorting, in which we sort by the daily rate.
public abstract class Vehicle implements Comparable<Vehicle>, Taxable {

    // We successfully applied the concept of Encapsulation via the use of private data access modifiers,
    // Which made us need to make getter and setter methods in order to access said data fields.
    private int vehicleId;
    private String licenseNumber;
    private String model;
    private double dailyRate;
    private boolean isAvailable;

    // Added a list to keep track of all the vehicles created.
    // We made it public static so we can access it wherever we wanted without having to instantiate our class.
    public static ArrayList<Vehicle> allVehicles = new ArrayList<>();

    // Constructor
    public Vehicle(String model, String licenseNumber, double rate) {
        this.vehicleId = getNextId(); // generates a unique ID for each car.
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.isAvailable = true;      // Defaults to available when created
        this.dailyRate = rate;

        // Automatically adds this new vehicle to our array list.
        allVehicles.add(this);
    }

    // Returns the rental cost.
    public double calculateRentalCost(int days, double dailyRate) {
        return days * dailyRate*(1+getTaxRateFraction());
    }

    // Getters and setters.

    public String getModel() { return model; }
    public String getLicenseNumber() { return licenseNumber; }
    public double getDailyRate() { return dailyRate; }
    public boolean getIsAvailable() { return isAvailable; }
    public int getVehicleId() { return vehicleId; }
    // Used when removing a vehicle to re-order IDs.
    public void setVehicleId(int id) { vehicleId = id; }
    // Used when a customer books or returns a vehicle
    public void setIsAvailable(boolean available) { this.isAvailable = available; }

    // These methods format data specifically for the display columns.

    // Returns the class name (e.g., "Car", "Bike") to display in the "Type" column.
    public String getType() {
        return this.getClass().getSimpleName();
    }

    // Returns a formatted string like "$50.00/day" for the UI.
    public String getRateFormatted() {
        return String.format("$%.2f/day", dailyRate);
    }

    // Returns "Available" or "Rented" instead of "true/false" for better UX.
    public String getStatusFormatted() {
        return isAvailable ? "Available" : "Rented";
    }

    // Comparable interface implementation
    @Override
    public int compareTo(Vehicle otherVehicle) {
        return Double.compare(this.dailyRate, otherVehicle.dailyRate);
    }

    // Helper for debugging to print object state
    public String displayInfo() {
        return("ID:" + vehicleId + "| Model: " + model + "| Rate: " + dailyRate);
    }

    // ID generator.
    private static int idCounter = 0;
    private static int getNextId() {
        return idCounter++;
    }
}