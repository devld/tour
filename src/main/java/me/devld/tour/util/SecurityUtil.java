package me.devld.tour.util;

import me.devld.tour.exception.UnauthorizedException;
import me.devld.tour.security.TourUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Authentication autnentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static TourUserDetails user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnauthorizedException();
        }
        return (TourUserDetails) authentication.getPrincipal();
    }

    public static boolean authenticated() {
        return autnentication().isAuthenticated();
    }

    public static String username() {
        return user().getUsername();
    }

    public static TourUserDetails userOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof TourUserDetails) {
                return (TourUserDetails) principal;
            }
        }
        return null;
    }

}
