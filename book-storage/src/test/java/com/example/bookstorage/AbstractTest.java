package com.example.bookstorage;

import com.example.bookstorage.entity.Book;
import com.example.bookstorage.entity.Category;
import com.example.bookstorage.mapper.BookMapper;
import com.example.bookstorage.model.BookRequest;
import com.example.bookstorage.model.BookResponse;
import com.example.bookstorage.repository.BookRepository;
import com.example.bookstorage.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
@Sql("classpath:db/init.sql")
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public class AbstractTest {

    public static final Long UPDATED_ID = 1534L;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected BookMapper bookMapper;

    @Autowired
    protected BookService bookService;

    @Autowired
    protected BookRepository bookRepository;

    @RegisterExtension
    protected static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    protected static PostgreSQLContainer postgreSQLContainer;

    @Container
    protected static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.0.12"))
            .withExposedPorts(6379)
            .withReuse(true);

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registeredProperties(DynamicPropertyRegistry registry) {

        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);

        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());

        registry.add("app.integration.base-url", wireMockServer::baseUrl);
    }

    @BeforeEach
    public void before() throws Exception {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        //stubClient();
    }

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    private void stubClient() throws Exception {
        Category category1 = new Category(1L, "Фантастика", new ArrayList<>());
        Category category2 = new Category(2L, "Класика", new ArrayList<>());
        List<Book> findAllResponseBody = new ArrayList<>();
        findAllResponseBody.add(new Book(1L, "Книга 1", "Автор 1", category1));
        findAllResponseBody.add(new Book(2L, "Книга 2", "Автор 2", category2));
        findAllResponseBody.add(new Book(3L, "Книга 3", "Автор 3", category2));
        wireMockServer.stubFor(WireMock.get("/api/v1/book")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(findAllResponseBody))
                        .withStatus(200)));

        Book findByNameAndAuthorResponseBody = new Book(1L, "Книга 1", "Автор 1", category1);
        wireMockServer.stubFor(WireMock.get(
                "/api/v1/book/?name=" + findByNameAndAuthorResponseBody.getName()
                + "&author=" + findByNameAndAuthorResponseBody.getAuthor())
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(findByNameAndAuthorResponseBody))
                        .withStatus(200)));

        BookRequest createRequest = new BookRequest();
        createRequest.setName("Книга 5");
        Book createResponseBody = new Book(4L, "Книга 5", "Автор 1", category1);
        wireMockServer.stubFor(WireMock.post("/api/v1/book")
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(createRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(createResponseBody))
                        .withStatus(201)));

        BookRequest updateRequest = new BookRequest();
        updateRequest.setName("Книга 11");
        BookResponse updateResponseBody = new BookResponse(UPDATED_ID, "Книга 11", "Автор 1", category1.getName());
        wireMockServer.stubFor(WireMock.put("/api/v1/book/" + UPDATED_ID)
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(updateRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(updateResponseBody))
                        .withStatus(200)));

        wireMockServer.stubFor(WireMock.delete("/api/v1/book/" + UPDATED_ID)
                .willReturn(aResponse().withStatus(204)));
    }
}
