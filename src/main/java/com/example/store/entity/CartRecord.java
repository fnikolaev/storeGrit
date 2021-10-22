package com.example.store.entity;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@SessionScope
public class CartRecord {
    @Getter
    private final Map<Long, Long> records = new LinkedHashMap<>();
}
