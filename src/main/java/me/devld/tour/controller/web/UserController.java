package me.devld.tour.controller.web;

import me.devld.tour.controller.Request;
import me.devld.tour.service.UserService;
import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("webUserController")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Request("个人资料页面")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String myProfile(Model model) {
        model.addAttribute("mine", true);
        model.addAttribute("user_info", userService.fillUserInfo(SecurityUtil.user()));
        return "user/user-info";
    }

    @Request("用户资料页面")
    @GetMapping("/{username}")
    public String userProfile(@PathVariable("username") String username, Model model) {
        model.addAttribute("mine", false);
        model.addAttribute("user_info", userService.getUserInfo(username));
        return "user/user-info";
    }

    @Request("修改密码页面")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/change-password")
    public String changePassword() {
        return "user/changePassword";
    }

}
