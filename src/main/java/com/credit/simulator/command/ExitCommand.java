package com.credit.simulator.command;

import java.util.List;

import com.credit.simulator.controller.AppContext;

public class ExitCommand implements Command {
    
    @Override
    public String name() {
        return "exit";
    }

    @Override
    public String usage() {
        return "exit";
    }

    @Override
    public String description() {
        return "Keluar dari aplikasi";
    }

    @Override
    public void execute(List<String> args, AppContext context) {
        context.view().showInfo("Sampai jumpa!");
        context.stop();
    }
}