package me.devld.tour.controller;

import me.devld.tour.dto.ApiResult;
import me.devld.tour.exception.BaseException;
import me.devld.tour.util.I18nUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class, basePackages = "me.devld.tour.controller.api")
public class ApiExceptionHandling {

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ApiResult handle(BaseException e) {
        return ApiResult.fail(e.getCode(), I18nUtil.getMessage(e.getMessage(), e.getData()));
    }

}
