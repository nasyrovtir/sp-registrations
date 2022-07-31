package ru.nasyrov.spr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.nasyrov.spr.dto.AddClientDTO;
import ru.nasyrov.spr.dto.AllClientDTO;
import ru.nasyrov.spr.dto.ClientDTO;
import ru.nasyrov.spr.service.ClientService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "client", description = "Контроллер для обработки клиентов")
@RequestMapping("/api/v0/pool/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    @Operation(summary = "Получение списка клиентов бассейна")
    public List<AllClientDTO> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/get")
    @Operation(summary = "Получение данных о клиенте")
    public ClientDTO getClient(@RequestParam("id") Long id) {
        return clientService.getClient(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление нового клиента")
    public void addClient(@Valid @RequestBody AddClientDTO clientDTO) {
        clientService.addClient(clientDTO);
    }

    @PostMapping("/update")
    @Operation(summary = "Обновление данных о клиенте")
    public void updateClient(@Valid @RequestBody ClientDTO clientDTO) {
        clientService.updateClient(clientDTO);
    }
}
