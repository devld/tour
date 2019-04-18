package me.devld.tour.dto.user;

import me.devld.tour.entity.TourUser;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;


public class UserProfileIn {

    @Pattern(regexp = "[A-z0-9_\\-]{5,32}")
    private String username;

    @Size(min = 1, max = 32)
    private String nickname;

    @Size(max = 128)
    private String avatar;

    @Size(max = 128)
    private String selfIntro;

    private TourUser.Gender gender;

    private Date birth;

    private Integer regionId;

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

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }
}
