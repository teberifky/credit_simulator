package com.credit.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;

import com.credit.simulator.controller.AppContext;
import com.credit.simulator.controller.ApplicationController;
import com.credit.simulator.factory.CommandFactory;
import com.credit.simulator.repository.InMemorySheetRepository;
import com.credit.simulator.service.InstallmentCalculator;
import com.credit.simulator.validator.LoanValidator;
import com.credit.simulator.view.ConsoleView;
import com.credit.simulator.view.LineSource;

public final class Application {
    private Application() {
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Usage: credit_simulator [file_inputs.txt]");
            System.exit(2);
        }

        LineSource input = new LineSource();
        if (args.length == 1) {
            Path file = Path.of(args[0]);
            try {
                input.addSource(Files.newBufferedReader(file, StandardCharsets.UTF_8), true);
            } catch (IOException e) {
                System.err.println("[ERROR] File input tidak bisa dibaca: " + file + " (" + e.getMessage() + ")");
                System.exit(1);
            }
        }
        input.addSource(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), false);

        ConsoleView view = new ConsoleView(System.out, input);
        AppContext context = new AppContext(
                view,
                new LoanValidator(Clock.systemDefaultZone()),
                new InstallmentCalculator(),
                new InMemorySheetRepository());
        new ApplicationController(context, CommandFactory.createDefault()).run();
    }
}
