package model;

// Our custom interface.
public interface Taxable {
    // Hard coding the taxRate for each vehicle type.
    final double carTax = 0.3;
    final double bikeTax = 0.1;
    final double vanTax = 0.15;

    // The abstract methods we weill override.
    public abstract String getTaxRate();
    public abstract double getTaxRateFraction();
}
