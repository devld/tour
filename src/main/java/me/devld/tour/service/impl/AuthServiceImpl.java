package me.devld.tour.service.impl;

import me.devld.tour.config.AppConfig;
import me.devld.tour.dto.auth.*;
import me.devld.tour.entity.TourUser;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.exception.UnauthorizedException;
import me.devld.tour.service.AuthService;
import me.devld.tour.service.UserService;
import me.devld.tour.util.TextUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Primary
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private final AppConfig.ApiTokenConfig apiTokenConfig;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, TokenService tokenService, AppConfig appConfig) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.apiTokenConfig = appConfig.getApiToken();
    }

    @Override
    public LoginOut login(LoginIn loginIn) {
        TourUser user;
        if (TextUtils.isEmail(loginIn.getUsernameOrEmail())) {
            user = userService.findUserByEmail(loginIn.getUsernameOrEmail());
        } else {
            user = userService.findUserByUsername(loginIn.getUsernameOrEmail());
        }
        if (user == null) {
            throw new UnauthorizedException("msg.auth.invalid_login");
        }
        if (!passwordEncoder.matches(loginIn.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("msg.auth.invalid_login");
        }
        if (user.getState() == TourUser.STATE_DISABLED) {
            throw new ForbiddenException("msg.account_disabled");
        }
        if (user.getState() == TourUser.STATE_DELETED) {
            throw new ForbiddenException("msg.auth.invalid_login");
        }
        Token token = tokenService.allocateToken(user.getUsername());
        return new LoginOut(token.getKey(), apiTokenConfig.getValidity(), userService.fillUserInfo(user));
    }

    @Override
    public void logout(String token) {
        // nothing
    }

    @Override
    public void register(RegisterIn register) {
        TourUser user = new TourUser();
        user.setUsername(register.getUsername());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setEmail(register.getEmail());
        user.setNickname(register.getNickname());
        user.setUserType(TourUser.UserType.USER);

        userService.createUser(user);
    }

    @Override
    public RegisterCheckOut checkRegister(RegisterCheckIn registerCheck) {
        boolean username = false;
        boolean email = false;
        if (!StringUtils.isEmpty(registerCheck.getUsername())) {
            username = userService.userExists(registerCheck.getUsername());
        }
        if (!StringUtils.isEmpty(registerCheck.getEmail())) {
            email = userService.emailExists(registerCheck.getEmail());
        }
        return new RegisterCheckOut(username, email);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        TourUser user = userService.findUserByUsername(username);
        if (user == null) {
            throw new NotFoundException();
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ForbiddenException("msg.auth.invalid_password");
        }
        userService.updateUserPassword(username, passwordEncoder.encode(newPassword));
    }
}
