package me.devld.tour.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseStateEntity extends BaseEntity {

    public static final byte STATE_NORMAL = 0;
    public static final byte STATE_DELETED = 1;

    private Byte state = STATE_NORMAL;

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}
