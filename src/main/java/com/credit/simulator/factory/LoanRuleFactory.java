package com.credit.simulator.factory;

import com.credit.simulator.model.VehicleCondition;
import com.credit.simulator.rule.LoanRule;
import com.credit.simulator.rule.NewVehicleRule;
import com.credit.simulator.rule.UsedVehicleRule;

public final class LoanRuleFactory {
    private LoanRuleFactory() {}

    public static LoanRule create(VehicleCondition condition) {
        return switch (condition) {
            case BARU -> new NewVehicleRule();
            case BEKAS -> new UsedVehicleRule();
        };
    }
}
