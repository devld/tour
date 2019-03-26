package me.devld.tour.dto.auth;

import me.devld.tour.dto.user.UserInfo;

public class LoginOut {

    private String token;

    private long expireIn;

    private UserInfo user;

    public LoginOut(String token, long expireIn, UserInfo user) {
        this.token = token;
        this.expireIn = expireIn;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
