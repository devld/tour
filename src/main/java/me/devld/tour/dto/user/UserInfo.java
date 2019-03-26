package me.devld.tour.dto.user;

import me.devld.tour.entity.TourUser;
import org.springframework.beans.BeanUtils;

import java.sql.Date;

public class UserInfo {

    private String username;

    private String nickname;

    private String selfIntro;

    private TourUser.Gender gender;

    private Date birth;

    private String region;

    private String email;

    private TourUser.UserType userType;

    public UserInfo(TourUser tourUser) {
        BeanUtils.copyProperties(tourUser, this);
    }

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

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public TourUser.Gender getGender() {
        return gender;
    }

    public void setGender(TourUser.Gender gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TourUser.UserType getUserType() {
        return userType;
    }

    public void setUserType(TourUser.UserType userType) {
        this.userType = userType;
    }
}
