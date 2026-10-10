package com.credit.simulator.command;

import java.util.List;

import com.credit.simulator.controller.AppContext;

public class SheetsCommand implements Command {
    
    @Override
    public String name() {
        return "sheets";
    }

    @Override
    public String usage() {
        return "sheets";
    }

    @Override
    public String description() {
        return "Tampilkan daftar sheet tersimpan (* = sheet aktif)";
    }

    @Override
    public void execute(List<String> args, AppContext context) {
        List<String> names = context.sheets().names();
        if (names.isEmpty()) {
            context.view().showInfo("Belum ada sheet tersimpan. Gunakan 'save <nama_sheet>'.");
            return;
        }
        context.view().showInfo("Sheet tersimpan:");
        for (String name : names) {
            String marker = name.equals(context.activeSheet()) ? "*" : " ";
            context.view().showInfo(" " + marker + " " + name);
        }
    }
}