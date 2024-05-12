package com.example.tasktracker.controller;

import com.example.tasktracker.AbstractTest;
import com.example.tasktracker.model.TaskRequest;
import com.example.tasktracker.model.TaskResponse;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.model.UserModel;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskControllerTest extends AbstractTest {

    @Test
    public void whenGetAllTasks_thenReturnListOfTasks() {

        UserModel author = new UserModel(USER_AUTHOR_ID, "user1", "user1@user1");
        UserModel assignee = new UserModel(USER_ASSIGNEE_ID, "user2", "user2@user2");
        var expectedData = List.of(
                new TaskResponse(TASK_ID, "task1", "description1",
                        TASK_DATE, TASK_DATE,
                        TaskStatus.TODO,
                        author, assignee,
                        Set.of(author))
        );

        webTestClient.get().uri("/api/v1/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(1)
                .contains(expectedData.toArray(TaskResponse[]::new));
    }

    @Test
    public void whenGetTasksById_thenReturnTaskById() {

        UserModel author = new UserModel(USER_AUTHOR_ID, "user1", "user1@user1");
        UserModel assignee = new UserModel(USER_ASSIGNEE_ID, "user2", "user2@user2");
        var expectedData = new TaskResponse(TASK_ID, "task1", "description1",
                        TASK_DATE, TASK_DATE,
                        TaskStatus.TODO,
                        author, assignee,
                        Set.of(author));

        webTestClient.get().uri("/api/v1/tasks/{id}", TASK_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(expectedData);
    }

    @Test
    public void whenCreateTask_thenReturnNewTask() {
        StepVerifier.create(taskRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();

        TaskRequest newTask = new TaskRequest("new task", "new description",
                TaskStatus.TODO, USER_AUTHOR_ID, USER_ASSIGNEE_ID,
                Set.of(USER_AUTHOR_ID));

        webTestClient.post()
                .uri("/api/v1/tasks")
                .body(Mono.just(newTask), TaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(responseModel -> {
                    assertNotNull(responseModel.getId());
                });

        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

    }

    @Test
    public void whenUpdateTask_thenReturnUpdatedTask() {

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setStatus(TaskStatus.DONE);

        webTestClient.put()
                .uri("/api/v1/tasks/{id}", TASK_ID)
                .body(Mono.just(taskRequest), TaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(responseModel -> {
                    assertEquals(TaskStatus.DONE, responseModel.getStatus());
                });
    }

    @Test
    public void whenDeleteById_thenRemoveTask() {
        webTestClient.delete()
                .uri("/api/v1/tasks/{id}", TASK_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(taskRepository.count())
                .expectNext(0L)
                .expectComplete()
                .verify();
    }
}
