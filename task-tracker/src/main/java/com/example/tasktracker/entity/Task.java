package com.example.tasktracker.entity;

import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;
    @ReadOnlyProperty
    private User author;
    @ReadOnlyProperty
    private User assignee;
    @ReadOnlyProperty
    private Set<User> observers;

    public void addObserver(User user) {
        observerIds.add(user.getId());
        observers.add(user);
    }

    public void deleteObserver(User user) {
        observerIds.remove(user.getId());
        observers.remove(user);
    }
}
