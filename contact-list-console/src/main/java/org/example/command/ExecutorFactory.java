package org.example.command;

import org.example.Contact;

import java.util.HashMap;
import java.util.Locale;

public class ExecutorFactory {
    public Executor getExecutor(String inputText, HashMap<String, Contact> contacts) {

        String[] inputArray = inputText.split("\\s");
        String command = inputArray[0].toLowerCase();
        String inputData = inputText.replaceFirst(command, "").trim();

        Executor executor;
        if (command.equals("list")) {
            executor = new ExecuteCommandList(contacts);
        } else if (command.equals("add")) {
            executor = new ExecuteCommandAdd(inputData, contacts);
        } else if (command.equals("del")) {
            executor = new ExecuteCommandDelete(inputData, contacts);
        } else if (command.equals("save")) {
            executor = new ExecuteCommandSaveContacts(contacts);
        }
        else executor = new ExecuteUnknownCommand(command);

        return executor;
    }
}
