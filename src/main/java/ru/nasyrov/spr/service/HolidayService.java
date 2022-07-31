package ru.nasyrov.spr.service;

import ru.nasyrov.spr.entity.Holiday;

public interface HolidayService {

    Holiday findByMonthAndDay(int month, int dayOfMonth);
}
