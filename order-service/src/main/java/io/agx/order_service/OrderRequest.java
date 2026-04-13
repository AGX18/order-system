package io.agx.order_service;

import lombok.Data;

@Data
public class OrderRequest {
    private String product;
    private int quantity;
}