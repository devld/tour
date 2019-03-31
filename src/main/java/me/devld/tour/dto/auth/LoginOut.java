package me.devld.tour.dto.auth;

import me.devld.tour.dto.user.UserProfile;

public class LoginOut {

    private String token;

    private long expireIn;

    private UserProfile user;

    public LoginOut(String token, long expireIn, UserProfile user) {
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

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
