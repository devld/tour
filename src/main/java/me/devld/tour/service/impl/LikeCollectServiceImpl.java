package me.devld.tour.service.impl;

import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.repository.rel.LikeCollectRelRepository;
import me.devld.tour.service.LikeCollectService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LikeCollectServiceImpl implements LikeCollectService {

    private final LikeCollectRelRepository likeCollectRelRepository;

    public LikeCollectServiceImpl(LikeCollectRelRepository likeCollectRelRepository) {
        this.likeCollectRelRepository = likeCollectRelRepository;
    }

    @Override
    public boolean markRelation(boolean state, LikeCollectRel rel) {
        if (rel.getUserId() == null || rel.getObjId() == null || rel.getObjType() == null || rel.getRelType() == null) {
            throw new IllegalArgumentException();
        }
        LikeCollectRel collected = likeCollectRelRepository.findFirstByUserIdAndObjIdAndObjTypeAndRelType(rel.getUserId(), rel.getObjId(), rel.getObjType(), rel.getRelType());
        if ((collected != null) == state) {
            return false;
        }
        if (state) {
            rel.setId(null);
            likeCollectRelRepository.save(rel);
        } else {
            likeCollectRelRepository.deleteById(collected.getId());
        }
        return true;
    }

    @Override
    public List<LikeCollectRel> getRelBy(long userId, RelObjectType objType, RelType relType, List<Long> objIds) {
        if (objIds.isEmpty()) {
            return Collections.emptyList();
        }
        return likeCollectRelRepository.findAllByUserIdAndObjIdInAndObjTypeAndRelType(userId, objIds, objType, relType);
    }
}
