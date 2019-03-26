package me.devld.tour.exception;

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
