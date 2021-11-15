package com.example.store.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * DTO for changing <b>quantity</b> in cart by <b>id</b>.
 */
@Data
public class CartAdditionDTO {
    @Min(1)
    private final Long id;
    @Min(1)
    private final Long quantity;
}
