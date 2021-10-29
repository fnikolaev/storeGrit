package com.example.store.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
}