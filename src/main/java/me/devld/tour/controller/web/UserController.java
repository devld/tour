package me.devld.tour.controller.web;

import me.devld.tour.service.UserService;
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

    @GetMapping("")
    public String myProfile(Model model) {
        model.addAttribute("mine", true);
        return "user/user-info";
    }

    @GetMapping("/{username}")
    public String userProfile(@PathVariable("username") String username, Model model) {
        model.addAttribute("mine", false);
        model.addAttribute("user", userService.getUserInfo(username));
        return "user/user-info";
    }

}
