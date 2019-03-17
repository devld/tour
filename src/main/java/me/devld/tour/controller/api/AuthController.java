package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.ApiResult;
import me.devld.tour.dto.auth.ApiLoginInput;
import me.devld.tour.util.I18nUtil;
import me.devld.tour.util.MapUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Value("${app.api-token.validity}")
    private long tokenValidity;

    public AuthController(TokenService tokenService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/token")
    public ApiResult token(@RequestBody ApiLoginInput apiLogin) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(apiLogin.getUsername());
        } catch (UsernameNotFoundException e) {
            return ApiResult.fail(401, I18nUtil.getMessage("msg.auth.invalid_login"));
        }
        if (!passwordEncoder.matches(apiLogin.getPassword(), userDetails.getPassword())) {
            return ApiResult.fail(401, I18nUtil.getMessage("msg.auth.invalid_login"));
        }
        return ApiResult.ok(MapUtil.map(
                "token", tokenService.allocateToken(userDetails.getUsername()),
                "expire_in", tokenValidity,
                "user", userDetails
        ));
    }

}
