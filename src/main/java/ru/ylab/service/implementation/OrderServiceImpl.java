package ru.ylab.service.implementation;

import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.dto.mapper.OrderMapper;
import ru.ylab.domain.model.Order;
import ru.ylab.repository.OrderRepository;
import ru.ylab.service.OrderService;
import ru.ylab.util.ResourceNotFoundException;
import ru.ylab.util.ValidationUtil;


import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(int userId) {
        return orderRepository.findByClientId(userId).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        ValidationUtil.validateOrderDTO(orderDTO);

        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    @Override
    public OrderDTO updateOrder(int id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + id + " not found."));

        orderMapper.updateEntityFromDTO(orderDTO, existingOrder);
        orderRepository.save(existingOrder);
        return orderMapper.toDTO(existingOrder);
    }

    @Override
    public void deleteOrder(int id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + id + " not found."));
        orderRepository.delete(existingOrder.getId());
    }

}
