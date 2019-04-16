package me.devld.tour.service.impl;

import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.SpotPhotoRepository;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.service.SpotPhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpotPhotoServiceImpl implements SpotPhotoService {

    private final SpotPhotoRepository spotPhotoRepository;

    private final LikeCollectService likeCollectService;

    public SpotPhotoServiceImpl(SpotPhotoRepository spotPhotoRepository, LikeCollectService likeCollectService) {
        this.spotPhotoRepository = spotPhotoRepository;
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
}
