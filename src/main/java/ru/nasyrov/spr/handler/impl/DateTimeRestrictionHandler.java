package ru.nasyrov.spr.handler.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.nasyrov.spr.dto.ReserveOrderDTO;
import ru.nasyrov.spr.exception.ProcessingException;
import ru.nasyrov.spr.handler.RestrictionHandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Обработчик для валидации времени записи
 */
@Order(value = 1)
@Component
public class DateTimeRestrictionHandler implements RestrictionHandler {

    public static final String MESSAGE = "Ограничение по валидации времени регистрации:";

    /**
     * Метод для валидации времени записи
     *
     * @param reserve входные данные
     * @throws ProcessingException если значение времени не прошло валидацию
     */
    @Override
    public void checkRestriction(ReserveOrderDTO reserve) {
        if (reserve.getDateTimeEnd() == null) {
            reserve.setDateTimeEnd(reserve.getDateTime().plusHours(1));
        }

        LocalDateTime start = reserve.getDateTime();
        LocalDateTime end = reserve.getDateTimeEnd();

        if (LocalDateTime.now().isAfter(start)) {
            throw new ProcessingException(MESSAGE + " время записи раньше текущего");
        }

        if (start.isAfter(end) || start.isEqual(end)) {
            throw new ProcessingException(MESSAGE + " конец записи раньше начала");
        }

        if (!start.truncatedTo(ChronoUnit.HOURS).isEqual(start) || !end.truncatedTo(ChronoUnit.HOURS).isEqual(end)) {
            throw new ProcessingException(MESSAGE + " дискретность записи - 1 час");
        }

        if (!start.toLocalDate().isEqual(end.toLocalDate())) {
            throw new ProcessingException(MESSAGE + " запись на разные дни невозможна");
        }
    }
}
