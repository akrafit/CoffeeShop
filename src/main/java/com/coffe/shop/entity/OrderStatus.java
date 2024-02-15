package com.coffe.shop.entity;

import com.coffe.shop.dto.OrderStatusDto;
import com.coffe.shop.entity.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private OrderStatusEnum orderStatusEnum;

    private String comment;

    private LocalDateTime dateTime;

    public OrderStatus(OrderStatusDto event) {
        this.order = event.getOrder();
        this.orderStatusEnum = event.getOrderStatusEnum();
        this.comment = event.getComment();
        this.dateTime = LocalDateTime.now();
    }
    public OrderStatus(OrderStatus event) {
        this.order = event.getOrder();
        this.orderStatusEnum = event.getOrderStatusEnum();
        this.comment = event.getComment();
        this.dateTime = LocalDateTime.now();
    }

}
