package com.coffe.shop.dto;

import com.coffe.shop.entity.Order;
import com.coffe.shop.entity.enums.OrderStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusDto {
    private Order order;

    private OrderStatusEnum orderStatusEnum;

    private String comment;

    private LocalDateTime dateTime;
}
