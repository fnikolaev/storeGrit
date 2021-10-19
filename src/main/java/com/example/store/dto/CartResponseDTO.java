package com.example.store.dto;

import lombok.Data;

@Data
public class CartResponseDTO {
    String title;
    int quantity;

    public CartResponseDTO(String title, int quantity) {
        this.title = title;
        this.quantity = quantity;
    }
}
