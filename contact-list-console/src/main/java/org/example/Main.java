package org.example;

import org.example.command.Executor;
import org.example.command.ExecutorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ContactStorage contactStorage = context.getBean(ContactStorage.class);
        HashMap<String, Contact> contacts = contactStorage.initializeContacts();

        ExecutorFactory executorFactory = new ExecutorFactory();
        while (true){
            System.out.println("Введите одну из следующих команд - LIST, ADD, DELETE, SAVE, EXIT:");
            Scanner scanner = new Scanner(System.in);
            String inputText = scanner.nextLine();
            if (inputText.toUpperCase(Locale.ROOT).equals("EXIT")) {
                System.out.println("Сохранить контакты: YES - сохранить, любую другую - не сохранять");
                String answer = scanner.nextLine();
                if (answer.toUpperCase(Locale.ROOT).equals("YES")) contactStorage.saveContacts(contacts);
                break;
            }
            Executor executor = executorFactory.getExecutor(inputText, contacts);
            executor.execute();
        }
    }
}