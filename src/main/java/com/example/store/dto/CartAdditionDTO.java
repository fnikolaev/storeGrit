package com.example.store.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class CartAdditionDTO {
    @Min(1)
    private final Long id;
    @Min(1)
    private final Long quantity;
}
