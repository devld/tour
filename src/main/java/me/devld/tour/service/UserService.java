package me.devld.tour.service;

import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.dto.user.UserProfileIn;
import me.devld.tour.entity.TourUser;

import java.util.List;
import java.util.Map;

public interface UserService {

    TourUser findUserById(long id);

    TourUser findUserByUsername(String username);

    TourUser findUserByEmail(String email);

    UserProfile fillUserInfo(TourUser user);

    UserProfile getUserInfo(String username);

    UserProfile getUserInfo(long userId);

    boolean userExists(String username);

    boolean emailExists(String email);

    TourUser createUser(TourUser user);

    TourUser updateProfile(String username, UserProfileIn user);

    TourUser updateUserPassword(String username, String newPassword);

    TourUser toggleUserEnabled(long userId, boolean enabled);

    void deleteUser(long userId);

    Map<Long, UserProfile> getUserInfos(List<Long> userIds);

}
