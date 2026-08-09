package org.example;

public class DataModel {
    private String domain;
    private String ip;
    public DataModel(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return domain + "-" + ip;
    }
}