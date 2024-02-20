package com.coffe.shop.service;

import com.coffe.shop.dto.*;
import com.coffe.shop.entity.Order;
import com.coffe.shop.entity.OrderStatus;
import com.coffe.shop.entity.enums.Status;
import com.coffe.shop.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;



@Slf4j
@Service
@Transactional
public class CoffeeShopService implements OrderService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ProductRepository productRepository;

    public CoffeeShopService(ClientRepository clientRepository, EmployeeRepository employeeRepository, OrderRepository orderRepository, OrderStatusRepository orderStatusRepository, ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.productRepository = productRepository;
    }

    public void registerOrder(OrderRegisteredDto registeredDto) {
        OrderStatus orderStatusLast = getLastOrderStatus(orderStatusRepository.findOrderStatusByOrder_Id(registeredDto.getOrderId()));
        if (orderStatusLast == null) {
            Order order = findOrder(registeredDto.getOrderId());
            order.setClient(clientRepository.getReferenceById(registeredDto.getClientId()));
            order.setProduct(productRepository.getReferenceById(registeredDto.getProductId()));
            order.setEstimatedOrderTime(registeredDto.getEstimatedOrderTime());
            order.setDateTime(LocalDateTime.now());
            order.setCost(registeredDto.getCost());

            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setOrder(order);
            orderStatus.setEmployee(employeeRepository.getReferenceById(registeredDto.getEmployeeId()));
            orderStatus.setStatus(Status.ORDER_REGISTERED);
            publishEvent(orderStatus);
            log.info("Event was successfully registered by {}, order {} registered", orderStatus.getEmployee().getName(), order.getId());
        } else {
            log.info("Can't save order status, order {} was registered early", registeredDto.getOrderId());
        }

    }

    public void progressOrder(OrderInProgressDto orderInProgressDto) {
        OrderStatus orderStatus = getLastOrderStatus(orderStatusRepository.findOrderStatusByOrderIdOrderByDateTimeDateTimeDesc(orderInProgressDto.getOrderId()));
        if (orderStatus == null || checkOrderStatus(orderStatus)) {
            log.info("Can't find order status for order {}, or order don't registered early", orderInProgressDto.getOrderId());
            return;
        }
        if (orderStatus.getStatus().equals(Status.ORDER_REGISTERED)) {
            Order order = findOrder(orderInProgressDto.getOrderId());
            OrderStatus orderStatusNew = new OrderStatus();
            orderStatusNew.setOrder(order);
            orderStatusNew.setEmployee(employeeRepository.getReferenceById(orderInProgressDto.getEmployeeId()));
            orderStatusNew.setStatus(Status.ORDER_IN_PROGRESS);
            publishEvent(orderStatusNew);
            log.info("Event was successfully registered by {}, order {} in progress", orderStatusNew.getEmployee().getName(), order.getId());
        }
    }

    public void readyOrder(OrderIsReadyDto orderIsReadyDto) {
        OrderStatus orderStatusLast = getLastOrderStatus(orderStatusRepository.findOrderStatusByOrderIdOrderByDateTimeDateTimeDesc(orderIsReadyDto.getOrderId()));
        if (orderStatusLast == null || checkOrderStatus(orderStatusLast)) {
            log.info("Can't find order status for order {}, or order status don't in progress", orderIsReadyDto.getOrderId());
            return;
        }
        if (orderStatusLast.getStatus().equals(Status.ORDER_IN_PROGRESS)) {
            Order order = findOrder(orderIsReadyDto.getOrderId());
            OrderStatus orderStatusNew = new OrderStatus();
            orderStatusNew.setOrder(order);
            orderStatusNew.setEmployee(employeeRepository.getReferenceById(orderIsReadyDto.getEmployeeId()));
            orderStatusNew.setStatus(Status.ORDER_IS_READY);
            publishEvent(orderStatusNew);
            log.info("Event was successfully registered by {}, order {} is ready", orderStatusNew.getEmployee().getName(), order.getId());
        }
    }

    public void cancelOrder(OrderCancelledDto orderCancelledDto) {
        OrderStatus orderStatusLast = getLastOrderStatus(orderStatusRepository.findOrderStatusByOrderIdOrderByDateTimeDateTimeDesc(orderCancelledDto.getOrderId()));
        if (orderStatusLast == null || checkOrderStatus(orderStatusLast)) {
            log.info("Can't find order status for order {}, order status cancelled or issued", orderCancelledDto.getOrderId());
            return;
        }
        Order order = findOrder(orderCancelledDto.getOrderId());
        OrderStatus orderStatusNew = new OrderStatus();
        orderStatusNew.setOrder(order);
        orderStatusNew.setEmployee(employeeRepository.getReferenceById(orderCancelledDto.getEmployeeId()));
        orderStatusNew.setStatus(Status.ORDER_CANCELLED);
        orderStatusNew.setComment(orderCancelledDto.getReasonForCancellation());
        publishEvent(orderStatusNew);
        log.info("Event was successfully registered by {}, order {} is cancelled", orderStatusNew.getEmployee().getName(), order.getId());
    }

    public void issueOrder(OrderIssuedDto orderIssuedDto) {
        OrderStatus orderStatusLast = getLastOrderStatus(orderStatusRepository.findOrderStatusByOrderIdOrderByDateTimeDateTimeDesc(orderIssuedDto.getOrderId()));
        if (orderStatusLast == null || checkOrderStatus(orderStatusLast)) {
            log.info("Can't find order status for order {}, order status cancelled or issued", orderIssuedDto.getOrderId());
            return;
        }

        Order order = findOrder(orderIssuedDto.getOrderId());
        OrderStatus orderStatusNew = new OrderStatus();
        orderStatusNew.setOrder(order);
        orderStatusNew.setEmployee(employeeRepository.getReferenceById(orderIssuedDto.getEmployeeId()));
        orderStatusNew.setStatus(Status.ORDER_WAS_ISSUED);
        publishEvent(orderStatusNew);
        log.info("Event was successfully registered by {}, order {} was issued", orderStatusNew.getEmployee().getName(), order.getId());
    }

    private boolean checkOrderStatus(OrderStatus orderStatus) {
        if (orderStatus.getStatus().equals(Status.ORDER_CANCELLED) || orderStatus.getStatus().equals(Status.ORDER_WAS_ISSUED)) {
            log.info("Can't change status: {} , order {} was cancelled or was issued", orderStatus.getStatus(), orderStatus.getOrder().getId());
            return true;
        } else {
            return false;
        }
    }

    private OrderStatus getLastOrderStatus(List<OrderStatus> orderStatusList) {
        if (orderStatusList.size() > 0) {
            return orderStatusList.get(orderStatusList.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public void publishEvent(OrderStatus event) {
        orderStatusRepository.save(event);
    }


    @Override
    public Order findOrder(int id) {
        return orderRepository.getReferenceById(id);
    }

}
