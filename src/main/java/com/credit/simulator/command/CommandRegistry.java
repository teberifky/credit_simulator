package com.credit.simulator.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class CommandRegistry {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public CommandRegistry register(Command command) {
        commands.put(command.name().toLowerCase(Locale.ROOT), command);
        return this;
    }

    public Optional<Command> find(String name) {
        return Optional.ofNullable(commands.get(name.toLowerCase(Locale.ROOT)));
    }

    public Collection<Command> all() {
        return commands.values();
    }
}
