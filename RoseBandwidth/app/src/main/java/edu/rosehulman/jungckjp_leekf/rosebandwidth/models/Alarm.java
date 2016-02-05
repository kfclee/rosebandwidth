package edu.rosehulman.jungckjp_leekf.rosebandwidth.models;

/**
 * Created by jonathan on 1/16/16.
 */
public class Alarm {
    private float amount;
    private boolean enabled;
    private int type;
    private String name;

    public Alarm(String name, float amount, boolean enabled, int type) {
        this.name = name;

        this.amount = amount;
        this.enabled = enabled;
        this.type = type;
    }

    public String getName() {
        return name;
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
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeString(){
        if(type == 0){
            return "%";
        } else if(type == 1){
            return "MB";
        } else if(type == 2){
            return "GB";
        }
        return "";
    }
}
