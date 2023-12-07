package com.example.students_accounting_console.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StudentEventHolder extends ApplicationEvent {

    private String message;

    public StudentEventHolder(Object source, String message) {
        super(source);
        this.message = message;
    }

}
