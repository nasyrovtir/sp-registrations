package ru.nasyrov.spr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nasyrov.spr.entity.Holiday;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Holiday findByMonthAndDay(int month, int dayOfMonth);
}
