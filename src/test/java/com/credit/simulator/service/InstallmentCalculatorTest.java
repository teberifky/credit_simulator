package com.credit.simulator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import com.credit.simulator.model.LoanCalculation;
import com.credit.simulator.model.LoanRequest;
import com.credit.simulator.model.VehicleCondition;
import com.credit.simulator.model.VehicleType;
import com.credit.simulator.model.YearlyInstallment;

class InstallmentCalculatorTest {
    private final InstallmentCalculator calculator = new InstallmentCalculator();

    @Test
    void matchesTheRumusXlsxExample() {
        LoanRequest request = new LoanRequest(VehicleType.MOBIL, VehicleCondition.BEKAS, 2020,
                new BigDecimal("100000000"), 3, new BigDecimal("25000000"));

        LoanCalculation result = calculator.calculate(request);

        assertEquals(3, result.installments().size());
        assertYear(result.installments().get(0), "8", "2250000.00");
        assertYear(result.installments().get(1), "8.1", "2432250.00");
        assertYear(result.installments().get(2), "8.6", "2641423.50");
    }

    @Test
    void motorUsesNinePercentBaseRate() {
        LoanRequest request = new LoanRequest(VehicleType.MOTOR, VehicleCondition.BARU, 2025,
                new BigDecimal("12000000"), 1, new BigDecimal("4200000"));

        LoanCalculation result = calculator.calculate(request);

        assertEquals(1, result.installments().size());
        assertYear(result.installments().get(0), "9", "708500.00");
    }

    @Test
    void mobilRatesStepUpEveryYear() {
        LoanRequest request = new LoanRequest(VehicleType.MOBIL, VehicleCondition.BARU, 2025,
                new BigDecimal("1000000000"), 6, new BigDecimal("500000000"));

        LoanCalculation result = calculator.calculate(request);

        String[] expected = {"8", "8.1", "8.6", "8.7", "9.2", "9.3"};
        assertEquals(6, result.installments().size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(0, new BigDecimal(expected[i]).compareTo(result.installments().get(i).interestRate()),
                    "rate year " + (i + 1));
        }
    }

    @Test
    void motorRatesStartAtNinePercent() {
        LoanRequest request = new LoanRequest(VehicleType.MOTOR, VehicleCondition.BEKAS, 2019,
                new BigDecimal("50000000"), 2, new BigDecimal("12500000"));

        LoanCalculation result = calculator.calculate(request);

        assertEquals(0, new BigDecimal("9").compareTo(result.installments().get(0).interestRate()));
        assertEquals(0, new BigDecimal("9.1").compareTo(result.installments().get(1).interestRate()));
    }

    private static void assertYear(YearlyInstallment actual, String rate, String monthly) {
        assertEquals(0, new BigDecimal(rate).compareTo(actual.interestRate()), "rate year " + actual.year());
        assertEquals(new BigDecimal(monthly), actual.monthlyInstallment(), "monthly year " + actual.year());
    }
}
