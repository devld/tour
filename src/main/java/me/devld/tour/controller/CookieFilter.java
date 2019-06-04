package me.devld.tour.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CookieFilter extends OncePerRequestFilter {

    private static final String COOKIE_KEY = "_tour_path";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_KEY)) {
                    if (cookie.getValue().equals(contextPath)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
            }
        }
        // 添加 contextPath 给前端 tour.js 使用
        Cookie cookie = new Cookie(COOKIE_KEY, contextPath);
        cookie.setPath("/");
        response.addCookie(cookie);
        filterChain.doFilter(request, response);
    }
}
