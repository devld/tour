package me.devld.tour.config;

import me.devld.tour.dto.ApiResult;
import me.devld.tour.security.ApiTokenAuthenticationFilter;
import me.devld.tour.security.ApiTokenAuthenticationProvider;
import me.devld.tour.security.ApiTokenService;
import me.devld.tour.util.I18nUtil;
import me.devld.tour.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.security.SecureRandom;

@Order(50)
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityApiConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Value("${app.api-token.secret}")
    private String serverSecret;
    @Value("${app.api-token.server-integer}")
    private int serverInteger;
    @Value("${app.api-token.validity}")
    private long tokenValidity;

    public SecurityApiConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**");

        http.rememberMe().disable();
        http.csrf().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.sessionManagement().disable();
        http.requestCache().disable();
        http.httpBasic().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());

        // verify token
        http.addFilterBefore(
                new ApiTokenAuthenticationFilter(authenticationManager()),
                BasicAuthenticationFilter.class
        );

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/token").permitAll()
                .anyRequest().authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new ApiTokenAuthenticationProvider(userDetailsService, tokenService()));
    }

    @Bean
    public TokenService tokenService() {
        return new ApiTokenService(serverSecret, serverInteger, new SecureRandom(), tokenValidity);
    }

    @Bean
    protected AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            JsonUtil.writeObject(ApiResult.fail(401, I18nUtil.getMessage("ex.not_login")), response.getWriter());
        };
    }

    @Bean
    protected AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            JsonUtil.writeObject(ApiResult.fail(403, I18nUtil.getMessage("ex.permission_denied")), response.getWriter());
        };
    }

}
