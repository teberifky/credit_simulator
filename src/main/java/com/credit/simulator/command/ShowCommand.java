package com.credit.simulator.command;

import java.util.List;

import com.credit.simulator.controller.AppContext;

public class ShowCommand implements Command {
    private final CommandRegistry registry;

    public ShowCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String name() {
        return "show";
    }

    @Override
    public String usage() {
        return "show";
    }

    @Override
    public String description() {
        return "Tampilkan semua command yang bisa digunakan";
    }

    @Override
    public void execute(List<String> args, AppContext context) {
        context.view().showInfo("Command yang tersedia:");
        for (Command command : registry.all()) {
            context.view().showInfo(String.format("  %-22s %s", command.usage(), command.description()));
        }
    }
}