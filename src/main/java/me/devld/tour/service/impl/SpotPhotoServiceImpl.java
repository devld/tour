package me.devld.tour.service.impl;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotPhotoDetailsOut;
import me.devld.tour.dto.spot.SpotPhotoOut;
import me.devld.tour.entity.BaseEntity;
import me.devld.tour.entity.Spot;
import me.devld.tour.entity.SpotPhoto;
import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.SpotPhotoRepository;
import me.devld.tour.repository.SpotRepository;
import me.devld.tour.repository.TravelNotesRepository;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.service.SpotPhotoService;
import me.devld.tour.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpotPhotoServiceImpl implements SpotPhotoService {

    private final SpotPhotoRepository spotPhotoRepository;
    private final SpotRepository spotRepository;
    private final TravelNotesRepository travelNotesRepository;

    private final UserService userService;
    private final LikeCollectService likeCollectService;

    public SpotPhotoServiceImpl(SpotPhotoRepository spotPhotoRepository,
                                SpotRepository spotRepository,
                                TravelNotesRepository travelNotesRepository,
                                UserService userService,
                                LikeCollectService likeCollectService) {
        this.spotPhotoRepository = spotPhotoRepository;
        this.spotRepository = spotRepository;
        this.travelNotesRepository = travelNotesRepository;
        this.userService = userService;
        this.likeCollectService = likeCollectService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likePhoto(long photoId, long userId, boolean state) {
        if (spotPhotoRepository.incrementCountById(photoId, state ? 1 : -1) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, photoId, RelObjectType.SPOT_PHOTO, RelType.LIKE))) {
            throw new ForbiddenException(state ? "msg.already_liked" : "msg.not_liked");
        }
    }

    @Override
    public Page<SpotPhotoOut> getSpotPhotosBySpot(long spotId, Long userId, PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "likeCount");
        if ("latest".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        }
        Page<SpotPhoto> photos = spotPhotoRepository.findAllBySpotId(spotId, pageParam.toPageable(sort));

        Map<Long, LikeCollectRel> likeCollectRelMap = userId == null ? null :
                likeCollectService.getRelBy(userId, RelObjectType.SPOT_PHOTO,
                        Collections.singletonList(RelType.LIKE),
                        photos.stream().map(BaseEntity::getId).collect(Collectors.toList())
                ).stream().collect(Collectors.toMap(LikeCollectRel::getObjId, e -> e));

        return photos.map(e -> new SpotPhotoOut(e, likeCollectRelMap == null ? null : likeCollectRelMap.containsKey(e.getId())));

    }

    @Override
    public SpotPhotoDetailsOut getPhotoDetails(long photoId, Long userId) {
        SpotPhoto photo = spotPhotoRepository.findById(photoId).orElseThrow(NotFoundException::new);
        // in case
        Spot spot = photo.getSpotId() == null ? null :
                spotRepository.findEntityById(photo.getSpotId()).orElse(null);

        Object from = null;
        if (photo.getPhotoFrom() == SpotPhoto.PhotoFrom.TRAVEL_NOTES) {
            from = travelNotesRepository.findEntityById(photo.getFromId()).orElse(null);
        }
        SpotPhotoDetailsOut out = new SpotPhotoDetailsOut(photo, userService.getUserInfo(photo.getAuthorId()), spot, from);
        if (userId != null) {
            out.setLiked(
                    !likeCollectService.getRelBy(userId, RelObjectType.SPOT_PHOTO,
                            Collections.singletonList(RelType.LIKE),
                            Collections.singletonList(photoId)
                    ).isEmpty()
            );

        }
        if (spot != null) {
            out.setPrev(spotPhotoRepository.findPreId(photoId, spot.getId()));
            out.setNext(spotPhotoRepository.findNextId(photoId, spot.getId()));
        }
        return out;
    }
}
