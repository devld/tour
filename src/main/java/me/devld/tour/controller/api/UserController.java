package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
import me.devld.tour.controller.Request;
import me.devld.tour.dto.user.UserProfile;
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

    @Request("获取用户资料")
    @GetMapping("/{username}")
    public UserProfile getUserProfile(@PathVariable("username") String username) {
        return userService.getUserInfo(username);
    }

    @Request("获取个人资料")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public UserProfile getMineProfile() {
        return userService.fillUserInfo(SecurityUtil.user());
    }

    @Request("更新个人资料")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public UserProfile updateProfile(@RequestBody UserProfileIn userProfile) {
        userService.updateProfile(SecurityUtil.username(), userProfile);
        return userService.getUserInfo(SecurityUtil.username());
    }

    @Request("设置用户状态(ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public void toggleUserState(@PathVariable("id") long userId, @RequestParam("enabled") boolean enabled) {
        userService.toggleUserEnabled(userId, enabled);
    }

    @Request("删除用户")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long userId) {
        userService.deleteUser(userId);
    }

}
