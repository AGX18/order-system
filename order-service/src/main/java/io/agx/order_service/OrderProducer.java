package io.agx.order_service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrder(OrderRequest request) {
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .build();

        String message = order.getOrderId() + ":" + order.getProduct() + ":" + order.getQuantity();
        kafkaTemplate.send("order-placed", message);
        log.info("Order sent to Kafka: {}", message);
    }
}