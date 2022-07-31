package ru.nasyrov.spr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nasyrov.spr.dto.*;
import ru.nasyrov.spr.service.TimeTableService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "timetable", description = "Контроллер для обработки записей")
@RequestMapping("/api/v0/pool/timetable")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @Autowired
    public TimeTableController(TimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить запись клиента на определенное время")
    public List<OrderDTO> doReserve(@Valid @RequestBody ReserveOrderDTO reserve) {
        return timeTableService.doReserve(reserve);
    }

//    @GetMapping("/cancel")
    @DeleteMapping ("/cancel")
    @Operation(summary = "Отмена записи клиента на определенное время")
    public void doCancel(@Valid @RequestBody CancelOrderDTO cancel) {
        timeTableService.doCancel(cancel);
    }

    @GetMapping("/available")
    @Operation(summary = "Получение доступных записей на определенную дату")
    public List<AvailableDTO> getAvailable(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return timeTableService.getAvailable(date);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение занятых записей на определенную дату")
    public List<AvailableDTO> getAll(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return timeTableService.getAll(date);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск записей (по ФИО, дате посещения)")
    public List<OrderInfoDTO> search(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                     @RequestParam("clientName") String clientName) {
        return timeTableService.search(date, clientName);
    }
}
