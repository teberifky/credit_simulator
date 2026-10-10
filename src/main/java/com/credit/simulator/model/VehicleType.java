package com.credit.simulator.model;

import java.math.BigDecimal;

import com.credit.simulator.exception.ValidationException;

public enum VehicleType {
    MOBIL("Mobil", new BigDecimal("8")),
    MOTOR("Motor", new BigDecimal("9"));

    private final String label;
    private final BigDecimal baseRate;

    VehicleType(String label, BigDecimal baseRate) {
        this.label = label;
        this.baseRate = baseRate;
    }

    public String label() {
        return label;
    }

    public BigDecimal baseRate() {
        return baseRate;
    }

    public static VehicleType fromInput(String raw) {
        String value = raw == null ? "" : raw.trim();
        for (VehicleType type : values()) {
            if (type.label.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ValidationException("Jenis kendaraan harus 'Mobil' atau 'Motor'.");
    }
}
