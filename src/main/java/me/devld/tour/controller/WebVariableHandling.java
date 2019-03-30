package me.devld.tour.controller;

import me.devld.tour.dto.user.UserInfo;
import me.devld.tour.security.TourUserDetails;
import me.devld.tour.service.UserService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class WebVariableHandling {

    private final UserService userService;

    public WebVariableHandling(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userDetail")
    public TourUserDetails currentUser() {
        return SecurityUtil.userOrNull();
    }

    @ModelAttribute("user")
    public UserInfo currentUserInfo() {
        return SecurityUtil.userOrNull() == null ? null : userService.getUserInfo(SecurityUtil.user());
    }

}