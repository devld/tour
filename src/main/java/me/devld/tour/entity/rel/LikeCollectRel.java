package me.devld.tour.entity.rel;

import me.devld.tour.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tour_like_collect_rel")
public class LikeCollectRel extends BaseEntity {

    private Long userId;

    private Long objId;

    @Column(columnDefinition = "tinyint")
    private RelObjectType objType;

    @Column(columnDefinition = "tinyint")
    private RelType relType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public RelObjectType getObjType() {
        return objType;
    }

    public void setObjType(RelObjectType objType) {
        this.objType = objType;
    }

    public RelType getRelType() {
        return relType;
    }

    public void setRelType(RelType relType) {
        this.relType = relType;
    }
}
