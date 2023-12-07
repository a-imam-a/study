package com.example.students_accounting_console;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;


@ShellComponent
@RequiredArgsConstructor
public class StudentsAccounting {

    private final StudentsStorage studentsStorage;

    @ShellMethod(key = "a")
    public void add(@ShellOption(value = "f") String firstName,
                      @ShellOption(value = "l") String lastName,
                      @ShellOption(value = "a") int age) {

        studentsStorage.add(firstName, lastName, age);

    }

    @ShellMethod(key = "d")
    public void delete(@ShellOption(value = "i") String id) {

        studentsStorage.delete(id);

    }

    @ShellMethod(key = "l")
    public void list() {
        studentsStorage.list();
    }

    @ShellMethod(key = "c")
    public void clear() {
        studentsStorage.clear();
    }
}
