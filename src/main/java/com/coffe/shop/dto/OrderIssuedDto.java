package com.coffe.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderIssuedDto {
    private Integer orderId;
    private Integer employeeId;
    private LocalDateTime dateTime;
}
