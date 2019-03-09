package me.devld.tour.util;

import me.devld.tour.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Object autnentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserDetails user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnauthorizedException();
        }
        return (UserDetails) authentication.getPrincipal();
    }

}
