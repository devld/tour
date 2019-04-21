package me.devld.tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseException {
    public BadRequestException() {
        this("ex.bad_request");
    }

    public BadRequestException(String message, Object... data) {
        super(400, message, data);
    }
}
