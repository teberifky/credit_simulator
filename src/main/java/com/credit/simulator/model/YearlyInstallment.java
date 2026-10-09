package com.credit.simulator.model;

import java.math.BigDecimal;

public record YearlyInstallment(
    int year, 
    BigDecimal interestRate, 
    BigDecimal monthlyInstallment) {
}
