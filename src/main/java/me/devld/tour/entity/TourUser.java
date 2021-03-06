package me.devld.tour.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "tour_user")
public class TourUser extends BaseStateEntity {

    public static final byte STATE_DISABLED = 2;

    private String username;

    private String nickname;

    private String avatar;

    private String selfIntro;

    @Column(columnDefinition = "tinyint")
    private Gender gender;

    private Date birth;

    @Column(columnDefinition = "smallint(5)")
    private Integer regionId;

    private String email;

    @Column(columnDefinition = "tinyint")
    private UserType userType;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public enum UserType {
        USER, ADMIN
    }

    public enum Gender {
        NONE, MALE, FEMALE
    }

    @Override
    public String toString() {
        return "TourUser{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", selfIntro='" + selfIntro + '\'' +
                ", gender=" + gender +
                ", birth=" + birth +
                ", regionId=" + regionId +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                '}';
    }
}
