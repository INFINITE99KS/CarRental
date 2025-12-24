package model;

import java.util.ArrayList;

public class RentalManager {
    private String name;
    private Account account;
    public static ArrayList<Vehicle> vehicles;
    public static ArrayList<Customer> customers;
    public static ArrayList<Booking> bookings;

    public RentalManager(Account account, String name){
        this.account = account;
        this.name = name;
    }
    public void addVehicle(String model, String licenseId, double rate, double capacity){
        new Van(model, licenseId, rate, capacity);
    }
    public void addVehicle(String model, String licenseId, double rate, boolean option, char type){
        if(type == 'c') new Car(model, licenseId, rate, option);
        if(type == 'b') new Bike(model, licenseId, rate, option);
    }
    public void removeVehicleById(int id){
        Vehicle.allVehicles.remove(id);
        for(int i = id; i < Vehicle.allVehicles.size(); i++){
            int used = i;
            Vehicle.allVehicles.get(i).setVehicleId(used--);
        }
    }
}
