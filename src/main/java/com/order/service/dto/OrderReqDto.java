package com.order.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {
    @NotNull
    @NotBlank
    private String customerId;
    @NotNull
    @NotBlank
    private String product;
    private double amount;
}
