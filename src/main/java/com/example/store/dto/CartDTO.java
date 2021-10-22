package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    @Getter
    @Setter
    private Map<Object, Object> cart = new HashMap<>();
}
