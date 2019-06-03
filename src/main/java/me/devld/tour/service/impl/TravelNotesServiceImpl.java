package me.devld.tour.service.impl;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.travel.TravelNotesDetailsOut;
import me.devld.tour.dto.travel.TravelNotesIn;
import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotPhoto;
import me.devld.tour.entity.TourUser;
import me.devld.tour.entity.TravelNotes;
import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.exception.BadRequestException;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.SpotPhotoRepository;
import me.devld.tour.repository.SpotRepository;
import me.devld.tour.repository.TravelNotesRepository;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.service.TravelNotesService;
import me.devld.tour.service.UserService;
import me.devld.tour.util.HtmlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TravelNotesServiceImpl implements TravelNotesService {

    private final TravelNotesRepository travelNotesRepository;
    private final SpotRepository spotRepository;
    private final SpotPhotoRepository spotPhotoRepository;

    private final UserService userService;
    private final LikeCollectService likeCollectService;

    public TravelNotesServiceImpl(TravelNotesRepository travelNotesRepository,
                                  SpotRepository spotRepository,
                                  SpotPhotoRepository spotPhotoRepository,
                                  UserService userService,
                                  LikeCollectService likeCollectService) {
        this.travelNotesRepository = travelNotesRepository;
        this.spotRepository = spotRepository;
        this.spotPhotoRepository = spotPhotoRepository;
        this.userService = userService;
        this.likeCollectService = likeCollectService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TravelNotes createTravelNotes(TravelNotesIn travelNotesIn, long authorId) {
        travelNotesIn.setContent(HtmlUtils.sanitizer(travelNotesIn.getContent()));
        List<Long> spotIds = travelNotesIn.getSpotIds();
        checkSpots(spotIds);
        TravelNotes travelNotes = new TravelNotes();
        BeanUtils.copyProperties(travelNotesIn, travelNotes);
        travelNotes.setAuthorId(authorId);
        travelNotes.setSpots(spotIds.stream().map(Spot::new).collect(Collectors.toList()));
        travelNotesRepository.save(travelNotes);
        List<TravelNotesIn.TravelNotesPhoto> photos = travelNotesIn.getPhotos();
        if (!ListUtils.isEmpty(photos)) {
            processNotesPhotos(authorId, travelNotes, photos, 0);
        }
        return travelNotes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TravelNotes editTravelNotes(long id, TravelNotesIn travelNotesIn, long userId) {
        travelNotesIn.setContent(HtmlUtils.sanitizer(travelNotesIn.getContent()));
        List<Long> spotIds = travelNotesIn.getSpotIds();
        checkSpots(spotIds);
        TravelNotes travelNotes = travelNotesRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!travelNotes.getAuthorId().equals(userId)) {
            throw new ForbiddenException();
        }
        BeanUtils.copyProperties(travelNotesIn, travelNotes);
        travelNotes.setSpots(spotIds.stream().map(Spot::new).collect(Collectors.toList()));
        travelNotesRepository.save(travelNotes);
        List<TravelNotesIn.TravelNotesPhoto> photos = travelNotesIn.getPhotos();
        int deletedCount = spotPhotoRepository.deleteAllByFromIdAndPhotoFrom(travelNotes.getId(), SpotPhoto.PhotoFrom.TRAVEL_NOTES);
        if (!ListUtils.isEmpty(photos)) {
            processNotesPhotos(travelNotes.getAuthorId(), travelNotes, photos, deletedCount);
        }
        return travelNotes;
    }

    private void processNotesPhotos(long authorId, TravelNotes travelNotes, List<TravelNotesIn.TravelNotesPhoto> photos, int deletedCount) {
        List<SpotPhoto> spotPhotos = photos.stream()
                .filter(e -> !StringUtils.isEmpty(e.getSrc()))
                .map(e -> new SpotPhoto(e.getSrc(), e.getAlt(), e.getSpotId(),
                        travelNotes.getId(), SpotPhoto.PhotoFrom.TRAVEL_NOTES, authorId))
                .collect(Collectors.toList());
        spotPhotoRepository.saveAll(spotPhotos);
        Map<Long, List<TravelNotesIn.TravelNotesPhoto>> photoList = photos.stream().filter(e -> e.getSpotId() != null)
                .collect(Collectors.groupingBy(TravelNotesIn.TravelNotesPhoto::getSpotId));
        for (Long spotId : photoList.keySet()) {
            int c = photoList.get(spotId).size();
            if (c > 0) {
                spotRepository.incrementPhotoCount(spotId, c - deletedCount);
            }
        }
        Map<String, SpotPhoto> photoMap = spotPhotos.stream().collect(Collectors.toMap(e -> e.getImgUrl() + e.getDescription(), e -> e));
        travelNotes.setContent(
                HtmlUtils.buildSanitizer(((tagName, modifier) -> {
                    if ("img".equals(tagName)) {
                        SpotPhoto photo = photoMap.get(modifier.attr("src") + modifier.attr("alt"));
                        if (photo != null) {
                            modifier.attr("data-id", String.valueOf(photo.getId()));
                        }
                    }
                })).get().sanitizer(travelNotes.getContent())
        );
        travelNotesRepository.save(travelNotes);
    }

    private void checkSpots(List<Long> spotIds) {
        if (spotIds == null || spotIds.isEmpty()) {
            throw new BadRequestException();
        }
        if (spotRepository.countByIdIn(spotIds) != spotIds.size()) {
            throw new NotFoundException("msg.spot_not_exists");
        }
    }

    @Override
    public TravelNotesDetailsOut getTravelNotesDetails(long id, Long userId) {
        TravelNotes travelNotes = travelNotesRepository.findById(id).orElseThrow(NotFoundException::new);
        if (travelNotes.getState() == TravelNotes.STATE_DELETED && userId != null && !userId.equals(travelNotes.getAuthorId())) {
            throw new NotFoundException();
        }

        TravelNotesDetailsOut out = new TravelNotesDetailsOut(travelNotes, userService.getUserInfo(travelNotes.getAuthorId()), travelNotes.getSpots());
        if (userId != null) {
            Map<RelType, LikeCollectRel> rel = likeCollectService.getRelBy(userId, RelObjectType.TRAVEL_NOTES, Arrays.asList(RelType.LIKE, RelType.COLLECT), Collections.singletonList(id))
                    .stream().collect(Collectors.toMap(LikeCollectRel::getRelType, e -> e));
            out.setLiked(rel.containsKey(RelType.LIKE));
            out.setCollected(rel.containsKey(RelType.COLLECT));
        }
        return out;
    }

    @Override
    public Page<TravelNotesDetailsOut> getTravelNotesBySpot(long spotId, PageParam pageParam) {
        return processTravelNotes(travelNotesRepository.findAllBySpots(spotId, parsePageParam(pageParam)));
    }

    private Pageable parsePageParam(PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt", "likeCount");
        if ("like".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "likeCount", "updatedAt");
        }
        if ("collect".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "collectCount", "likeCount", "updatedAt");
        }
        if ("share".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "shareCount", "collectCount", "likeCount", "updatedAt");
        }
        return pageParam.toPageable(sort);
    }

    private Page<TravelNotesDetailsOut> processTravelNotes(Page<TravelNotes> travelNotes) {
        Map<Long, UserProfile> userProfiles = userService.getUserInfos(travelNotes.stream().map(TravelNotes::getAuthorId).collect(Collectors.toList()));
        return travelNotes.map(e -> new TravelNotesDetailsOut(e, userProfiles.get(e.getAuthorId()), null));
    }

    @Override
    public Page<TravelNotesDetailsOut> getHotTravelNotes(PageParam pageParam) {
        pageParam.setSort("share");
        return processTravelNotes(travelNotesRepository.findAll(parsePageParam(pageParam)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shareTravelNotes(long travelNotesId, long userId) {
        if (travelNotesRepository.incrementCountById(travelNotesId, 0, 0, 1) != 1) {
            throw new NotFoundException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeTravelNotes(long travelNotesId, long userId, boolean state) {
        if (travelNotesRepository.incrementCountById(travelNotesId, state ? 1 : -1, 0, 0) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, travelNotesId, RelObjectType.TRAVEL_NOTES, RelType.LIKE))) {
            throw new ForbiddenException(state ? "msg.already_liked" : "msg.not_liked");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectTravelNotes(long travelNotesId, long userId, boolean state) {
        if (travelNotesRepository.incrementCountById(travelNotesId, 0, state ? 1 : -1, 0) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, travelNotesId, RelObjectType.TRAVEL_NOTES, RelType.COLLECT))) {
            throw new ForbiddenException(state ? "msg.collected" : "msg.not_collected");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTravelNotes(long travelNotesId, long userId) {
        TravelNotes notes = travelNotesRepository.findById(travelNotesId).orElseThrow(NotFoundException::new);
        if (notes.getAuthorId() != userId) {
            if (userService.findUserById(userId).getUserType() != TourUser.UserType.ADMIN) {
                throw new ForbiddenException();
            }
        }
        travelNotesRepository.deleteById(travelNotesId);
    }
}
