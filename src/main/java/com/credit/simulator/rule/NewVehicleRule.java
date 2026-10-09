package com.credit.simulator.rule;

import com.credit.simulator.exception.ValidationException;

public class NewVehicleRule implements LoanRule {
    
    @Override
    public int minDownPaymentPercent() {
        return 35;
    }

    @Override
    public void validateYear(int vehicleYear, int currentYear) {
        if (vehicleYear < currentYear - 1) {
            throw new ValidationException("Kendaraan baru tidak boleh kurang dari tahun " + (currentYear - 1) + ".");
        }
    }
}
