package me.devld.tour.dto;

import me.devld.tour.util.I18nUtil;
import org.springframework.data.domain.Page;

public class ApiResult {

    private final int code;
    private final String message;

    private final Object data;

    public ApiResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResult ok(Page<?> pageData) {
        return ok(pageData.getContent());
    }

    public static ApiResult ok(Object data) {
        return new ApiResult(0, I18nUtil.getMessage("msg.success"), data);
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
