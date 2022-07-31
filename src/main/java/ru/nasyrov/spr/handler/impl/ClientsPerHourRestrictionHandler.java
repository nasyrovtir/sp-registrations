package ru.nasyrov.spr.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nasyrov.spr.dto.ReserveOrderDTO;
import ru.nasyrov.spr.entity.Order;
import ru.nasyrov.spr.exception.ProcessingException;
import ru.nasyrov.spr.handler.RestrictionHandler;
import ru.nasyrov.spr.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Обработчик для ограничения количества записей в час
 */
@org.springframework.core.annotation.Order(value = 3)
@Component
public class ClientsPerHourRestrictionHandler implements RestrictionHandler {

    @Value("${default.limit.orders-per-hour}")
    private Long defaultLimitOrdersPerHour;

    private final OrderService orderService;

    @Autowired
    public ClientsPerHourRestrictionHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Метод для ограничения количества записей в час
     *
     * @param reserve входные данные
     * @throws ProcessingException если количество записей в час будет выше допустимого
     */
    @Override
    public void checkRestriction(ReserveOrderDTO reserve) {
        Map<LocalDateTime, List<Order>> ordersPerHours = orderService.getOrdersPerHours(reserve.getDateTime(), reserve.getDateTimeEnd());
        ordersPerHours.forEach((key, value) -> {
            if (value.size() >= defaultLimitOrdersPerHour)
                throw new ProcessingException("Ограничение на количество записей в час");
        });
    }
}
