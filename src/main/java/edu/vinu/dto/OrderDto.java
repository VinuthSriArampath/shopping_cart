package edu.vinu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String orderStatus;
    private List<OrderItemDto> items ;
}
