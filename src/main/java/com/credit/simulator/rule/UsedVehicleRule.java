package com.credit.simulator.rule;

public class UsedVehicleRule implements LoanRule {

    @Override
    public int minDownPaymentPercent() {
        return 25;
    }

    @Override
    public void validateYear(int vehicleYear, int currentYear) {}
}
