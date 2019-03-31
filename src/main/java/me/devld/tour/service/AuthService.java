package me.devld.tour.service;

import me.devld.tour.dto.auth.*;

public interface AuthService {

    LoginOut login(LoginIn loginIn);

    void logout(String token);

    void register(RegisterIn register);

    RegisterCheckOut checkRegister(RegisterCheckIn registerCheck);

    void changePassword(String username, String oldPassword, String newPassword);

}
