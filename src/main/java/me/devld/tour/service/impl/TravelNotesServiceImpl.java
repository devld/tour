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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;
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
        }
        return travelNotes;
    }

    @Override
    public TravelNotesDetailsOut getTravelNotesDetails(long id) {
        Optional<TravelNotes> travelNotes = travelNotesRepository.findById(id);
        if (!travelNotes.isPresent()) {
            throw new NotFoundException();
        }
        return new TravelNotesDetailsOut(travelNotes.get(), userService.getUserInfo(travelNotes.get().getAuthorId()), travelNotes.get().getSpots());
    }

    @Override
    public Page<TravelNotes> getTravelNotesBySpot(long spotId, PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt", "likeCount");
        if ("like".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "likeCount", "updatedAt");
        }
        return travelNotesRepository.findAllBySpots(new Spot(spotId), pageParam.toPageable(sort));
    }

    @Override
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
