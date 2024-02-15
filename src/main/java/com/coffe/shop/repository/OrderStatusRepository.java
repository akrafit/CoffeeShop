package com.coffe.shop.repository;

import com.coffe.shop.entity.Order;
import com.coffe.shop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus,Integer> {

    List<OrderStatus> findOrderStatusByOrder(Order order);
}
