package org.example.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.Contact;

import java.util.HashMap;

public class ExecuteCommandList implements Executor {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteCommandAdd.class);
    private static final Marker COMMANDS_HISTORY_MARKER = MarkerManager.getMarker("COMMANDS_HISTORY");
    private HashMap<String, Contact> contacts;

    public ExecuteCommandList(HashMap<String, Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void execute() {
        LOGGER.info(COMMANDS_HISTORY_MARKER, "execute command: List");
        contacts.forEach((s, contact) ->
                    System.out.println(contact));
    }
}
