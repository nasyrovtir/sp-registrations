package ru.nasyrov.spr.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.nasyrov.spr.dto.ReserveOrderDTO;
import ru.nasyrov.spr.entity.Schedule;
import ru.nasyrov.spr.exception.ProcessingException;
import ru.nasyrov.spr.handler.RestrictionHandler;
import ru.nasyrov.spr.service.ScheduleService;

import java.time.LocalTime;

/**
 * Обработчик для проверки соответствия времени записи графику работы
 */
@Order(value = 2)
@Component
public class ScheduleRestrictionHandler implements RestrictionHandler {

    public static final String MESSAGE = "Ограничение по графику работы:";

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleRestrictionHandler(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Метод для проверки соответствия времени записи графику работы
     *
     * @param reserve входные данные
     * @throws ProcessingException если значение времени записи не соответствует графику работы
     */
    @Override
    public void checkRestriction(ReserveOrderDTO reserve) {
        Schedule schedule = scheduleService.getScheduleForDay(reserve.getDateTime().toLocalDate());

        LocalTime scheduleStartTime = schedule.getStartTime();
        LocalTime scheduleEndTime = schedule.getEndTime();

        if (reserve.getDateTime().toLocalTime().isBefore(scheduleStartTime)) {
            throw new ProcessingException(MESSAGE + " время записи раньше открытия");
        }

        if (reserve.getDateTimeEnd().toLocalTime().minusMinutes(1).isAfter(scheduleEndTime)) {
            throw new ProcessingException(MESSAGE + " конец записи позже закрытия");
        }
    }
}
