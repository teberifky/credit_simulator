package com.credit.simulator.model;

import java.util.List;

public record LoanCalculation(
    LoanRequest request, 
    List<YearlyInstallment> installments) {
        
    public LoanCalculation {
        installments = List.copyOf(installments);
    }
}
