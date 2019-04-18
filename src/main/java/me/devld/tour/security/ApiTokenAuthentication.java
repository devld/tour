package me.devld.tour.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiTokenAuthentication extends AbstractAuthenticationToken {

    private final String token;

    private Object principal;

    public ApiTokenAuthentication(String token) {
        super(null);
        this.token = token;
    }

    public ApiTokenAuthentication(String token, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
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
