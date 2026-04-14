package io.agx.inventory_service;

import io.agx.common.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@TestPropertySource(properties = {
        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer"
})
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class InventoryConsumerTest {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void whenOrderReceived_thenStockIsUpdated() {
        Order order = Order.builder()
                .orderId("test-123")
                .product("Laptop")
                .quantity(5)
                .build();

        kafkaTemplate.send("order-placed", order);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            Inventory inventory = inventoryRepository
                    .findByProduct("Laptop")
                    .orElseThrow();
            assertThat(inventory.getQuantity()).isEqualTo(95);
        });
    }
}