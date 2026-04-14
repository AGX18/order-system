package io.agx.notification_service;

import io.agx.common.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "order-placed", groupId = "notification-group")
    public void consume(Order order) {
        log.info("Sending notification for order: {}", order.getOrderId());
        log.info("Dear customer, your order for {} x{} has been placed successfully.",
                order.getProduct(), order.getQuantity());
    }
}