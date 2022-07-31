package ru.nasyrov.spr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.nasyrov.spr.entity.Holiday;
import ru.nasyrov.spr.entity.Schedule;
import ru.nasyrov.spr.repository.ScheduleRepository;
import ru.nasyrov.spr.service.HolidayService;
import ru.nasyrov.spr.service.ScheduleService;

import java.time.LocalDate;

/**
 * Сервис для обработки графиков работы
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Value("${default.schedule.value}")
    private Long defaultScheduleId;

    private final HolidayService holidayService;

    private final ScheduleRepository repository;

    @Autowired
    public ScheduleServiceImpl(HolidayService holidayService, ScheduleRepository repository) {
        this.holidayService = holidayService;
        this.repository = repository;
    }

    /**
     * Метод для поиска графика работы по id
     *
     * @param id идентификатор графика работы
     * @return график работы
     */
    @Override
    public Schedule findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Метод для поиска графика работы на конкретную дату
     *
     * @param date дата
     * @return график работы
     */
    @Override
    public Schedule getScheduleForDay(LocalDate date) {
        Holiday holiday = holidayService.findByMonthAndDay(date.getMonth().getValue(), date.getDayOfMonth());
        Schedule schedule;

        if (holiday == null) {
            schedule = findById(defaultScheduleId);
            if (schedule == null) {
                throw new NotFoundException("Не определен график работы");
            }
        } else {
            schedule = holiday.getSchedule();
        }
        return schedule;
    }
}
