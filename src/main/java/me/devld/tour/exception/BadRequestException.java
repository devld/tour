package me.devld.tour.exception;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        this("ex.bad_request");
    }

    public BadRequestException(String message, Object... data) {
        super(400, message, data);
    }
}
