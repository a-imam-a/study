package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    private String filePath;

    public HashMap<String, Contact> initializeContacts() {
        HashMap<String, Contact> contacts = new HashMap();
        if (!profile.equals(INIT)) {
            return contacts;
        }
        Resource resource = new ClassPathResource(filePath);
        if (!resource.exists()) {
            String text = "Не найден файл инициализации контактов";
            System.out.println(text);
            LOGGER.error(EXCEPTIONS_MARKER, text);
            return contacts;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                String[] arrayContact = line.split(";");
                contacts.put(arrayContact[2], new Contact(arrayContact[0], arrayContact[1], arrayContact[2]));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Ошибка при инициализаци контактов из файла: " + e.getMessage());
            LOGGER.error(EXCEPTIONS_MARKER,  "initializeContacts: " + e.getMessage());
        }
        return contacts;
    }

    public void saveContacts(HashMap<String, Contact> contacts) {
        if (filePath.isEmpty()) filePath = "default-contacts.txt";
        try {
            File file = new ClassPathResource(filePath).getFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file.getAbsoluteFile());
            contacts.forEach((s, contact) -> addToWriter(writer, contact));
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка сохранения контактов в файл: " + e.getMessage());
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
