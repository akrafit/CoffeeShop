package com.coffe.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class OrderRegisteredDto {
    private Integer orderId;
    private Integer clientId;
    private Integer employeeId;
    private Integer estimatedOrderTime;
    private Integer productId;
    private String cost;
    private LocalDateTime dateTime;
}
