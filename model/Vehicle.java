package model;

import java.util.ArrayList;

// Abstract Class (OOP Concept: Abstraction)
// This class serves as a "blueprint" for specific vehicle types (Car, Bike, Van).
// It cannot be instantiated directly (you can't say "new Vehicle()").
//
// Implements Comparable<Vehicle> (Project Requirement: Generic Sort)
// This interface allows us to sort a list of vehicles automatically (e.g., by price).
public abstract class Vehicle implements Comparable<Vehicle> {

    // Encapsulation: All fields are private to protect data integrity.
    // They can only be accessed via public Getter/Setter methods.
    private int vehicleId;
    private String licenseNumber;
    private String model;
    private double dailyRate;
    private boolean isAvailable;

    // A static list acting as a global "In-Memory Database".
    // Since it is static, it belongs to the class, not a specific object.
    // All created vehicles are automatically added here.
    public static ArrayList<Vehicle> allVehicles = new ArrayList<>();

    // Constructor
    public Vehicle(String model, String licenseNumber, double rate) {
        this.vehicleId = getNextId(); // Auto-generate unique ID
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.isAvailable = true;      // Default to available when created
        this.dailyRate = rate;

        // Auto-Registration:
        // Automatically adds this new vehicle to the global system list.
        allVehicles.add(this);
    }

    // Abstract Method (OOP Concept: Polymorphism)
    // This method has NO body. It forces every subclass (Car, Bike, Van)
    // to provide its own implementation of how to calculate cost.
    public abstract double calculateRentalCost(int days, double dailyRate);

    // --- Encapsulation: Getters and Setters ---

    public String getModel() { return model; }
    public String getLicenseNumber() { return licenseNumber; }
    public double getDailyRate() { return dailyRate; }
    public boolean getIsAvailable() { return isAvailable; }
    public int getVehicleId() { return vehicleId; }

    // Used when removing a vehicle to re-order IDs (optional logic)
    public void setVehicleId(int id) { vehicleId = id; }

    // Used when a customer books or returns a vehicle
    public void setIsAvailable(boolean available) { this.isAvailable = available; }

    // --- UI Helper Methods (Used by JavaFX TableView) ---
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

    // --- Interface Implementation (Project Requirement) ---
    // This defines the "Natural Ordering" of vehicles.
    // When Collections.sort(allVehicles) is called in AdminDashboard,
    // this logic runs to sort them by Daily Rate (Cheapest -> Most Expensive).
    @Override
    public int compareTo(Vehicle otherVehicle) {
        return Double.compare(this.dailyRate, otherVehicle.dailyRate);
    }

    // Helper for debugging to print object state
    public String displayInfo() {
        return("ID:" + vehicleId + "| Model: " + model + "| Rate: " + dailyRate);
    }

    // --- ID Generator Logic ---
    // Static counter to ensure every vehicle gets a unique ID (1, 2, 3...)
    private static int idCounter = 0;
    private static int getNextId() {
        return idCounter++;
    }
}