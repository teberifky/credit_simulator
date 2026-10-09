package com.credit.simulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.credit.simulator.model.LoanCalculation;
import com.credit.simulator.model.LoanRequest;
import com.credit.simulator.model.VehicleType;
import com.credit.simulator.model.YearlyInstallment;

public class InstallmentCalculator {
    private static final int MONTHS_PER_YEAR = 12; 
    
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100); 
    private static final BigDecimal EVEN_YEAR_STEP = new BigDecimal("0.1"); 
    private static final BigDecimal ODD_YEAR_STEP = new BigDecimal("0.5"); 
    
    public LoanCalculation calculate(LoanRequest request) { 
        List<YearlyInstallment> installments = new ArrayList<>(); 
        BigDecimal principal = request.principal(); 
        int tenor = request.loanTenure(); 
        
        for (int year = 1; year <= tenor; year++) { 
            BigDecimal rate = rateForYear(request.vehicleType(), year); 
            BigDecimal total = principal.add( principal.multiply(rate).divide(HUNDRED) ); 
            int remainingMonths = MONTHS_PER_YEAR * (tenor - year + 1); 
            BigDecimal monthly = total.divide( BigDecimal.valueOf(remainingMonths), 10, RoundingMode.HALF_UP ); 
            installments.add( new YearlyInstallment( year, rate, monthly.setScale(2, RoundingMode.HALF_UP) ) ); 
            principal = total.subtract( monthly.multiply(BigDecimal.valueOf(MONTHS_PER_YEAR)) ); 
        } 
        return new LoanCalculation(request, installments); 
    } 
    
    private BigDecimal rateForYear(VehicleType type, int year) { 
        BigDecimal rate = type.baseRate(); 
        
        for (int y = 2; y <= year; y++) { 
            rate = rate.add( y % 2 == 0 ? EVEN_YEAR_STEP : ODD_YEAR_STEP ); 
        } 
        return rate; 
    } 
}
