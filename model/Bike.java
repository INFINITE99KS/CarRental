package model;

public class Bike extends Vehicle{
    private boolean helmetInclude;

    public Bike(String model, String liscenseNumber, double rate, boolean helmetInclude){
        super(model, liscenseNumber, rate);
        this.helmetInclude = helmetInclude;
    }

    public boolean getHelmetInclude(){return helmetInclude;}

    @Override
    public double calculateRentalCost(int days, double dailyRate){
        return days*dailyRate;
    }
}
