package edu.rosehulman.jungckjp_leekf.rosebandwidth.models;

/**
 * Created by leekf on 1/29/2016.
 */
public class Usage {
    private String status;
    private float upload;
    private float download;

    public Usage(String status, float upload, float download) {
        this.status = status;
        this.upload = upload;
        this.download = download;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getDownload() {
        return download;
    }

    public void setDownload(float download) {
        this.download = download;
    }

    public float getUpload() {
        return upload;
    }

    public void setUpload(float upload) {
        this.upload = upload;
    }
}
