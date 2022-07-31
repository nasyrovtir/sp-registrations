package ru.nasyrov.spr.handler;

import ru.nasyrov.spr.dto.ReserveOrderDTO;

public interface RestrictionHandler {

    void checkRestriction(ReserveOrderDTO reserve);
}
