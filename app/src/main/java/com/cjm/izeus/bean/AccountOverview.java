package com.cjm.izeus.bean;

public class AccountOverview {
    private String uid;
    private String creditcard;
    private int iscurrent;
    private String type;
    private String byname;
    private String currency;
    private String rate;

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getCreditcard() {
        return creditcard;
    }
    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }
    public int getIscurrent() {
        return iscurrent;
    }
    public void setIscurrent(int iscurrent) {
        this.iscurrent = iscurrent;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getByname() {
        return byname;
    }
    public void setByname(String byname) {
        this.byname = byname;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getRate() {
        return rate;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }

}
