package com.credit.simulator.command;

import java.util.List;

import com.credit.simulator.controller.AppContext;
import com.credit.simulator.exception.ValidationException;
import com.credit.simulator.model.LoanCalculation;
import com.credit.simulator.model.LoanRequest;

public class SwitchCommand implements Command {
    
    @Override
    public String name() {
        return "switch";
    }

    @Override
    public String usage() {
        return "switch <nama_sheet>";
    }

    @Override
    public String description() {
        return "Pindah ke sheet yang sudah disimpan dan tampilkan hasilnya";
    }

    @Override
    public void execute(List<String> args, AppContext context) {
        if (args.isEmpty()) {
            throw new ValidationException("Gunakan: switch <nama_sheet>. Lihat daftar dengan 'sheets'.");
        }
        String name = args.get(0);
        LoanRequest request = context.sheets().find(name)
                .orElseThrow(() -> new ValidationException("Sheet '" + name + "' tidak ditemukan. Lihat daftar dengan 'sheets'."));
        context.validator().validate(request);

        LoanCalculation result = context.calculator().calculate(request);

        context.setActiveSheet(name);
        context.setCurrent(result);
        context.view().showResult(name, result);
    }
}
