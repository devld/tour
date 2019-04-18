package me.devld.tour.service.impl;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotCommentIn;
import me.devld.tour.dto.spot.SpotCommentOut;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.*;
import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.SpotCommentRepository;
import me.devld.tour.repository.SpotRepository;
import me.devld.tour.repository.SpotTicketRepository;
import me.devld.tour.service.DistrictService;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.service.SpotService;
import me.devld.tour.service.UserService;
import me.devld.tour.util.BeanUtil;
import me.devld.tour.util.HtmlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpotServiceImpl implements SpotService {

    private final SpotRepository spotRepository;
    private final SpotTicketRepository spotTicketRepository;
    private final SpotCommentRepository spotCommentRepository;

    private final UserService userService;
    private final DistrictService districtService;
    private final LikeCollectService likeCollectService;

    public SpotServiceImpl(SpotRepository spotRepository,
                           SpotTicketRepository spotTicketRepository,
                           SpotCommentRepository spotCommentRepository,
                           UserService userService,
                           DistrictService districtService,
                           LikeCollectService likeCollectService) {
        this.spotRepository = spotRepository;
        this.spotTicketRepository = spotTicketRepository;
        this.spotCommentRepository = spotCommentRepository;
        this.userService = userService;
        this.districtService = districtService;
        this.likeCollectService = likeCollectService;
    }

    @Override
    public Spot createSpot(SpotIn spotIn) {
        Spot spot = new Spot();
        BeanUtils.copyProperties(spotIn, spot);
        spot.setIntro(HtmlUtils.sanitizer(spot.getIntro()));
        spot.setCreatedAt(System.currentTimeMillis());
        spot.setUpdatedAt(spot.getCreatedAt());
        return spotRepository.save(spot);
    }

    @Override
    public Spot updateSpot(long spotId, SpotIn spotIn) {
        Optional<Spot> spot = spotRepository.findById(spotId);
        if (!spot.isPresent()) {
            throw new NotFoundException();
        }
        Location newLocation = spotIn.getLocation();
        if (newLocation != null) {
            BeanUtil.copyPropertiesButNull(newLocation, spot.get().getLocation());
        }
        spotIn.setLocation(null);
        BeanUtil.copyPropertiesButNull(spotIn, spot.get());
        spot.get().setUpdatedAt(System.currentTimeMillis());
        return spotRepository.save(spot.get());
    }

    @Override
    public SpotDetailsOut getSpotDetails(long id) {
        Optional<Spot> spot = spotRepository.findById(id);
        if (!spot.isPresent()) {
            throw new NotFoundException();
        }
        List<SpotTicket> tickets = spotTicketRepository.findAllBySpotId(id);
        return new SpotDetailsOut(spot.get(), tickets);
    }

    @Override
    public Page<SpotCommentOut> getSpotComments(long spotId, PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updated_at", "like_count");
        if ("like_count".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "like_count", "updated_at");
        }
        return spotCommentRepository.findAllBySpotId(spotId, pageParam.toPageable(sort)).map(e -> new SpotCommentOut(e, userService.getUserInfo(e.getAuthorId())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpotComment commentSpot(SpotCommentIn comment, long spotId, long authorId) {
        if (spotRepository.incrementCountById(spotId, 0, 0, 1) != 1) {
            throw new NotFoundException();
        }
        SpotComment spotComment = new SpotComment();
        spotComment.setContent(comment.getContent());
        spotComment.setAuthorId(authorId);
        spotComment.setSpotId(spotId);
        spotComment.setCreatedAt(System.currentTimeMillis());
        spotComment.setUpdatedAt(spotComment.getCreatedAt());
        return spotCommentRepository.save(spotComment);
    }

    @Override
    public Page<Spot> getSpotsByLocationId(int id, PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "collect_count".equals(pageParam.getSort()) ? "collect_count" : "went_count");
        return spotRepository.findAllByLocationLocationIdIn(
                districtService.getFlattingChildren(id).stream().map(District::getId).collect(Collectors.toList()),
                pageParam.toPageable(sort)
        );
    }

    @Override
    public Page<Spot> getSpotList(PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "went_count");
        if ("collect_count".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "collect_count");
        }
        return spotRepository.findAll(pageParam.toPageable(sort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectSpot(long id, long userId, boolean state) {
        if (spotRepository.incrementCountById(id, 0, state ? 1 : -1, 0) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, id, RelObjectType.SPOT, RelType.COLLECT))) {
            throw new ForbiddenException(state ? "msg.collected" : "msg.not_collected");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wentSpot(long id, long userId, boolean state) {
        if (spotRepository.incrementCountById(id, state ? 1 : -1, 0, 0) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, id, RelObjectType.SPOT, RelType.LIKE))) {
            throw new ForbiddenException(state ? "msg.already_went" : "msg.not_went");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeSpotComment(long commentId, long userId, boolean state) {
        if (spotCommentRepository.incrementCountById(commentId, state ? 1 : -1) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, commentId, RelObjectType.SPOT_COMMENT, RelType.LIKE))) {
            throw new ForbiddenException(state ? "msg.already_liked" : "msg.not_liked");
        }
    }
}
