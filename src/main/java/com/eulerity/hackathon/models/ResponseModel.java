package com.eulerity.hackathon.models;

import java.util.List;
import java.util.Objects;

public class ResponseModel {
    private String src;
    private String alt;
    private List<String> labels;

    public ResponseModel(String src, String alt, List<String> labels){
        this.src = src;
        this.alt = alt;
        this.labels = labels;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseModel that = (ResponseModel) o;
        return Objects.equals(src, that.src) && Objects.equals(alt, that.alt) && Objects.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, alt, labels);
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "src='" + src + '\'' +
                ", alt='" + alt + '\'' +
                ", labels=" + labels +
                '}';
    }
}
