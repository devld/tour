package me.devld.tour.controller.api;

import me.devld.tour.controller.ApiController;
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

    @GetMapping("/{username}")
    public UserProfile getUserProfile(@PathVariable("username") String username) {
        return userService.getUserInfo(username);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public UserProfile getMineProfile() {
        return userService.fillUserInfo(SecurityUtil.user());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public UserProfile updateProfile(@RequestBody UserProfileIn userProfile) {
        userService.updateProfile(SecurityUtil.username(), userProfile);
        return userService.getUserInfo(SecurityUtil.username());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public void toggleUserState(@PathVariable("id") long userId, @RequestParam("enabled") boolean enabled) {
        userService.toggleUserEnabled(userId, enabled);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long userId) {
        userService.deleteUser(userId);
    }

}
