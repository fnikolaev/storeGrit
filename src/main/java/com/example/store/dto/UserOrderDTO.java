package com.example.store.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserOrderDTO {
    private Long id;
    private Date date;
    private Long total;
    private boolean status;

    public UserOrderDTO(Long id, Date date, Long total, boolean status) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.status = status;
    }
}
