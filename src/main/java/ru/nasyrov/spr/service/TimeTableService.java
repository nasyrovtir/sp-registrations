package ru.nasyrov.spr.service;

import ru.nasyrov.spr.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface TimeTableService {

    List<OrderDTO> doReserve(ReserveOrderDTO reserve);

    void doCancel(CancelOrderDTO cancel);

    List<AvailableDTO> getAvailable(LocalDate date);

    List<AvailableDTO> getAll(LocalDate date);

    List<OrderInfoDTO> search(LocalDate date, String clientName);
}
