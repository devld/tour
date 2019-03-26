package me.devld.tour.security;

import me.devld.tour.entity.TourUser;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TourUserDetails extends TourUser implements UserDetails {

    private static final Map<UserType, Collection<? extends GrantedAuthority>> USER_TYPE_COLLECTION_MAP = new HashMap<>();

    static {
        USER_TYPE_COLLECTION_MAP.put(UserType.USER, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        USER_TYPE_COLLECTION_MAP.put(UserType.ADMIN, Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    public TourUserDetails(TourUser user) {
        BeanUtils.copyProperties(user, this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return USER_TYPE_COLLECTION_MAP.get(this.getUserType());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}