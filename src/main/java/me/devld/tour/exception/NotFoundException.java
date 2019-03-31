package me.devld.tour.exception;

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
