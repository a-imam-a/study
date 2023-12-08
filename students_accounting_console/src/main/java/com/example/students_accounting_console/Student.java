package com.example.students_accounting_console;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.UUID;

@Getter
public class Student {

    public Student(String firstName, String lastName, int age) {
        id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    private String id;

    private String firstName;

    private String lastName;

    private int age;

    @Override
    public String toString() {
        return MessageFormat.format("student : {0} {1};  age: {2}; id: {3}",
                firstName, lastName, age, id);
    }
}
