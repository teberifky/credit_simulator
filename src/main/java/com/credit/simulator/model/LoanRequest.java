package com.credit.simulator.model;

import java.math.BigDecimal;

public record LoanRequest(
        VehicleType vehicleType,
        VehicleCondition vehicleCondition,
        int vehicleYear,
        BigDecimal totalLoanAmount,
        int loanTenure,
        BigDecimal downPayment) {

    public BigDecimal principal() {
        return totalLoanAmount.subtract(downPayment);
    }
}
