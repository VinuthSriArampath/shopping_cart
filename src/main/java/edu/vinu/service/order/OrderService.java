package edu.vinu.service.order;


import edu.vinu.dto.OrderDto;
import edu.vinu.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto covertToDto(Order order);
}
