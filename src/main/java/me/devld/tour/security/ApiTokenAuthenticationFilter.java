package me.devld.tour.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTokenAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;

    public ApiTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication result = null;

        try {
            result = attemptAuthentication(request, response);
        } catch (AuthenticationException e) {
            LOGGER.debug("api token auth failed", e);
        }

        if (result != null) {
            SecurityContextHolder.getContext().setAuthentication(result);
        }

        filterChain.doFilter(request, response);

    }

    protected Authentication attemptAuthentication(HttpServletRequest request,
                                                   HttpServletResponse response) throws AuthenticationException, IOException,
            ServletException {
        String token = obtainTokenFromRequest(request);
        if (token == null) {
            return null;
        }
        return authenticationManager.authenticate(new ApiTokenAuthentication(token));
    }

    protected String obtainTokenFromRequest(HttpServletRequest request) throws IOException {
        String authentication = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authentication)) {
            return null;
        }
        String[] temp = authentication.split("\\s+");
        if (temp.length != 2) {
            return null;
        }
        String type = temp[0];
        if (!"token".equals(type)) {
            return null;
        }
        return temp[1];
    }

}
