package me.devld.tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends BaseException {
    public ForbiddenException(int code, String message, Object... data) {
        super(code, message, data);
    }

    public ForbiddenException(String message, Object... data) {
        super(403, message, data);
    }

    public ForbiddenException() {
        super(403, "ex.permission_denied");
    }

}
