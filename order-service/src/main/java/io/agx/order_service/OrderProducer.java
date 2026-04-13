package io.agx.order_service;

import io.agx.common.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public void sendOrder(OrderRequest request) {
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .build();

        kafkaTemplate.send("order-placed", order);
        log.info("Order sent to Kafka: {}", order);
    }
}