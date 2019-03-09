package me.devld.tour.exception;

public class BaseException extends RuntimeException {

    private final int code;
    private final Object[] data;

    public BaseException(int code, String message, Object[] data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public BaseException(int code, String message, Throwable t) {
        super(message, t);
        this.code = code;
        this.data = null;
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.data = null;
    }

    public BaseException(int code, String message, Object[] data, Throwable t) {
        super(message, t);
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object[] getData() {
        return data;
    }
}
