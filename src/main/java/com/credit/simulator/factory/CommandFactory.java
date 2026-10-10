package com.credit.simulator.factory;

import com.credit.simulator.command.CommandRegistry;
import com.credit.simulator.command.ExitCommand;
import com.credit.simulator.command.ResultCommand;
import com.credit.simulator.command.SaveCommand;
import com.credit.simulator.command.SheetsCommand;
import com.credit.simulator.command.ShowCommand;
import com.credit.simulator.command.SwitchCommand;
import com.credit.simulator.command.CalculateCommand;

public final class CommandFactory {
    private CommandFactory() {}

    public static CommandRegistry createDefault() {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new CalculateCommand())
                .register(new SaveCommand())
                .register(new SwitchCommand())
                .register(new SheetsCommand())
                .register(new ResultCommand())
                .register(new ShowCommand(registry))
                .register(new ExitCommand());
        return registry;
    }
}
