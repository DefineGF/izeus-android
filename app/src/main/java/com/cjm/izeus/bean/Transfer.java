package com.cjm.izeus.bean;

public class Transfer {
    public String id;
    public String from_account;
    public String to_account;
    public String from_name;
    public String amount;
    public String tn_time;
    public String verified;
    public String unverified;
    public String state;
    public String note;
    public Transfer(){}

    public Transfer(String id, String from_account, String to_account, String from_name, String amount, String tn_time,
                    String verified, String unverified, String state, String note) {
        super();
        this.id = id;
        this.from_account = from_account;
        this.to_account = to_account;
        this.from_name = from_name;
        this.amount = amount;
        this.tn_time = tn_time;
        this.verified = verified;
        this.unverified = unverified;
        this.state = state;
        this.note = note;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFrom_account() {
        return from_account;
    }
    public void setFrom_account(String from_account) {
        this.from_account = from_account;
    }
    public String getTo_account() {
        return to_account;
    }
    public void setTo_account(String to_account) {
        this.to_account = to_account;
    }
    public String getFrom_name() {
        return from_name;
    }
    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getTn_time() {
        return tn_time;
    }
    public void setTn_time(String tn_time) {
        this.tn_time = tn_time;
    }
    public String getVerified() {
        return verified;
    }
    public void setVerified(String verified) {
        this.verified = verified;
    }
    public String getUnverified() {
        return unverified;
    }
    public void setUnverified(String unverified) {
        this.unverified = unverified;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }


}
