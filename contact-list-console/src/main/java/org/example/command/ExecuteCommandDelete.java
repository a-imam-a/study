package org.example.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.Contact;
import java.util.HashMap;

public class ExecuteCommandDelete implements Executor{

    private static final Logger LOGGER = LogManager.getLogger(ExecuteCommandAdd.class);
    private static final Marker COMMANDS_HISTORY_MARKER = MarkerManager.getMarker("COMMANDS_HISTORY");
    private String email;
    private HashMap<String, Contact> contacts;

    public ExecuteCommandDelete(String email, HashMap<String, Contact> contacts) {
        this.email = email.trim();
        this.contacts = contacts;
    }

    @Override
    public void execute() {
        String result;
        Contact contact = contacts.remove(email);
        if (contact == null) {
            result = "Не удалось найти контакт с email " + email;

        }
        else result = "Удален контакт " + contact;

        System.out.println(result);
        LOGGER.info(COMMANDS_HISTORY_MARKER, "execute command: Delete, email: {}, result: {}", email, result);
    }
}
