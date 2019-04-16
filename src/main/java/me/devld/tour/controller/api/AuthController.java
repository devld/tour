package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.auth.*;
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
    public LoginOut token(@RequestBody @Valid LoginIn apiLogin) {
        return authService.login(apiLogin);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public void logout() {
        authService.logout(SecurityUtil.token());
    }

    @PostMapping("/check_register")
    public RegisterCheckOut checkUsername(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email) {
        return authService.checkRegister(new RegisterCheckIn(username, email));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public void changePassword(@RequestBody ChangePasswordIn changePassword) {
        authService.changePassword(SecurityUtil.username(), changePassword.getOld(), changePassword.getPassword());
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterIn register) {
        authService.register(register);
    }

}
