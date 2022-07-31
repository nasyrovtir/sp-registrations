package ru.nasyrov.spr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;

    private Integer count;
}
