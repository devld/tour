package me.devld.tour.exception;

public class ThirdPartException extends ServerErrorException {

    public ThirdPartException(int code, String message, Object... data) {
        super(code, message, data);
    }

    public ThirdPartException(String message, Object... data) {
        super(message, data);
    }

    public ThirdPartException(String message, Throwable e, Object... data) {
        super(message, e, data);
    }
}
