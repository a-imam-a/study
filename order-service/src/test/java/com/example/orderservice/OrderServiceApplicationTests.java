package com.example.orderservice;

import com.example.orderservice.event.OrderEvent;
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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class OrderServiceApplicationTests {

	@Container
	static final KafkaContainer kafka = new KafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
	);

	@DynamicPropertySource
	static void registryKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	@Value("${app.kafka.kafkaOrderEventTopic}")
	private String topicName;

	@Test
	public void whenSendOrderEvent_thenHandleOrderEventByListener() {
		OrderEvent event = new OrderEvent();
		event.setProduct("product");
		event.setQuantity(1);
		String key = UUID.randomUUID().toString();
		CompletableFuture future = kafkaTemplate.send(topicName, key, event);
		await()
				.pollInterval(Duration.ofSeconds(3))
				.atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> {
					SendResult result = (SendResult) future.get();
					assertThat(result.getProducerRecord().key()).isEqualTo(key);
					OrderEvent receivedEvent = (OrderEvent) result.getProducerRecord().value();
					assertThat(receivedEvent.getProduct()).isEqualTo(event.getProduct());
					assertThat(receivedEvent.getQuantity()).isEqualTo(event.getQuantity());
				});
	}
}
