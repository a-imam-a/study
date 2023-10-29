package org.example.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.Contact;
import org.example.InputTextChecker;

import java.util.HashMap;

public class ExecuteCommandAdd implements Executor{

    private static final Logger LOGGER = LogManager.getLogger(ExecuteCommandAdd.class);
    private static final Marker COMMANDS_HISTORY_MARKER = MarkerManager.getMarker("COMMANDS_HISTORY");
    private String inputData;
    private HashMap<String, Contact> contacts;

    public ExecuteCommandAdd(String inputData, HashMap<String, Contact> contacts) {
        this.inputData = inputData;
        this.contacts = contacts;
    }

    @Override
    public void execute() {

        LOGGER.info(COMMANDS_HISTORY_MARKER, "execute command: ADD, inputData: {}", inputData);

        String[] dataArray = inputData.split(";");
        if (!dataArrayIsCorrect(dataArray)) {
            return;
        }
        String fullName = dataArray[0].trim();
        String phoneNumber = dataArray[1].trim();
        String email = dataArray[2].trim();
        contacts.put(email, new Contact(fullName, phoneNumber, email));
    }

    private boolean dataArrayIsCorrect(String[] dataArray) {

        if (dataArray.length != 3) {
            System.out.println("Неправильный формат введенных данных");
            return false;
        }

        if (!checkFullName(dataArray[0])) {
            System.out.println("Неправильный формат ФИО");
            return false;
        }
        if (!checkPhoneNumber(dataArray[1])) {
            System.out.println("Неправильный формат телефонного номера");
            return false;
        }
        if (!checkEmail(dataArray[2])) {
            System.out.println("Неправильный формат email");
            return false;
        }

        return true;
    }

    private boolean checkEmail(String email) {
        return InputTextChecker.emailIsCorrect(email);
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        return InputTextChecker.isPhoneNumberIsCorrect(phoneNumber);
    }

    private boolean checkFullName(String fullName) {
        return InputTextChecker.fullNameIsCorrect(fullName);
    }

}
