package org.example.command;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class ExecuteUnknownCommand implements Executor{

    private static final Logger LOGGER = LogManager.getLogger(ExecuteCommandAdd.class);
    private static final Marker COMMANDS_HISTORY_MARKER = MarkerManager.getMarker("COMMANDS_HISTORY");
    private final String COMMAND;

    public ExecuteUnknownCommand(String command) {
        this.COMMAND = command;
    }

    @Override
    public void execute() {
        String text = "Unknown command " + COMMAND;
        LOGGER.info(COMMANDS_HISTORY_MARKER, text);
        System.out.println(text);
    }
}
