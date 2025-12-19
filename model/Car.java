package model;

public class Car extends Vehicle {
    private boolean isAutomatic;

    public Car( String model, String liscenseNumber,double rate, boolean isAuto){
        super( model, liscenseNumber, rate);
        this.isAutomatic = isAuto;
    }

    public boolean getIsAutmatic(){return isAutomatic;}

    @Override
    public double calculateRentalCost(int days, double dailyRate){
        return days*dailyRate;
    }
    
}