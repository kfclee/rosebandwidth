package edu.rosehulman.jungckjp_leekf.rosebandwidth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by jonathan on 1/16/16.
 */
public class Alarm implements Comparable<Alarm> {
    private float amount;
    private boolean enabled;
    private int type;
    private String name;
    private String user;

    @JsonIgnore
    private String key;

    public Alarm() {
        // Firebase empty constructor
    }

    public Alarm(String name, float amount, boolean enabled, int type, String user) {
        this.name = name;

        this.amount = amount;
        this.enabled = enabled;
        this.type = type;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public int compareTo(Alarm another) {
        return this.getName().compareTo(another.getName());
    }
}
