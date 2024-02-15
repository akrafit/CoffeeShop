package com.coffe.shop.service;

import com.coffe.shop.dto.OrderStatusDto;
import com.coffe.shop.entity.*;
import com.coffe.shop.entity.enums.OrderStatusEnum;
import com.coffe.shop.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
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

    @Override
    public void publishEvent(OrderStatus event) {
        List<OrderStatus> orderStatusList = orderStatusRepository.findOrderStatusByOrder(event.getOrder());
        if (orderStatusList.size() == 0 && event.getOrderStatusEnum().equals(OrderStatusEnum.ORDER_REGISTERED)) {
            log.info("Order {} registered",event.getOrder().getId());
            orderStatusRepository.save(new OrderStatus(event));
        } else {
            for (OrderStatus orderStatus : orderStatusList) {
                log.info(orderStatus.getOrderStatusEnum().toString());
                //System.out.println((orderStatus.getOrderStatusEnum().equals(OrderStatusEnum.ORDER_CANCELLED)));
                if (orderStatus.getOrderStatusEnum().equals(OrderStatusEnum.ORDER_CANCELLED) || orderStatus.getOrderStatusEnum().equals(OrderStatusEnum.ORDER_WAS_ISSUED)) {
                    log.info("Can't change status: {} , order {} was cancelled or was issued", event.getOrderStatusEnum(), event.getOrder().getId());
                    return;
                }
            }
            orderStatusRepository.save(new OrderStatus(event));
            log.info("Order {} change status: {}", event.getOrder().getId(), event.getOrderStatusEnum());
        }
    }

    @Override
    public Order findOrder(int id) {
        return orderRepository.getReferenceById(id);
    }

    @Bean
	public CommandLineRunner CommandLineRunnerBean() {
        return args -> {
            Client client = new Client("Клиент");
            clientRepository.save(client);
            Employee employee = new Employee("Сотрудник");
            employeeRepository.save(employee);
            Product product = new Product("Пицца");
            productRepository.save(product);

            Order order = new Order(client,employee,product, LocalDateTime.now(),1, 60, "777");
            order.setClient(client);
            order.setEmployee(employee);
            order.setProduct(product);
            orderRepository.save(order);

            OrderStatusDto orderStatusdto = new OrderStatusDto();
            orderStatusdto.setOrder(order);

            orderStatusdto.setOrderStatusEnum(OrderStatusEnum.ORDER_REGISTERED);
            orderStatusdto.setDateTime(LocalDateTime.now());
            publishEvent(new OrderStatus(orderStatusdto));

            orderStatusdto.setOrderStatusEnum(OrderStatusEnum.ORDER_IN_PROGRESS);
            orderStatusdto.setDateTime(LocalDateTime.now());
            publishEvent(new OrderStatus(orderStatusdto));

            orderStatusdto.setOrderStatusEnum(OrderStatusEnum.ORDER_IS_READY);
            orderStatusdto.setDateTime(LocalDateTime.now());
            publishEvent(new OrderStatus(orderStatusdto));

            orderStatusdto.setOrderStatusEnum(OrderStatusEnum.ORDER_WAS_ISSUED);
            orderStatusdto.setDateTime(LocalDateTime.now());
            publishEvent(new OrderStatus(orderStatusdto));

            order = new Order(client,employee,product, LocalDateTime.now(),2, 60, "888");
            order.setClient(client);
            order.setEmployee(employee);
            order.setProduct(product);
            orderRepository.save(order);

            orderStatusdto = new OrderStatusDto();
            orderStatusdto.setOrder(order);

            orderStatusdto.setOrderStatusEnum(OrderStatusEnum.ORDER_REGISTERED);
            orderStatusdto.setDateTime(LocalDateTime.now());
            publishEvent(new OrderStatus(orderStatusdto));

            orderStatusdto.setOrderStatusEnum(OrderStatusEnum.ORDER_CANCELLED);
            orderStatusdto.setDateTime(LocalDateTime.now());
            publishEvent(new OrderStatus(orderStatusdto));

            orderStatusdto.setOrderStatusEnum(OrderStatusEnum.ORDER_REGISTERED);
            orderStatusdto.setDateTime(LocalDateTime.now());
            publishEvent(new OrderStatus(orderStatusdto));

        };
    }

}
