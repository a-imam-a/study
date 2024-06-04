package com.example.tasktracker.controller;

import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.model.TaskRequest;
import com.example.tasktracker.model.TaskResponse;
import com.example.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Flux<TaskResponse> getAllTasks() {
        return taskService.findAll().map(taskMapper::taskToTaskResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Mono<ResponseEntity<TaskResponse>> getTaskById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.save(taskMapper.taskRequestToTask(taskRequest))
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> updateTask(@PathVariable String id, @RequestBody TaskRequest taskRequest) {
        return taskService.update(id, taskMapper.taskRequestToTask(taskRequest))
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Mono<ResponseEntity<TaskResponse>> addObserverToTask(@RequestParam String taskId, @RequestParam String userId) {
        return taskService.addObserver(taskId, userId)
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping()
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Mono<ResponseEntity<TaskResponse>> deleteObserverFromTask(@RequestParam String taskId, @RequestParam String userId) {
        return taskService.deleteObserver(taskId, userId)
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
