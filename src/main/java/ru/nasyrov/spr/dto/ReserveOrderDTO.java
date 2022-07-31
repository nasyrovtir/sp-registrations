package ru.nasyrov.spr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReserveOrderDTO {

    @NotNull
    private Long clientId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTimeEnd;
}
