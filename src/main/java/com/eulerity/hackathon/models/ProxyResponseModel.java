package com.eulerity.hackathon.models;

public class ProxyResponseModel {
    private String anonymous;
    private int check_count;
    private int fail_count;
    private boolean https;
    private boolean last_status;
    private String last_time;
    private String proxy;
    private String region;
    private String source;

    public ProxyResponseModel(String anonymous, int check_count, int fail_count, boolean https, boolean last_status, String last_time, String proxy, String region, String source) {
        this.anonymous = anonymous;
        this.check_count = check_count;
        this.fail_count = fail_count;
        this.https = https;
        this.last_status = last_status;
        this.last_time = last_time;
        this.proxy = proxy;
        this.region = region;
        this.source = source;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public int getFail_count() {
        return fail_count;
    }

    public void setFail_count(int fail_count) {
        this.fail_count = fail_count;
    }

    public int getCheck_count() {
        return check_count;
    }

    public void setCheck_count(int check_count) {
        this.check_count = check_count;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public boolean isLast_status() {
        return last_status;
    }

    public void setLast_status(boolean last_status) {
        this.last_status = last_status;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ProxyResponseModel{" +
                "anonymous='" + anonymous + '\'' +
                ", check_count=" + check_count +
                ", fail_count=" + fail_count +
                ", https=" + https +
                ", last_status=" + last_status +
                ", last_time='" + last_time + '\'' +
                ", proxy='" + proxy + '\'' +
                ", region='" + region + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
