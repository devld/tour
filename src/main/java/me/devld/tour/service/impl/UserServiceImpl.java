package me.devld.tour.service.impl;

import me.devld.tour.dto.user.UserInfo;
import me.devld.tour.entity.TourUser;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.repository.TourUserRepository;
import me.devld.tour.service.DistrictService;
import me.devld.tour.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final TourUserRepository tourUserRepository;

    private final DistrictService districtService;

    public UserServiceImpl(TourUserRepository tourUserRepository, DistrictService districtService) {
        this.tourUserRepository = tourUserRepository;
        this.districtService = districtService;
    }

    @Cacheable("tour_user")
    @Override
    public TourUser findUserByUsername(String username) {
        Optional<TourUser> user = tourUserRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Cacheable("tour_user")
    @Override
    public TourUser findUserByEmail(String email) {
        Optional<TourUser> user = tourUserRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Cacheable(value = "tour_user_info", key = "#p0.id")
    @Override
    public UserInfo getUserInfo(TourUser user) {
        UserInfo info = new UserInfo(user);
        if (user.getRegionId() != null) {
            info.setRegion(districtService.getFullName(user.getRegionId()));
        }
        return info;
    }

    @Override
    public boolean userExists(String username) {
        return tourUserRepository.existsByUsername(username);
    }

    @Override
    public boolean emailExists(String email) {
        return tourUserRepository.existsByEmail(email);
    }

    @Override
    public TourUser createUser(TourUser user) {
        Assert.hasLength(user.getUsername(), "username must not be empty");
        Assert.hasLength(user.getPassword(), "password must not be empty");

        if (userExists(user.getUsername())) {
            throw new ForbiddenException("msg.username_exists", user.getUsername());
        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            if (emailExists(user.getEmail())) {
                throw new ForbiddenException("msg.email_exists", user.getEmail());
            }
        } else {
            user.setEmail("");
        }

        if (StringUtils.isEmpty(user.getNickname())) {
            user.setNickname(user.getUsername());
        }

        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdatedAt(System.currentTimeMillis());
        user.setEnabled(true);
        user.setGender(TourUser.Gender.NONE);
        tourUserRepository.save(user);
        return user;
    }
}
