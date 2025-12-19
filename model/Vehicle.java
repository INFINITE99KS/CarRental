package model;

import java.util.ArrayList;

// Abstract Class (Inheritance & Abstraction)
// Implements Comparable for the "Generic Sort" requirement
public abstract class Vehicle  implements Comparable<Vehicle>{
    private int vehicleId;
    private String licenseNumber;
    private String model;
    private double dailyRate;
    private boolean isAvailable;
    public static ArrayList<Vehicle> allVehicles = new ArrayList<>();
    
    public Vehicle( String model, String licenseNumber, double rate){
        this.vehicleId = getNextId();
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.isAvailable = true;
        this.dailyRate = rate;
        allVehicles.add(this);
    }

    public abstract double calculateRentalCost(int days, double dailyRate);

    // Encapsulation: Getter and Setters
    public String getModel(){return model;}
    public String getlicenseNumver(){return licenseNumber;}
    public double getDailyRate(){return dailyRate;}
    public boolean getIsAvailable(){return isAvailable;}
    public int getVehicleId(){return vehicleId;}
    public void setVehicleId(int id){vehicleId =id;}
    public void setIsAvailable(boolean available){ this.isAvailable = available;}
    
   @Override
    public int compareTo(Vehicle otherVehicle) { 
        return Double.compare(this.dailyRate, otherVehicle.dailyRate);
    }

    public String displayInfo(){
        return("ID:" + vehicleId + "| Model"+ model + "| Rate"+ dailyRate);
    }

    private static int idCounter = 0;
    private static int getNextId()
    {
        return idCounter++;
    }

}