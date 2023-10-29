package org.example.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.AppConfig;
import org.example.Contact;
import org.example.ContactStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;

public class ExecuteCommandSaveContacts implements Executor {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteCommandAdd.class);
    private static final Marker COMMANDS_HISTORY_MARKER = MarkerManager.getMarker("COMMANDS_HISTORY");
    private HashMap<String, Contact> contacts;

    public ExecuteCommandSaveContacts(HashMap<String, Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void execute() {

        LOGGER.info(COMMANDS_HISTORY_MARKER, "execute command: Save");

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ContactStorage contactStorage = context.getBean(ContactStorage.class);
        contactStorage.saveContacts(contacts);
    }
}
