package com.coffe.shop;

import com.coffe.shop.dto.*;
import com.coffe.shop.entity.Client;
import com.coffe.shop.entity.Employee;
import com.coffe.shop.entity.Order;
import com.coffe.shop.entity.Product;
import com.coffe.shop.repository.*;
import com.coffe.shop.service.CoffeeShopService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ShopApplication {
	private final ClientRepository clientRepository;
	private final EmployeeRepository employeeRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final CoffeeShopService service;

	public ShopApplication(ClientRepository clientRepository, EmployeeRepository employeeRepository, OrderRepository orderRepository, ProductRepository productRepository, CoffeeShopService service) {
		this.clientRepository = clientRepository;
		this.employeeRepository = employeeRepository;
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.service = service;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

	@Bean
	public CommandLineRunner CommandLineRunnerBean() {
		return args -> {

			Client client = new Client("Клиент");
			clientRepository.save(client);
			Employee employee = new Employee("Сотрудник 1");
			employeeRepository.save(employee);
			Employee employee2 = new Employee("Сотрудник 2");
			employeeRepository.save(employee2);
			Product product = new Product("Пицца");
			productRepository.save(product);
			Order order = new Order();
			orderRepository.save(order);

			OrderRegisteredDto registeredDto = new OrderRegisteredDto();
			registeredDto.setOrderId(order.getId());
			registeredDto.setClientId(client.getId());
			registeredDto.setEmployeeId(employee.getId());
			registeredDto.setProductId(product.getId());
			registeredDto.setCost("777");
			registeredDto.setEstimatedOrderTime(60);
			registeredDto.setDateTime(LocalDateTime.now());

			service.registerOrder(registeredDto);

			OrderInProgressDto orderInProgressDto = new OrderInProgressDto();
			orderInProgressDto.setOrderId(order.getId());
			orderInProgressDto.setEmployeeId(employee2.getId());
			orderInProgressDto.setDateTime(LocalDateTime.now());

			service.progressOrder(orderInProgressDto);

			OrderIsReadyDto orderIsReadyDto = new OrderIsReadyDto();
			orderIsReadyDto.setOrderId(order.getId());
			orderIsReadyDto.setEmployeeId(employee.getId());
			orderIsReadyDto.setDateTime(LocalDateTime.now());

			service.readyOrder(orderIsReadyDto);

			OrderIssuedDto orderIssuedDto = new OrderIssuedDto();
			orderIssuedDto.setOrderId(order.getId());
			orderIssuedDto.setEmployeeId(employee2.getId());
			orderIssuedDto.setDateTime(LocalDateTime.now());

			service.issueOrder(orderIssuedDto);

			Order orderTwo = new Order();
			orderRepository.save(orderTwo);
			registeredDto.setOrderId(orderTwo.getId());
			registeredDto.setCost("888");
			registeredDto.setEstimatedOrderTime(60);
			registeredDto.setDateTime(LocalDateTime.now());

			service.registerOrder(registeredDto);

			OrderCancelledDto orderCancelledDto = new OrderCancelledDto();
			orderCancelledDto.setOrderId(orderTwo.getId());
			orderCancelledDto.setEmployeeId(employee2.getId());
			orderCancelledDto.setDateTime(LocalDateTime.now());

			service.cancelOrder(orderCancelledDto);
			service.registerOrder(registeredDto);

		};
	}

}
