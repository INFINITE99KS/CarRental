package model;

public class Van extends Vehicle {
    private double loadCapacity;

    public Van(String modle, String liscenseNumber, double rate, double loadCapacity){
            super(modle, liscenseNumber, rate);
            this.loadCapacity = loadCapacity;
        }

         public double getLoadCapacityInclude(){return loadCapacity;}

    @Override
    public double calculateRentalCost(int days, double dailyRate){
        return days*dailyRate;
    }
}
