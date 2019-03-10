package me.devld.tour.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class ApiTokenAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    public ApiTokenAuthenticationProvider(UserDetailsService userDetailsService, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiTokenAuthentication tokenAuthentication = (ApiTokenAuthentication) authentication;
        String tokenKey = (String) tokenAuthentication.getCredentials();
        Token token = tokenService.verifyToken(tokenKey);
        if (token == null) {
            throw new BadCredentialsException("bad api token");
        }
        String username = token.getExtendedInformation();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        tokenAuthentication.setDetails(userDetails);
        tokenAuthentication.setAuthenticated(true);
        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
