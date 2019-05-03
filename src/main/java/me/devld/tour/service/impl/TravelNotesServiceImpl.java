package me.devld.tour.service.impl;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.travel.TravelNotesDetailsOut;
import me.devld.tour.dto.travel.TravelNotesIn;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotPhoto;
import me.devld.tour.entity.TravelNotes;
import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.SpotPhotoRepository;
import me.devld.tour.repository.SpotRepository;
import me.devld.tour.repository.TravelNotesRepository;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.service.TravelNotesService;
import me.devld.tour.service.UserService;
import me.devld.tour.util.HtmlUtils;
import me.devld.tour.util.ImageProp;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import java.util.*;
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
        List<Long> spotIds = travelNotesIn.getSpotIds();
        if (spotIds != null && !spotIds.isEmpty()) {
            if (spotRepository.countByIdIn(spotIds) != spotIds.size()) {
                throw new NotFoundException("msg.spot_not_exists");
            }
        }
        travelNotesIn.setContent(travelNotesIn.getContent());
        TravelNotes travelNotes = new TravelNotes();
        BeanUtils.copyProperties(travelNotesIn, travelNotes);
        travelNotes.setAuthorId(authorId);
        if (spotIds != null) {
            travelNotes.setSpots(spotIds.stream().map(Spot::new).collect(Collectors.toList()));
        }
        travelNotesRepository.save(travelNotes);
        List<TravelNotesIn.TravelNotesPhoto> photos = travelNotesIn.getPhotos();
        if (!ListUtils.isEmpty(photos)) {
            spotPhotoRepository.saveAll(
                    photos.stream()
                            .filter(e -> !StringUtils.isEmpty(e.getSrc()))
                            .map(e -> new SpotPhoto(e.getSrc(), e.getAlt(), e.getSpotId(),
                                    travelNotes.getId(), SpotPhoto.PhotoFrom.TRAVEL_NOTES, authorId))
                            .collect(Collectors.toList())
            );
            Map<Long, List<TravelNotesIn.TravelNotesPhoto>> photoList = photos.stream().filter(e -> e.getSpotId() != null).collect(Collectors.groupingBy(TravelNotesIn.TravelNotesPhoto::getSpotId));
            for (Long spotId : photoList.keySet()) {
                int c = photoList.get(spotId).size();
                if (c > 0) {
                    spotRepository.incrementPhotoCount(spotId, c);
                }
            }
        }
        return travelNotes;
    }

    @Override
    public TravelNotesDetailsOut getTravelNotesDetails(long id, Long userId) {
        Optional<TravelNotes> travelNotes = travelNotesRepository.findById(id);
        if (!travelNotes.isPresent()) {
            throw new NotFoundException();
        }

        TravelNotesDetailsOut out = new TravelNotesDetailsOut(travelNotes.get(), userService.getUserInfo(travelNotes.get().getAuthorId()), travelNotes.get().getSpots());
        if (userId != null) {
            Map<RelType, LikeCollectRel> rel = likeCollectService.getRelBy(userId, RelObjectType.TRAVEL_NOTES, Arrays.asList(RelType.LIKE, RelType.COLLECT), Collections.singletonList(userId))
                    .stream().collect(Collectors.toMap(LikeCollectRel::getRelType, e -> e));
            out.setLiked(rel.containsKey(RelType.LIKE));
            out.setCollected(rel.containsKey(RelType.COLLECT));
        }
        return out;
    }

    @Override
    public Page<TravelNotesDetailsOut> getTravelNotesBySpot(long spotId, PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt", "likeCount");
        if ("like".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "likeCount", "updatedAt");
        }
        return processTravelNotes(travelNotesRepository.findAllBySpots(new Spot(spotId), pageParam.toPageable(sort)));
    }

    private Page<TravelNotesDetailsOut> processTravelNotes(Page<TravelNotes> travelNotes) {
        return travelNotes.map(e -> {
            TravelNotesDetailsOut out = new TravelNotesDetailsOut(e, userService.getUserInfo(e.getAuthorId()), null);
            String content = e.getContent();
            List<ImageProp> images = HtmlUtils.extractImagesFromHtml(content);
            if (!images.isEmpty()) {
                out.setCoverUrl(images.get(0).getSrc());
            }
            String shortContent = HtmlUtils.htmlToText(content);
            if (shortContent.length() > 100) {
                shortContent = shortContent.substring(0, 100) + "...";
            }
            out.setShortContent(shortContent);
            return out;
        });
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
}
