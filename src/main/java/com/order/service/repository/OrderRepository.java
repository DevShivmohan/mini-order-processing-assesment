package com.order.service.repository;

import com.order.service.file.FileOperationService;
import com.order.service.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
public class OrderRepository {

    /**
     * orderId -> order
     */
    private final Map<String, Order> orderMap = new ConcurrentHashMap<>();

    public Order save(Order order) {
        order.setOrderId(UUID.randomUUID().toString()).setCreatedAt(LocalDateTime.now().toString());
        orderMap.put(order.getOrderId(), order);
        FileOperationService.writeIntoFile(order);
        return order;
    }

    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orderMap.get(orderId));
    }

    public List<Order> findByCustomerId(String customerId) {
        return orderMap.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .toList();
    }

    public List<Order> findAll() {
        return orderMap.values().stream().toList();
    }
}
