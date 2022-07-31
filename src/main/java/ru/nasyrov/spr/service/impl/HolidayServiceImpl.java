package ru.nasyrov.spr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nasyrov.spr.entity.Holiday;
import ru.nasyrov.spr.repository.HolidayRepository;
import ru.nasyrov.spr.service.HolidayService;

/**
 * Сервис для обработки праздничных дней
 */
@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository repository;

    @Autowired
    public HolidayServiceImpl(HolidayRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод для получения празничного дня по месяцу и дню
     * @param month месяц
     * @param dayOfMonth день
     * @return праздничный день
     */
    @Override
    public Holiday findByMonthAndDay(int month, int dayOfMonth) {
        return repository.findByMonthAndDay(month, dayOfMonth);
    }
}
