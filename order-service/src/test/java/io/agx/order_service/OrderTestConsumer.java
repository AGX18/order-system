package io.agx.order_service;

import io.agx.common.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTestConsumer {

    private Order lastOrder;

    @KafkaListener(topics = "order-placed", groupId = "test-group")
    public void consume(Order order) {
        this.lastOrder = order;
    }

    public Order getLastOrder() {
        return lastOrder;
    }
}