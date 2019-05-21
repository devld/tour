package me.devld.tour.service.impl;

import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.dto.user.UserProfileIn;
import me.devld.tour.entity.TourUser;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.TourUserRepository;
import me.devld.tour.service.DistrictService;
import me.devld.tour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final String CACHE_USER = "tour_user";

    private final TourUserRepository tourUserRepository;

    private final DistrictService districtService;

    private UserServiceImpl self;

    private final CacheManager cacheManager;

    public UserServiceImpl(TourUserRepository tourUserRepository, DistrictService districtService, CacheManager cacheManager) {
        this.tourUserRepository = tourUserRepository;
        this.districtService = districtService;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = CACHE_USER, key = "'$' + #p0")
    @Override
    public TourUser findUserById(long id) {
        return tourUserRepository.findById(id).orElse(null);
    }

    @Cacheable(CACHE_USER)
    @Override
    public TourUser findUserByUsername(String username) {
        Optional<TourUser> user = tourUserRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public TourUser findUserByEmail(String email) {
        Optional<TourUser> user = tourUserRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public UserProfile getUserInfo(String username) {
        return fillUserInfo(self.findUserByUsername(username));
    }

    @Override
    public UserProfile getUserInfo(long userId) {
        return self.fillUserInfo(self.findUserById(userId));
    }

    @Override
    public UserProfile fillUserInfo(TourUser user) {
        if (user == null) {
            return null;
        }
        UserProfile info = new UserProfile(user);
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

        if (StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar("/static/image/avatar.jpg");
        }

        user.setGender(TourUser.Gender.NONE);
        tourUserRepository.save(user);
        return user;
    }


    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_USER, key = "#p0"),
                    @CacheEvict(value = CACHE_USER, key = "'$' + #result.id")
            }
    )
    @Override
    public TourUser updateProfile(String username, UserProfileIn newUser) {
        Optional<TourUser> userOptional = tourUserRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new NotFoundException();
        }
        TourUser user = userOptional.get();
        if (!StringUtils.isEmpty(newUser.getUsername())) {
            // 修改用户名
            if (userExists(newUser.getUsername())) {
                throw new ForbiddenException("msg.username_exists", user.getUsername());
            }
            user.setUsername(newUser.getUsername());
        }
        if (newUser.getNickname() != null) {
            user.setNickname(newUser.getNickname());
        }
        if (newUser.getAvatar() != null) {
            user.setAvatar(newUser.getAvatar());
        }
        if (newUser.getSelfIntro() != null) {
            user.setSelfIntro(newUser.getSelfIntro());
        }
        if (newUser.getGender() != null) {
            user.setGender(newUser.getGender());
        }
        if (newUser.getBirth() != null) {
            user.setBirth(newUser.getBirth());
        }
        if (newUser.getRegionId() != null) {
            user.setRegionId(newUser.getRegionId());
        }
        user.setUpdatedAt(System.currentTimeMillis());
        return tourUserRepository.save(user);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_USER, key = "#p0"),
                    @CacheEvict(value = CACHE_USER, key = "'$' + #result.id")
            }
    )
    @Override
    public TourUser updateUserPassword(String username, String newPassword) {
        Optional<TourUser> userOptional = tourUserRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new NotFoundException();
        }
        TourUser user = userOptional.get();
        user.setPassword(newPassword);
        return tourUserRepository.save(user);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_USER, key = "'$' + #p0"),
                    @CacheEvict(value = CACHE_USER, key = "#result.username")
            }
    )
    @Override
    public TourUser toggleUserEnabled(long userId, boolean enabled) {
        TourUser user = self.findUserById(userId);
        if (user == null) {
            throw new NotFoundException();
        }
        if (user.getState() == TourUser.STATE_DELETED) {
            throw new ForbiddenException();
        }
        user.setState(enabled ? TourUser.STATE_NORMAL : TourUser.STATE_DISABLED);
        return tourUserRepository.save(user);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_USER, key = "'$' + #p0")
            }
    )
    @Override
    public void deleteUser(long userId) {
        TourUser user = self.findUserById(userId);
        if (user == null) {
            throw new NotFoundException();
        }

        Objects.requireNonNull(cacheManager.getCache(CACHE_USER)).evict(user.getUsername());

        user.setUsername(UUID.randomUUID().toString().replace("-", ""));
        user.setNickname("");
        user.setAvatar(null);
        user.setSelfIntro(null);
        user.setGender(null);
        user.setBirth(null);
        user.setRegionId(null);
        user.setEmail(null);
        user.setUserType(null);
        user.setPassword(null);
        user.setState(TourUser.STATE_DELETED);

        tourUserRepository.save(user);
    }

    @Autowired
    public void setSelf(UserServiceImpl self) {
        this.self = self;
    }

}
