package com.example.students_accounting_console;

import com.example.students_accounting_console.event.StudentEventHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class StudentsStorage {

    private final Map<String, Student> students = new HashMap<>();

    @Autowired
    private ApplicationEventPublisher publisher;

    public void add(String firstName, String lastName, int age) {

        Student student = new Student(firstName, lastName, age);
        students.put(student.getId(), student);
        publisher.publishEvent(new StudentEventHolder(this, "add - " + student));

    }

    public void delete(String id) {

        Student student = students.get(id);
        if (student == null) {
            publisher.publishEvent(new StudentEventHolder(this,
                    MessageFormat.format("delete - not found student with id: {0}", id)));
            return;
        }
        students.remove(id);
        publisher.publishEvent(new StudentEventHolder(this, "delete - " + student));

    }

    public void list() {
        students.values().stream().forEach(student -> System.out.println(student));
    }

    public void clear() {
        students.clear();
        publisher.publishEvent(new StudentEventHolder(this, "students list was cleared"));
    }

}
