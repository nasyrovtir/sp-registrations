package ru.nasyrov.spr.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderDTO {

    @NotNull
    private Long clientId;

    @NotNull
    private String orderId;
}
