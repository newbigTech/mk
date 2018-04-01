package com.hand.hmall.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * author: zhangzilong
 * name: ProfileValue.java
 * discription:
 * date: 2017/11/17
 * version: 0.1
 */
@Entity
@Table(name = "SYS_PROFILE_VALUE")
public class ProfileValue {

    private String profileValue;

    public String getProfileValue() {
        return profileValue;
    }

    public void setProfileValue(String profileValue) {
        this.profileValue = profileValue;
    }
}
