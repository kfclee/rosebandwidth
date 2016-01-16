package edu.rosehulman.jungckjp_leekf.rosebandwidth;

/**
 * Created by jonathan on 1/16/16.
 */
public class Alarm {
    private int amount;
    private boolean enabled;
    private String type;

    public Alarm(int amount, boolean enabled, String type) {
        this.amount = amount;
        this.enabled = enabled;
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
