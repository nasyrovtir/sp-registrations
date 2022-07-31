package ru.nasyrov.spr.exception;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorInfo {

    private String url;

    private String errorMessage;

    private Integer code;
}
