package me.devld.tour.controller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Request {

    /**
     * 接口名称
     *
     * @return name
     */
    String value();

}
