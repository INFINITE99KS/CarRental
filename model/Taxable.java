package model;

public interface Taxable {
    final double carTax = 0.3;
    final double bikeTax = 0.1;
    final double vanTax = 0.15;

    public abstract String getTaxRate();
    public abstract double getTaxRateFraction();
}
