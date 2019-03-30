package me.devld.tour.security;

import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CookieApiTokenAuthenticationFilter extends ApiTokenAuthenticationFilter {

    private final String cookieName;

    public CookieApiTokenAuthenticationFilter(AuthenticationManager authenticationManager, String cookieName) {
        super(authenticationManager);
        this.cookieName = cookieName;
    }

    @Override
    protected String obtainTokenFromRequest(HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                }
            }
        }
        return null;
    }
}
