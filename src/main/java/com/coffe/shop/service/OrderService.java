package com.coffe.shop.service;

import com.coffe.shop.entity.Order;
import com.coffe.shop.entity.OrderStatus;

public interface OrderService {


        void publishEvent(OrderStatus event);

        Order findOrder(int id);


}
