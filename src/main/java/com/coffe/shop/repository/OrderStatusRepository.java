package com.coffe.shop.repository;

import com.coffe.shop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus,Integer> {
    List<OrderStatus> findOrderStatusByOrder_Id(Integer orderId);
    @Query(nativeQuery = true,value = "SELECT * FROM order_status WHERE  order_id = :orderId ORDER BY date_time DESC")
    List<OrderStatus> findOrderStatusByOrderIdOrderByDateTimeDateTimeDesc(Integer orderId);

}
