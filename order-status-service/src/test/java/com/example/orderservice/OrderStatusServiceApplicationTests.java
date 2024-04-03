package com.example.orderservice;

import com.example.orderservice.event.OrderEvent;
import com.example.orderservice.event.OrderStatusEvent;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class OrderStatusServiceApplicationTests {

	@Container
	static final KafkaContainer kafka = new KafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
	);

	@DynamicPropertySource
	static void registryKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Autowired
	private KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

	@Value("${app.kafka.kafkaOrderStatusEventTopic}")
	private String topicName;

	@Test
	public void whenSendOrderEvent_thenHandleOrderEventByListener() {
		OrderStatusEvent event = new OrderStatusEvent("PROCESS", Instant.now());
		String key = UUID.randomUUID().toString();
		CompletableFuture future = kafkaTemplate.send(topicName, key, event);
		await()
				.pollInterval(Duration.ofSeconds(3))
				.atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> {
					SendResult result = (SendResult) future.get();
					assertThat(result.getProducerRecord().key()).isEqualTo(key);
					OrderStatusEvent receivedEvent = (OrderStatusEvent) result.getProducerRecord().value();
					assertThat(receivedEvent.getStatus()).isEqualTo(event.getStatus());
					assertThat(receivedEvent.getDate()).isEqualTo(event.getDate());
				});
	}
}
