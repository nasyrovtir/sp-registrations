package ru.nasyrov.spr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.nasyrov.spr.dto.OrderInfoDTO;
import ru.nasyrov.spr.entity.Order;
import ru.nasyrov.spr.repository.OrderRepository;
import ru.nasyrov.spr.service.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис для обработки записей клиентов
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${default.limit.orders-per-hour}")
    private Integer defaultLimitOrdersPerHour;

    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод для определения количества записей в час
     *
     * @param startTime время начала
     * @param endTime время окончания
     * @return Map: время - список записей за это время
     */
    @Override
    public Map<LocalDateTime, List<Order>> getOrdersPerHours(LocalDateTime startTime, LocalDateTime endTime) {
        return getOrdersBetween(startTime, endTime).stream().collect(Collectors.groupingBy(Order::getStartTime));
    }

    /**
     * Метод для получения всех записей за интервал времени
     *
     * @param startTime время начала
     * @param endTime время окончания
     * @return список записей за интервал времени
     */
    @Override
    public List<Order> getOrdersBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return repository.findAllByStartTimeBetween(startTime, endTime);
    }

    /**
     * Метод для сохранения записи
     *
     * @param order запись
     * @return сохраненная запись
     */
    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    /**
     * Метод для проверки наличия в БД записи
     *
     * @param id идентификатор записи
     * @throws NotFoundException если запись не найдена
     * @return сохраненная запись
     */
    @Override
    public Order checkOrderExistsOrThrowException(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Запись не найдена"));
    }

    /**
     * Метод для удаления записи из БД
     * @param order клиента
     */
    @Override
    public void delete(Order order) {
        repository.delete(order);
    }

    /**
     * Метод для получения доступных записей за интервал времени
     *
     * @param startTime время начала
     * @param endTime время окончания
     * @return Map: время и количество доступных записей
     */
    @Override
    public Map<LocalDateTime, Integer> getAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        Map<LocalDateTime, Integer> available = Stream.iterate(startTime, t -> t.plusHours(1))
                                                       .limit(ChronoUnit.HOURS.between(startTime, endTime) + 1)
                                                       .collect(Collectors.toMap(Function.identity(), v -> defaultLimitOrdersPerHour));

        getOrdersPerHours(startTime, endTime).forEach((key, value) -> available.merge(key, value.size(), (oldVal, newVal) -> oldVal - newVal));
        return available;
    }

    /**
     * Метод для поиска записей (по ФИО, дате посещения)
     *
     * @param date дата
     * @param clientName ФИО клиента - может быть введено частично: поиск по шаблону
     * @return список идентификаторов записей и имен клиента
     */
    @Override
    public List<OrderInfoDTO> getOrdersByDateAndClientName(LocalDate date, String clientName) {
        return repository.getOrdersByDateBetweenAndClientName(date.atStartOfDay(), date.atTime(LocalTime.MAX), clientName);
    }
}
