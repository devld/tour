package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.ApiResult;
import me.devld.tour.dto.auth.LoginIn;
import me.devld.tour.dto.auth.RegisterCheckIn;
import me.devld.tour.dto.auth.RegisterIn;
import me.devld.tour.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@ApiController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ApiResult token(@RequestBody @Valid LoginIn apiLogin) {
        return ApiResult.ok(authService.login(apiLogin));
    }

    @PostMapping("/check_register")
    public ApiResult checkUsername(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email) {
        return ApiResult.ok(authService.checkRegister(new RegisterCheckIn(username, email)));
    }

    @PostMapping("/register")
    public ApiResult register(@RequestBody @Valid RegisterIn register) {
        authService.register(register);
        return ApiResult.ok();
    }

}
