package io.agx.order_service;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Data
@AllArgsConstructor
@Builder
public class Order {
    private String orderId;
    private String product;
    private int quantity;
}