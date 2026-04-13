package io.agx.inventory_service;

import io.agx.common.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryConsumer {

    private final InventoryRepository inventoryRepository;

    @KafkaListener(topics = "order-placed", groupId = "inventory-group")
    public void consume(Order order) {
        log.info("Received order: {}", order);

        inventoryRepository.findByProduct(order.getProduct()).ifPresentOrElse(
                inventory -> {
                    inventory.setQuantity(inventory.getQuantity() - order.getQuantity());
                    inventoryRepository.save(inventory);
                    log.info("Updated stock for {}: new quantity = {}",
                            order.getProduct(), inventory.getQuantity());
                },
                () -> log.warn("Product not found: {}", order.getProduct())
        );
    }
}