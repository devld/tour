package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.ApiResult;
import me.devld.tour.dto.auth.ChangePasswordIn;
import me.devld.tour.dto.auth.LoginIn;
import me.devld.tour.dto.auth.RegisterCheckIn;
import me.devld.tour.dto.auth.RegisterIn;
import me.devld.tour.service.AuthService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ApiResult logout() {
        authService.logout(SecurityUtil.token());
        return ApiResult.ok();
    }

    @PostMapping("/check_register")
    public ApiResult checkUsername(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email) {
        return ApiResult.ok(authService.checkRegister(new RegisterCheckIn(username, email)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public ApiResult changePassword(@RequestBody ChangePasswordIn changePassword) {
        authService.changePassword(SecurityUtil.username(), changePassword.getOld(), changePassword.getPassword());
        return ApiResult.ok();
    }

    @PostMapping("/register")
    public ApiResult register(@RequestBody @Valid RegisterIn register) {
        authService.register(register);
        return ApiResult.ok();
    }

}
