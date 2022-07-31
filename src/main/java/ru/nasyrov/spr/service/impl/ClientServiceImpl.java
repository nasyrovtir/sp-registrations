package ru.nasyrov.spr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.nasyrov.spr.component.MapstructMapper;
import ru.nasyrov.spr.dto.AddClientDTO;
import ru.nasyrov.spr.dto.ClientDTO;
import ru.nasyrov.spr.dto.AllClientDTO;
import ru.nasyrov.spr.entity.Client;
import ru.nasyrov.spr.repository.ClientRepository;
import ru.nasyrov.spr.service.ClientService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для обработки клиентов
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    private final MapstructMapper mapper;

    @Autowired
    public ClientServiceImpl(ClientRepository repository, MapstructMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Метод для проверки наличия в БД клиента
     *
     * @param id идентификатор клиента
     * @throws NotFoundException если клиент не найден
     * @return сохраненный клиент
     */
    @Override
    public Client checkClientExistsOrThrowException(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Клиент не найден"));
    }

    /**
     * Метод для получения всех клиентов
     *
     * @return список клиентов
     */
    @Override
    public List<AllClientDTO> getClients() {
        return repository.findAll().stream().map(mapper::clientToAllClientDTO).collect(Collectors.toList());
    }

    /**
     * Метод для получения клиента по идентификатору
     *
     * @param id идентификатор клиента
     * @return данные клента
     */
    @Override
    public ClientDTO getClient(Long id) {
        Client client = checkClientExistsOrThrowException(id);
        return mapper.clientToClientDTO(client);
    }

    /**
     * Метод для добавления клиента в БД
     *
     * @param clientDTO данные клиента
     */
    @Override
    public void addClient(AddClientDTO clientDTO) {
        Client client = mapper.AddClientDTOToClient(clientDTO);
        repository.save(client);
    }

    /**
     * Метод для обновления данных о клиенте
     * @param clientDTO данные клиента
     */
    @Override
    public void updateClient(ClientDTO clientDTO) {
        Client client = mapper.clientDTOToClient(clientDTO);
        Client persist = checkClientExistsOrThrowException(client.getId());
        mapper.updateClient(client, persist);
        repository.save(persist);
    }
}
