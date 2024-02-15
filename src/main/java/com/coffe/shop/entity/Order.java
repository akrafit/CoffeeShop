package com.coffe.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_list")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer productCount;

    private String price;

    private LocalDateTime dateTime;

    private Integer estimatedOrderTime;

    public Order(Client client, Employee employee, Product product, LocalDateTime now, int count, int estTime, String price) {
        this.client = client;
        this.employee = employee;
        this.estimatedOrderTime = estTime;
        this.dateTime = now;
        this.productCount = count;
        this.price = price;
    }
}
