package com.credit.simulator.model;

import com.credit.simulator.exception.ValidationException;

public enum VehicleCondition {
    BARU("Baru"),
    BEKAS("Bekas");

    private final String label;

    VehicleCondition(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static VehicleCondition fromInput(String raw) {
        String value = raw == null ? "" : raw.trim();
        for (VehicleCondition condition : values()) {
            if (condition.label.equalsIgnoreCase(value)) {
                return condition;
            }
        }
        throw new ValidationException("Kondisi kendaraan harus 'Baru' atau 'Bekas'.");
    }
}
