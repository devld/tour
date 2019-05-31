package me.devld.tour.repository.rel;

import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LikeCollectRelRepository extends CrudRepository<LikeCollectRel, Long> {

    LikeCollectRel findFirstByUserIdAndObjIdAndObjTypeAndRelType(long userId, long objId, RelObjectType objType, RelType relType);

    List<LikeCollectRel> findAllByUserIdAndObjIdInAndObjTypeAndRelTypeIn(long userId, List<Long> objIds, RelObjectType objType, List<RelType> relType);

}
