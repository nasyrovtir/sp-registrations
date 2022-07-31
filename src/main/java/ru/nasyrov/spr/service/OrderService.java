package ru.nasyrov.spr.service;

import ru.nasyrov.spr.dto.OrderInfoDTO;
import ru.nasyrov.spr.entity.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<LocalDateTime, List<Order>> getOrdersPerHours(LocalDateTime startTime, LocalDateTime endTime);

    List<Order> getOrdersBetween(LocalDateTime startTime, LocalDateTime endTime);

    Order save(Order order);

    Order checkOrderExistsOrThrowException(String id);

    void delete(Order order);

    Map<LocalDateTime, Integer> getAvailable(LocalDateTime startTime, LocalDateTime endTime);

    List<OrderInfoDTO> getOrdersByDateAndClientName(LocalDate date, String clientName);
}
