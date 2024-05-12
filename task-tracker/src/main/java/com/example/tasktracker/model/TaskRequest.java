package com.example.tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private String name;
    private String description;
    private TaskStatus status;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;
}
