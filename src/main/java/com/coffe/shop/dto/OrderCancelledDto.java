package com.coffe.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderCancelledDto {
    private Integer orderId;
    private Integer employeeId;
    private String reasonForCancellation;
    private LocalDateTime dateTime;
}
