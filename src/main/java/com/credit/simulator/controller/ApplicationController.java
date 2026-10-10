package com.credit.simulator.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.credit.simulator.command.Command;
import com.credit.simulator.command.CommandRegistry;
import com.credit.simulator.exception.InputEndedException;
import com.credit.simulator.exception.ValidationException;

public class ApplicationController {
    private final AppContext context;
    private final CommandRegistry registry;

    public ApplicationController(AppContext context, CommandRegistry registry) {
        this.context = context;
        this.registry = registry;
    }

    public void run() {
        context.view().showInfo("======================================");
        context.view().showInfo("           KREDIT SIMULATOR");
        context.view().showInfo("======================================");
        context.view().showInfo("Ketik 'show' untuk melihat semua command, 'exit' untuk keluar.");
        try {
            while (context.isRunning()) {
                String line = context.view().prompt("[" + context.activeSheet() + "]> ");
                if (!line.isEmpty()) {
                    dispatch(line);
                }
            }
        } catch (InputEndedException e) {}
    }

    void dispatch(String line) {
        List<String> parts = Arrays.asList(line.trim().split("\\s+"));
        Optional<Command> command = registry.find(parts.get(0));
        if (command.isEmpty()) {
            context.view().showError("Command '" + parts.get(0) + "' tidak dikenal. Ketik 'show' atau 'exit'.");
            return;
        }
        try {
            command.get().execute(parts.subList(1, parts.size()), context);
        } catch (ValidationException e) {
            context.view().showError(e.getMessage());
        }
    }
}
