package com.example.tasktracker.service;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public Flux<Task> findAll() {
        Flux<Task> tasks = taskRepository.findAll();
        Flux<User> allUsers = userRepository.findAll();
        Mono<Map<String, User>> userMapMono = allUsers.collectMap(User::getId, user -> user);

        return tasks.zipWith(userMapMono, (task, userMap) -> {
            User author = userMap.get(task.getAuthorId());
            if (author != null) {
                task.setAuthor(author);
            }
            User assignee = userMap.get(task.getAssigneeId());
            if (assignee != null) {
                task.setAssignee(assignee);
            }
            Set<User> observers = new HashSet<>();
            for (String observerId : task.getObserverIds()) {
                User observer = userMap.get(observerId);
                if (observer != null) {
                    observers.add(observer);
                }
            }
            task.setObservers(observers);
            return task;
        });
    }

    public Mono<Task> findById(String id) {
        Mono<Task> taskMono = taskRepository.findById(id);
        Mono<User> authorMono = taskMono.flatMap(task -> userRepository.findById(task.getAuthorId()));
        Mono<User> assigneeMono = taskMono.flatMap(task -> userRepository.findById(task.getAssigneeId()));
        Flux<User> observers = taskMono.flatMapMany(task -> {
            Flux<User> monoList = userRepository.findByIdIn(task.getObserverIds());
            return monoList;
        });

        return Mono.zip(taskMono, authorMono, assigneeMono, observers.collectList()).map(objects -> {
            Task task = objects.getT1();
            task.setAuthor(objects.getT2());
            task.setAssignee(objects.getT3());
            task.setObservers(objects.getT4().stream().collect(Collectors.toSet()));
            return task;
        });
    }

    public Mono<Task> save(Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        return taskRepository.save(task);
    }

    public Mono<Task> update(String id, Task task) {
        return findById(id).flatMap(taskForUpdate -> {
            taskForUpdate.setUpdatedAt(Instant.now());
            if (StringUtils.hasText(task.getName())) {
                taskForUpdate.setName(task.getName());
            }
            if (StringUtils.hasText(task.getDescription())) {
                taskForUpdate.setDescription(task.getDescription());
            }
            if (task.getStatus() != null) {
                taskForUpdate.setStatus(task.getStatus());
            }
            if (StringUtils.hasText(task.getAssigneeId())) {
                taskForUpdate.setAssigneeId(task.getAssigneeId());
            }
            return taskRepository.save(taskForUpdate);
        });
    }

    public Mono<Task> addObserver(String taskId, String userId) {
        return Mono.zip(findById(taskId), userRepository.findById(userId), (task, user) -> {
            task.setUpdatedAt(Instant.now());
            task.addObserver(user);
            taskRepository.save(task);
            return task;
        });
    }

    public Mono<Task> deleteObserver(String taskId, String userId) {

        Mono<Task> taskForUpdate =  Mono.zip(findById(taskId), userRepository.findById(userId), (task, user) -> {
            task.setUpdatedAt(Instant.now());
            task.deleteObserver(user);
            return task;
        });

        return taskForUpdate.flatMap(task -> taskRepository.save(task));
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }
}
