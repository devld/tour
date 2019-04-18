package me.devld.tour.security;

import me.devld.tour.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;

public class ApiTokenAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final TokenService tokenService;

    public ApiTokenAuthenticationProvider(UserService userService, TokenService tokenService) {
        this.userService = userService;
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
        return createAuthentication(tokenAuthentication, username);
    }

    private Authentication createAuthentication(ApiTokenAuthentication apiTokenAuthentication, String username) {
        TourUserDetails principal = new TourUserDetails(userService.findUserByUsername(username));
        ApiTokenAuthentication authentication = new ApiTokenAuthentication((String) apiTokenAuthentication.getCredentials(), principal, principal.getAuthorities());
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
