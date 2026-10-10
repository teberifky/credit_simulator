package com.credit.simulator.command;

import java.util.List;

import com.credit.simulator.controller.AppContext;

public interface Command {
    String name();

    String usage();

    String description();

    void execute(List<String> args, AppContext context);
}
