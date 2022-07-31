package ru.nasyrov.spr.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nasyrov.spr.dto.ReserveOrderDTO;
import ru.nasyrov.spr.entity.Order;
import ru.nasyrov.spr.exception.ProcessingException;
import ru.nasyrov.spr.handler.RestrictionHandler;
import ru.nasyrov.spr.service.OrderService;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * Обработчик для ограничения количества записей в день на человека
 */
@org.springframework.core.annotation.Order(value = 4)
@Component
public class ClientPerDayRestrictionHandler implements RestrictionHandler {

    @Value("${default.limit.orders-per-client}")
    private Long defaultLimitOrdersPerClient;

    private final OrderService orderService;

    @Autowired
    public ClientPerDayRestrictionHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Метод для ограничения количества записей в день на человека
     *
     * @param reserve входные данные
     * @throws ProcessingException если количество записей в день на человека будет выше допустимого
     */
    @Override
    public void checkRestriction(ReserveOrderDTO reserve) {
        List<Order> ordersPerDay = orderService.getOrdersBetween(reserve.getDateTime().toLocalDate().atStartOfDay(), reserve.getDateTime().toLocalDate().atTime(LocalTime.MAX));
        long persistOrderCountPerClientPerDay = ordersPerDay.stream().map(Order::getClient).filter(c -> Objects.equals(c.getId(), reserve.getClientId())).count();
        long reserveOrderCountPerClientPerDay = ChronoUnit.HOURS.between(reserve.getDateTime(), reserve.getDateTimeEnd());

        if (persistOrderCountPerClientPerDay + reserveOrderCountPerClientPerDay > defaultLimitOrdersPerClient) {
            throw new ProcessingException("Ограничение на количество записей в день на человека");
        }
    }
}
