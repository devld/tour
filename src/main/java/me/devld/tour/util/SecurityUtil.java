package me.devld.tour.util;

import me.devld.tour.exception.UnauthorizedException;
import me.devld.tour.security.TourUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static TourUserDetails user() {
        Authentication authentication = authentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException();
        }
        return (TourUserDetails) authentication.getPrincipal();
    }

    public static String token() {
        return (String) authentication().getCredentials();
    }

    public static boolean authenticated() {
        return authentication().isAuthenticated();
    }

    public static String username() {
        return user().getUsername();
    }

    public static long userId() {
        return user().getId();
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
