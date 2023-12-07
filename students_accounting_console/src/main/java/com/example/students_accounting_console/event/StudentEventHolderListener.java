package com.example.students_accounting_console.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StudentEventHolderListener {

    @EventListener
    public void listen(StudentEventHolder studentEventHolder) {

        System.out.println(studentEventHolder.getMessage());

    }
}
