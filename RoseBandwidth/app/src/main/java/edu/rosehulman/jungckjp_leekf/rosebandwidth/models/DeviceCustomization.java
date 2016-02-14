package edu.rosehulman.jungckjp_leekf.rosebandwidth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Comparator;

/**
 * Created by jonathan on 2/11/16.
 */
public class DeviceCustomization {
    private String uid;
    private String nickname;
    private String imageResId;
    private String user;

    @JsonIgnore
    private String key;

    public DeviceCustomization() {
        // Required Empty Constructor
    }

    public DeviceCustomization(String uid, String nickname, String imageResId, String user) {
        this.uid = uid;
        this.nickname = nickname;
        this.imageResId = imageResId;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
