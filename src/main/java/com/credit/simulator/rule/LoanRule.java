package com.credit.simulator.rule;

public interface LoanRule {
    int minDownPaymentPercent();
    void validateYear(int vehicleYear, int currentYear);
}
