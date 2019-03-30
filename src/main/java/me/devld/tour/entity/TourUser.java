package me.devld.tour.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "tour_user")
public class TourUser extends BaseEntity {

    private String username;

    private String nickname;

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

    private boolean enabled;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public enum UserType {
        USER, ADMIN;

        @JsonValue
        public int value() {
            return ordinal();
        }
    }

    public enum Gender {
        NONE, MALE, FEMALE;

        @JsonValue
        public int value() {
            return ordinal();
        }
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
                ", enabled=" + enabled +
                '}';
    }
}
