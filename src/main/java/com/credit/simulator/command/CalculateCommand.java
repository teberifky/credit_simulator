package com.credit.simulator.command;

import com.credit.simulator.model.LoanRequest;
import com.credit.simulator.controller.AppContext;
import com.credit.simulator.model.LoanCalculation;
import com.credit.simulator.model.VehicleCondition;
import com.credit.simulator.model.VehicleType;
import com.credit.simulator.validator.LoanValidator;
import com.credit.simulator.view.ConsoleView;

import java.math.BigDecimal;
import java.util.List;

public class CalculateCommand implements Command {

    @Override
    public String name() {
        return "calculate";
    }

    @Override
    public String usage() {
        return "calculate";
    }

    @Override
    public String description() {
        return "Menghitung simulasi cicilan kredit kendaraan.";
    }

    @Override
    public void execute(List<String> args, AppContext context) {
        ConsoleView view = context.view();
        LoanValidator validator = context.validator();
        VehicleType vehicleType = view.ask(
                "1. Jenis Kendaraan (Mobil/Motor)",
                validator::parseType
        );
        VehicleCondition vehicleCondition = view.ask(
                "2. Kondisi Kendaraan (Baru/Bekas)",
                validator::parseCondition
        );
        int vehicleYear = view.ask(
                "3. Tahun Kendaraan",
                raw -> validator.parseYear(raw, vehicleCondition)
        );
        BigDecimal totalLoanAmount = view.ask(
                "4. Jumlah Pinjaman Total (maksimal Rp1 miliar)",
                validator::parseLoanAmount
        );
        int loanTenure = view.ask(
                "5. Tenor Pinjaman (1-6 tahun)",
                validator::parseTenor
        );
        BigDecimal downPayment = view.ask(
                "6. Jumlah DP",
                raw -> validator.parseDownPayment(
                        raw,
                        totalLoanAmount,
                        vehicleCondition
                )
        );

        LoanRequest request = new LoanRequest(
                vehicleType,
                vehicleCondition,
                vehicleYear,
                totalLoanAmount,
                loanTenure,
                downPayment
        );

        validator.validate(request);

        LoanCalculation result = context.calculator().calculate(request);

        context.setCurrent(result);
        view.showResult(context.activeSheet(), result);
    }
}
