package me.devld.tour.dto.auth;

public class RegisterCheckIn {

    private String username;

    private String email;

    public RegisterCheckIn() {
    }

    public RegisterCheckIn(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
