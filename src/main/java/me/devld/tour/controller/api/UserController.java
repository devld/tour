package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.dto.ApiResult;
import me.devld.tour.dto.user.UserProfileIn;
import me.devld.tour.service.UserService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@ApiController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ApiResult getUserProfile(@PathVariable("username") String username) {
        return ApiResult.ok(userService.getUserInfo(username));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ApiResult getMineProfile() {
        return ApiResult.ok(userService.fillUserInfo(SecurityUtil.user()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public ApiResult updateProfile(@RequestBody UserProfileIn userProfile) {
        userService.updateProfile(SecurityUtil.username(), userProfile);
        return ApiResult.ok(userService.getUserInfo(SecurityUtil.username()));
    }

}
