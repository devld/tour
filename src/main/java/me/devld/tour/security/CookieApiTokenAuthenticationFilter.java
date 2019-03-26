package me.devld.tour.security;

import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieApiTokenAuthenticationFilter extends ApiTokenAuthenticationFilter {

    private final String cookieName;

    public CookieApiTokenAuthenticationFilter(AuthenticationManager authenticationManager, String cookieName) {
        super(authenticationManager);
        this.cookieName = cookieName;
    }

    @Override
    protected String obtainTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
