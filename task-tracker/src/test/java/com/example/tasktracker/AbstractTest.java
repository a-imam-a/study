package com.example.tasktracker;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public abstract class AbstractTest {

    protected static String USER_AUTHOR_ID = UUID.randomUUID().toString();
    protected static String USER_ASSIGNEE_ID = UUID.randomUUID().toString();
    protected static String TASK_ID = UUID.randomUUID().toString();

    protected static Instant TASK_DATE = Instant.ofEpochSecond(0);

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TaskMapper taskMapper;

    @BeforeEach
    public void setup() {
        User author = new User(USER_AUTHOR_ID, "user1", "user1@user1");
        User assignee = new User(USER_ASSIGNEE_ID, "user2", "user2@user2");
        userRepository.saveAll(List.of(author, assignee)).collectList().block();
        taskRepository.save(new Task(TASK_ID, "task1", "description1",
                        TASK_DATE, TASK_DATE,
                        TaskStatus.TODO, USER_AUTHOR_ID, USER_ASSIGNEE_ID,
                        Set.of(USER_AUTHOR_ID),
                        author, assignee,
                        Set.of(author))
        ).block();
    }

    @AfterEach
    public void afterEach() {
        taskRepository.deleteAll().block();
        userRepository.deleteAll().block();
    }

}
