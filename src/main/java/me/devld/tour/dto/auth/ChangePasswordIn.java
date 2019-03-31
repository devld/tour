package me.devld.tour.dto.auth;

public class ChangePasswordIn {

    private String old;
    private String password;

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
