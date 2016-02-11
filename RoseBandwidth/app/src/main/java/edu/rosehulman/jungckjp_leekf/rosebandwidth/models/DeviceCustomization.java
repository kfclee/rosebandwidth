package edu.rosehulman.jungckjp_leekf.rosebandwidth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Comparator;

/**
 * Created by jonathan on 2/11/16.
 */
public class DeviceCustomization {
    private String uid;
    private String nickname;
    private int imageResId;
    private String user;

    @JsonIgnore
    private String key;

    public DeviceCustomization() {
        // Required Empty Constructor
    }

    public DeviceCustomization(String uid, String nickname, int imageResId, String user) {
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

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
