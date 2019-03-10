package me.devld.tour.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class ApiTokenAuthentication extends AbstractAuthenticationToken {

    private final String token;

    public ApiTokenAuthentication(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }
}
