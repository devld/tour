package me.devld.tour.service;

import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.dto.user.UserProfileIn;
import me.devld.tour.entity.TourUser;

public interface UserService {

    TourUser findUserByUsername(String username);

    TourUser findUserByEmail(String email);

    UserProfile fillUserInfo(TourUser user);

    UserProfile getUserInfo(String username);

    boolean userExists(String username);

    boolean emailExists(String email);

    TourUser createUser(TourUser user);

    TourUser updateProfile(String username, UserProfileIn user);

    TourUser updateUserPassword(String username, String newPassword);

}
