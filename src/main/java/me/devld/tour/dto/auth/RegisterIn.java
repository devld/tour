package me.devld.tour.dto.auth;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterIn {

    @NotNull
    @Pattern(regexp = "[A-z0-9_\\-]{5,32}")
    private String username;

    @Size(min = 1, max = 32)
    private String nickname;

    @Size(max = 64)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 32)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
