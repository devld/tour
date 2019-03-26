package me.devld.tour.dto.auth;

public class RegisterCheckOut {

    private boolean username;
    private boolean email;

    public RegisterCheckOut(boolean username, boolean email) {
        this.username = username;
        this.email = email;
    }

    public boolean isUsername() {
        return username;
    }

    public void setUsername(boolean username) {
        this.username = username;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }
}
