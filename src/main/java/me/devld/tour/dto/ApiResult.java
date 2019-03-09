package me.devld.tour.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import me.devld.tour.util.I18nUtil;

public class ApiResult {

    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object data;

    public ApiResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResult ok(Object data) {
        return new ApiResult(0, I18nUtil.getMessage("msg.success"), data);
    }

    public static ApiResult ok() {
        return ok(null);
    }

    public static ApiResult fail(int code, String message, Object data) {
        return new ApiResult(code, message, data);
    }

    public static ApiResult fail(int code, String message) {
        return fail(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}