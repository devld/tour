package me.devld.tour.service;

import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;

import java.util.List;

public interface LikeCollectService {

    /**
     * 标记状态
     *
     * @param state 标记/取消状态
     * @param rel   关系
     * @return 是否成功
     */
    boolean markRelation(boolean state, LikeCollectRel rel);

    /**
     * 获取用户点赞收藏状态
     *
     * @param userId  用户 id
     * @param objType 对象类型
     * @param relType 关系类型
     * @param objIds  对象 ids
     * @return 点赞关系
     */
    List<LikeCollectRel> getRelBy(long userId, RelObjectType objType, RelType relType, List<Long> objIds);

}
