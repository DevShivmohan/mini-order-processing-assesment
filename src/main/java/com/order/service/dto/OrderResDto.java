package com.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResDto {
    private String orderId;
    private String status;
}
