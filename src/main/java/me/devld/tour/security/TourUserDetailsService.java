package me.devld.tour.security;

import me.devld.tour.entity.TourUser;
import me.devld.tour.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TourUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public TourUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TourUser user = userService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username '" + username + "' not found");
        }
        return new TourUserDetails(user);
    }
}
