package me.devld.tour.exception;

public class ServerErrorException extends BaseException {
    public ServerErrorException(int code, String message, Object... data) {
        super(code, message, data);
    }

    public ServerErrorException(String message, Object... data) {
        super(500, message, data);
    }

    public ServerErrorException(String message, Throwable e, Object... data) {
        super(500, message, e, data);
    }

    public ServerErrorException() {
        super(500, "ex.server_error");
    }

}
