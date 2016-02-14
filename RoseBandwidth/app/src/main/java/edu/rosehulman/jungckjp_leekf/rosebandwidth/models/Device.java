package edu.rosehulman.jungckjp_leekf.rosebandwidth.models;

/**
 * Created by leekf on 1/12/2016.
 */
public class Device {
    private String name;
    private String macAddress;
    private float usageAmount;
    private float uploadAmount;
    private String imageRes;

    public Device(String name, String macAddress, float usageAmount, float uploadAmount, String imageRes) {
        this.name = name;
        this.macAddress = macAddress;
        this.usageAmount = usageAmount;
        this.uploadAmount = uploadAmount;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public float getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(float usageAmount) {
        this.usageAmount = usageAmount;
    }

    public String getImageRes() {
        return imageRes;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }

    public float getUploadAmount() {
        return uploadAmount;
    }

    public void setUploadAmount(float uploadAmount) {
        this.uploadAmount = uploadAmount;
    }
}
