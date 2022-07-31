package ru.nasyrov.spr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;
import ru.nasyrov.spr.exception.ErrorInfo;
import ru.nasyrov.spr.exception.ProcessingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleNotFoundException(HttpServletRequest req, HttpServletResponse res, NotFoundException ex) {
        return getErrorInfo(req, res, ex, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProcessingException.class)
    public ErrorInfo handleProcessingException(HttpServletRequest req, HttpServletResponse res, ProcessingException ex) {
        return getErrorInfo(req, res, ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleException(HttpServletRequest req, HttpServletResponse res, Exception ex) {
        return getErrorInfo(req, res, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ErrorInfo getErrorInfo(HttpServletRequest req, HttpServletResponse res, Exception ex, HttpStatus httpStatus) {
        Throwable rootCause = getRootCause(ex);
        return new ErrorInfo(req.getRequestURI(), rootCause.getMessage(), httpStatus.value());
    }

    private static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }

        return result;
    }
}
