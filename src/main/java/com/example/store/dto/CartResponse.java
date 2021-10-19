package com.example.store.dto;

import lombok.Data;

@Data
public class CartResponse {
    String title;
    int quantity;

    public CartResponse(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }
}
