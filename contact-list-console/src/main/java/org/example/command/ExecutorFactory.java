package org.example.command;

import org.example.Contact;

import java.util.HashMap;

public class ExecutorFactory {
    public Executor getExecutor(String inputText, HashMap<String, Contact> contacts) {

        String[] inputArray = inputText.split("\\s");
        String command = inputArray[0];
        String inputData = inputText.replaceFirst(command, "").trim();

        Executor executor;
        if (command.equals("LIST")) {
            executor = new ExecuteCommandList(contacts);
        } else if (command.equals("ADD")) {
            executor = new ExecuteCommandAdd(inputData, contacts);
        } else if (command.equals("DELETE")) {
            executor = new ExecuteCommandDelete(inputData, contacts);
        } else if (command.equals("SAVE")) {
            executor = new ExecuteCommandSaveContacts(contacts);
        }
        else executor = new ExecuteUnknownCommand(command);

        return executor;
    }
}
