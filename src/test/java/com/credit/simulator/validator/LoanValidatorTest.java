package com.credit.simulator.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.credit.simulator.exception.ValidationException;
import com.credit.simulator.model.VehicleCondition;
import com.credit.simulator.model.VehicleType;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class LoanValidatorTest {
    private final LoanValidator validator = new LoanValidator(Clock.fixed(Instant.parse("2026-10-08T00:00:00Z"), ZoneOffset.UTC));

    @Test
    void vehicleTypeAndConditionAreCaseInsensitive() {
        assertEquals(VehicleType.MOBIL, validator.parseType(" mObIl "));
        assertEquals(VehicleType.MOTOR, validator.parseType("MOTOR"));
        assertEquals(VehicleCondition.BARU, validator.parseCondition("baru"));
        assertEquals(VehicleCondition.BEKAS, validator.parseCondition("BEKAS"));
        assertThrows(ValidationException.class, () -> validator.parseType("truk"));
        assertThrows(ValidationException.class, () -> validator.parseCondition("second"));
    }

    @Test
    void yearMustBeFourDigits() {
        assertThrows(ValidationException.class, () -> validator.parseYear("20", VehicleCondition.BEKAS));
        assertThrows(ValidationException.class, () -> validator.parseYear("abcd", VehicleCondition.BEKAS));
        assertEquals(2010, validator.parseYear("2010", VehicleCondition.BEKAS));
    }

    @Test
    void newVehicleCannotBeOlderThanCurrentYearMinusOne() {
        assertEquals(2025, validator.parseYear("2025", VehicleCondition.BARU));
        assertEquals(2026, validator.parseYear("2026", VehicleCondition.BARU));
        assertThrows(ValidationException.class, () -> validator.parseYear("2024", VehicleCondition.BARU));
    }

    @Test
    void loanAmountMustBePositiveAndAtMostOneBillion() {
        assertEquals(new BigDecimal("1000000000"), validator.parseLoanAmount("1000000000"));
        assertEquals(new BigDecimal("1"),validator.parseLoanAmount("1"));
        assertThrows(ValidationException.class, () -> validator.parseLoanAmount("1000000001"));
        assertThrows(ValidationException.class, () -> validator.parseLoanAmount("0"));
        assertThrows(ValidationException.class, () -> validator.parseLoanAmount("-5"));
        assertThrows(ValidationException.class, () -> validator.parseLoanAmount("satu juta"));
    }


    @Test
    void tenorMustBeBetweenOneAndSix() {
        assertEquals(1, validator.parseTenor("1"));
        assertEquals(6, validator.parseTenor("6"));
        assertThrows(ValidationException.class, () -> validator.parseTenor("0"));
        assertThrows(ValidationException.class, () -> validator.parseTenor("7"));
        assertThrows(ValidationException.class, () -> validator.parseTenor("2.5"));
    }

    @Test
    void downPaymentOfNewVehicleIsAtLeast35Percent() {
        BigDecimal loan = new BigDecimal("100000000");
        assertEquals(new BigDecimal("35000000"), validator.parseDownPayment("35000000", loan, VehicleCondition.BARU));
        assertThrows(ValidationException.class,
                () -> validator.parseDownPayment("34999999", loan, VehicleCondition.BARU));
    }

    @Test
    void downPaymentOfUsedVehicleIsAtLeast25Percent() {
        BigDecimal loan = new BigDecimal("100000000");
        assertEquals(new BigDecimal("25000000"), validator.parseDownPayment("25000000", loan, VehicleCondition.BEKAS));
        assertThrows(ValidationException.class,
                () -> validator.parseDownPayment("24999999", loan, VehicleCondition.BEKAS));
    }

    @Test
    void downPaymentMustBeSmallerThanLoan() {
        BigDecimal loan = new BigDecimal("100000000");
        assertThrows(ValidationException.class,
                () -> validator.parseDownPayment("100000000", loan, VehicleCondition.BEKAS));
    }
}
