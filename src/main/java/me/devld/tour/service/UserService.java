package me.devld.tour.service;

import me.devld.tour.dto.user.UserInfo;
import me.devld.tour.entity.TourUser;

public interface UserService {

    TourUser findUserByUsername(String username);

    TourUser findUserByEmail(String email);

    UserInfo getUserInfo(TourUser user);

    boolean userExists(String username);

    boolean emailExists(String email);

    TourUser createUser(TourUser user);

}
