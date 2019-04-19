package me.devld.tour.controller;

import me.devld.tour.dto.ApiResult;
import me.devld.tour.exception.BaseException;
import me.devld.tour.util.I18nUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(annotations = ApiController.class, basePackages = "me.devld.tour.controller.api")
public class ApiControllerPostHandling implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ApiResult handle(BaseException e) {
        return ApiResult.fail(e.getCode(), I18nUtil.getMessage(e.getMessage(), e.getData()));
    }

    @ExceptionHandler({
            org.springframework.web.bind.MethodArgumentNotValidException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class,
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
            org.springframework.http.converter.HttpMessageNotReadableException.class
    })
    @ResponseBody
    public ApiResult handle(Exception e) {
        return ApiResult.fail(400, I18nUtil.getMessage("ex.bad_request"));
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return ApiResult.ok(body);
    }
}
