package com.credit.simulator.command;

import java.util.List;

import com.credit.simulator.controller.AppContext;
import com.credit.simulator.exception.ValidationException;

public class ResultCommand implements Command {

    @Override
    public String name() {
        return "result";
    }

    @Override
    public String usage() {
        return "result";
    }

    @Override
    public String description() {
        return "Tampilkan ulang hasil perhitungan terakhir";
    }

    @Override
    public void execute(List<String> args, AppContext context) {
        if (context.current() == null) {
            throw new ValidationException("Belum ada perhitungan. Jalankan 'calculate' terlebih dahulu.");
        }
        context.view().showResult(context.activeSheet(), context.current());
    }
}
