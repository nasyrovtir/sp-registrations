package ru.nasyrov.spr.service;

import ru.nasyrov.spr.entity.Schedule;

import java.time.LocalDate;

public interface ScheduleService {

    Schedule findById(Long id);

    Schedule getScheduleForDay(LocalDate date);
}
