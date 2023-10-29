package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;

@Component
public class ContactStorage {

    private static final Logger LOGGER = LogManager.getLogger(ContactStorage.class);
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");
    private final String INIT = "init";
    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${app.fileStorage}")
    private String fileStorage;

    public HashMap<String, Contact> initializeContacts() {
        HashMap<String, Contact> contacts = new HashMap();
        if (profile.equals(INIT)) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileStorage));
                String line = reader.readLine();
                while (line != null) {
                    String[] arrayContact = line.split(";");
                    contacts.put(arrayContact[2], new Contact(arrayContact[0], arrayContact[1], arrayContact[2]));
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(EXCEPTIONS_MARKER,  "initializeContacts: " + e.getMessage());
            }
        }
        return contacts;
    }

    public void saveContacts(HashMap<String, Contact> contacts) {

        try {
            if (fileStorage.isEmpty()) fileStorage = "src/main/resources/default-contacts.txt";
            FileWriter writer = new FileWriter(fileStorage);
            contacts.forEach((s, contact) -> addToWriter(writer, contact));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(EXCEPTIONS_MARKER,  "saveContacts: " + e.getMessage());
        }
    }

    private void addToWriter(FileWriter writer, Contact contact) {
        try {
            writer.write(contact.getFullName() + ";" +
                    contact.getPhoneNumber() + ";" +
                    contact.getEmail() +
                    System.getProperty("line.separator")
            );
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(EXCEPTIONS_MARKER,  "addToWriter: " + e.getMessage());
        }
    }

}