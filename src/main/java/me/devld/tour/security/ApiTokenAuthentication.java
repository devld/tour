package me.devld.tour.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class ApiTokenAuthentication extends AbstractAuthenticationToken {

    private final String token;

    private TourUserDetails principal;

    public ApiTokenAuthentication(String token) {
        super(null);
        this.token = token;
    }

    public void setUserDetails(TourUserDetails details) {
        this.principal = details;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
