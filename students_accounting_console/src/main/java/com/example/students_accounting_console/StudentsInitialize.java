package com.example.students_accounting_console;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("app.create-on-startup.enabled")
@RequiredArgsConstructor
public class StudentsInitialize {

    private final StudentsStorage studentsStorage;

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        studentsStorage.add("Иванов", "Иван", 25);
        studentsStorage.add("Петров", "Петр", 27);
    }

}
