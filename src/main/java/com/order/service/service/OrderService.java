package com.order.service.service;

import com.order.service.dto.OrderReqDto;
import com.order.service.exception.GenericException;
import com.order.service.model.Order;
import com.order.service.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order create(OrderReqDto orderReqDto) {
        if (orderReqDto.getAmount() <= 0) {
            throw new GenericException(HttpStatus.BAD_REQUEST.value(), "Invalid amount");
        }
        return orderRepository.save(Order.builder().product(orderReqDto.getProduct()).customerId(orderReqDto.getCustomerId()).amount(orderReqDto.getAmount()).build());
    }

    public Order findById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND.value(), "Order not found"));
    }

    public Order findByIdAndUserId(String orderId, String userId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND.value(), "Order not found"));
        if (!order.getCustomerId().equals(userId)) {
            throw new GenericException(HttpStatus.NOT_FOUND.value(), "Order not found");
        }
        return order;
    }

    public List<Order> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByUserId(String userId) {
        return orderRepository.findAll().stream().filter(order -> order.getCustomerId().equals(userId)).toList();
    }
}
