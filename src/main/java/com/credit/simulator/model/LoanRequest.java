package com.credit.simulator.model;

import java.math.BigDecimal;
import java.util.Objects;

public record LoanRequest(
        VehicleType vehicleType,
        VehicleCondition vehicleCondition,
        int vehicleYear,
        BigDecimal totalLoanAmount,
        int loanTenure,
        BigDecimal downPayment) {

    public LoanRequest {
        Objects.requireNonNull(vehicleType, "Jenis kendaraan harus diisi.");
        Objects.requireNonNull(vehicleCondition, "Kondisi kendaraan harus diisi.");
        Objects.requireNonNull(totalLoanAmount, "Jumlah pinjaman harus diisi.");
        Objects.requireNonNull(downPayment, "DP harus diisi.");
    }

    public BigDecimal principal() {
        return totalLoanAmount.subtract(downPayment);
    }
}
