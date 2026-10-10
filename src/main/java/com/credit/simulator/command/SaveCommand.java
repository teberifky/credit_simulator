package com.credit.simulator.command;

import java.util.List;
import java.util.regex.Pattern;

import com.credit.simulator.controller.AppContext;
import com.credit.simulator.exception.ValidationException;

public class SaveCommand implements Command {
    private static final Pattern VALID_NAME = Pattern.compile("[A-Za-z0-9_-]{1,30}");

    @Override
    public String name() {
        return "save";
    }

    @Override
    public String usage() {
        return "save [nama_sheet]";
    }

    @Override
    public String description() {
        return "Simpan hasil perhitungan terakhir sebagai sheet (default: sheet aktif)";
    }

    @Override
    public void execute(List<String> args, AppContext context) {
        if (context.current() == null) {
            throw new ValidationException("Belum ada perhitungan. Jalankan 'calculate' terlebih dulu.");
        }
        String name = args.isEmpty() ? context.activeSheet() : args.get(0);
        if (!VALID_NAME.matcher(name).matches()) {
            throw new ValidationException("Nama sheet hanya boleh huruf, angka, '_' atau '-' (maks 30 karakter).");
        }
        context.sheets().save(name, context.current().request());
        context.setActiveSheet(name);
        context.view().showInfo("Sheet '" + name + "' tersimpan dan sekarang aktif.");
    }
}
