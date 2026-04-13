package io.agx.order_service;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderProducer orderProducer;

    @PostMapping
    public String placeOrder(@RequestBody OrderRequest order) {
        log.info("Received order: {}", order);
        orderProducer.sendOrder(order);
        return "Order placed successfully!";
    }
}