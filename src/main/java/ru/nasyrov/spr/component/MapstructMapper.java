package ru.nasyrov.spr.component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.nasyrov.spr.dto.AllClientDTO;
import ru.nasyrov.spr.dto.ClientDTO;
import ru.nasyrov.spr.dto.AddClientDTO;
import ru.nasyrov.spr.dto.OrderDTO;
import ru.nasyrov.spr.entity.Client;
import ru.nasyrov.spr.entity.Order;

@Mapper(componentModel = "spring")
public interface MapstructMapper {

    AllClientDTO clientToAllClientDTO(Client client);

    ClientDTO clientToClientDTO(Client client);

    Client AddClientDTOToClient(AddClientDTO clientDTO);

    @Mapping(target = "id", ignore = true)
    Client clientDTOToClient(ClientDTO clientDTO);

    @Mapping(target = "id", ignore = true)
    void updateClient(Client client, @MappingTarget Client persist);

    @Mapping(target = "orderId", source = "order.id")
    OrderDTO orderToOrderDTO(Order order);
}
