package me.devld.tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseException {
    public NotFoundException(int code, String message, Object... data) {
        super(code, message, data);
    }

    public NotFoundException(String message, Object... data) {
        super(404, message, data);
    }

    public NotFoundException() {
        super(404, "ex.not_found");
    }

}
