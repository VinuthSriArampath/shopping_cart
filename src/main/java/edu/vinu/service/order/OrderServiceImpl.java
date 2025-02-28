package edu.vinu.service.order;

import edu.vinu.dto.OrderDto;
import edu.vinu.enums.OrderStatus;
import edu.vinu.exception.ResourceNotFoundException;
import edu.vinu.model.Cart;
import edu.vinu.model.Order;
import edu.vinu.model.OrderItem;
import edu.vinu.model.Product;
import edu.vinu.repository.order.OrderRepository;
import edu.vinu.repository.product.ProductRepository;
import edu.vinu.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this :: covertToDto).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems()
               .stream()
               .map(cartItem -> {
                   Product product = cartItem.getProduct();
                   product.setInventory(product.getInventory() - cartItem.getQuantity());
                   productRepository.save(product);
                   return new OrderItem(
                           order,
                           product,
                           cartItem.getQuantity(),
                           cartItem.getUnitPrice()
                   );
               }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders= orderRepository.findByUserId(userId);
        return orders.stream().map(this::covertToDto).toList();
    }

    @Override
    public OrderDto covertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }

}
