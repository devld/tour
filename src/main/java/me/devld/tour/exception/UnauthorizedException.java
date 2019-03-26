package me.devld.tour.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(int code, String message, Object... data) {
        super(code, message, data);
    }

    public UnauthorizedException(String message, Object... data) {
        super(401, message, data);
    }

    public UnauthorizedException() {
        super(401, "ex.not_login");
    }
}
