package ru.nasyrov.spr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nasyrov.spr.component.MapstructMapper;
import ru.nasyrov.spr.dto.*;
import ru.nasyrov.spr.entity.Client;
import ru.nasyrov.spr.entity.Order;
import ru.nasyrov.spr.entity.Schedule;
import ru.nasyrov.spr.exception.ProcessingException;
import ru.nasyrov.spr.handler.RestrictionHandler;
import ru.nasyrov.spr.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис для обработки расписания
 */
@Service
public class TimeTableServiceImpl implements TimeTableService {

    private final ClientService clientService;

    private final OrderService orderService;

    private final ScheduleService scheduleService;

    private final List<RestrictionHandler> handlers;

    private final MapstructMapper mapper;

    @Autowired
    public TimeTableServiceImpl(ClientService clientService, OrderService orderService, ScheduleService scheduleService, List<RestrictionHandler> handlers, MapstructMapper mapper) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.scheduleService = scheduleService;
        this.handlers = handlers;
        this.mapper = mapper;
    }

    /**
     * Метод для создания и сохранения в БД записи клиента {@link Order}
     * после проверки на наличие ограничений {@link RestrictionHandler}
     *
     * @param reserve входные данные (id клиента, время старта и окончания (может быть null))
     * @return идентификатор записи клиента (DTO)
     */
    @Override
    @Transactional
    public List<OrderDTO> doReserve(ReserveOrderDTO reserve) {
        handlers.forEach(h -> h.checkRestriction(reserve));

        Client client = clientService.checkClientExistsOrThrowException(reserve.getClientId());

        return Stream.iterate(reserve.getDateTime(), t -> t.plusHours(1))
                .limit(ChronoUnit.HOURS.between(reserve.getDateTime(), reserve.getDateTimeEnd()))
                .map(time -> new Order(time, client))
                .map(orderService::save)
                .map(mapper::orderToOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     * Метод для удаления записи клиента {@link Order} из БД
     *
     * @param cancel входные данные (id клиента и id записи клиента)
     */
    @Override
    public void doCancel(CancelOrderDTO cancel) {
        Order order = orderService.checkOrderExistsOrThrowException(cancel.getOrderId());
        if (order.getClient().getId().equals(cancel.getClientId())) {
            orderService.delete(order);
        } else {
            throw new ProcessingException("ClientId не соответствует OrderId");
        }
    }

    /**
     * Метод для получения доступных записей {@link Order} на определенную дату
     *
     * @param date дата
     * @return время и количество доступных записей (DTO)
     */
    @Override
    public List<AvailableDTO> getAvailable(LocalDate date) {
        Schedule schedule = scheduleService.getScheduleForDay(date);
        Map<LocalDateTime, Integer> available = orderService.getAvailable(date.atTime(schedule.getStartTime()), date.atTime(schedule.getEndTime()));

        return available.entrySet().stream().sorted(Map.Entry.comparingByKey())
                                            .map(entry -> new AvailableDTO(entry.getKey(), entry.getValue()))
                                            .collect(Collectors.toList());
    }

    /**
     * Метод для получение занятых записей {@link Order} на определенную дату
     *
     * @param date дата
     * @return время и количество занятых записей (DTO)
     */
    @Override
    public List<AvailableDTO> getAll(LocalDate date) {
        Map<LocalDateTime, List<Order>> ordersPerHours = orderService.getOrdersPerHours(date.atStartOfDay(), date.atTime(LocalTime.MAX));

        return ordersPerHours.entrySet().stream().sorted(Map.Entry.comparingByKey())
                                                 .map(entry -> new AvailableDTO(entry.getKey(), entry.getValue().size()))
                                                 .collect(Collectors.toList());
    }

    /**
     * Метод для поиска записей {@link Order} (по ФИО, дате посещения)
     *
     * @param date дата
     * @param clientName ФИО клиента - может быть введено частично: поиск по шаблону
     * @return список идентификаторов записей и имен клиента (DTO)
     */
    @Override
    public List<OrderInfoDTO> search(LocalDate date, String clientName) {
        return orderService.getOrdersByDateAndClientName(date, clientName);
    }
}
