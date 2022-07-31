package ru.nasyrov.spr.service;

import ru.nasyrov.spr.dto.AddClientDTO;
import ru.nasyrov.spr.dto.ClientDTO;
import ru.nasyrov.spr.dto.AllClientDTO;
import ru.nasyrov.spr.entity.Client;

import java.util.List;

public interface ClientService {

    Client checkClientExistsOrThrowException(Long id);

    List<AllClientDTO> getClients();

    ClientDTO getClient(Long id);

    void addClient(AddClientDTO clientDTO);

    void updateClient(ClientDTO clientDTO);
}
