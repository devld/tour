package me.devld.tour.config;

import me.devld.tour.dto.ApiResult;
import me.devld.tour.security.ApiTokenAuthenticationFilter;
import me.devld.tour.security.ApiTokenAuthenticationProvider;
import me.devld.tour.security.ApiTokenService;
import me.devld.tour.security.CookieApiTokenAuthenticationFilter;
import me.devld.tour.service.UserService;
import me.devld.tour.util.I18nUtil;
import me.devld.tour.util.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.security.SecureRandom;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    private final AppConfig.ApiTokenConfig apiTokenConfig;

    public SecurityConfig(UserService userService, AppConfig appConfig) {
        this.userService = userService;
        this.apiTokenConfig = appConfig.getApiToken();
    }

    @Bean
    public TokenService tokenService() {
        return new ApiTokenService(
                apiTokenConfig.getSecret(),
                apiTokenConfig.getServerInteger(),
                new SecureRandom(),
                apiTokenConfig.getValidity());
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(
                Collections.singletonList(
                        new ApiTokenAuthenticationProvider(userService, tokenService())
                )
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Order(50)
    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true)
    public class SecurityApiConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**");

            http
                    .rememberMe().disable()
                    .csrf().disable()
                    .formLogin().disable()
                    .logout().disable()
                    .requestCache().disable()
                    .httpBasic().disable();

            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

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
                    .antMatchers(HttpMethod.POST, "/api/auth/check_register").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                    .anyRequest().authenticated();

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

    @Order(100)
    @Configuration
    public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 公开页面全部为后端渲染
            // 需要登录访问的，全部为 REST API 调用

            http
                    .formLogin().disable()
                    .logout().disable()
                    .csrf().disable()
                    .httpBasic().disable();

            http.addFilterBefore(
                    new CookieApiTokenAuthenticationFilter(authenticationManager(), "hello"),
                    BasicAuthenticationFilter.class
            );

            http
                    .authorizeRequests().anyRequest().permitAll();
        }

    }

}
