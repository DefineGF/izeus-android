package com.cjm.izeus.bean;

public class Member {
    private int memberLabel;
    private String memberName;
    private String memberAccount;
    private String memberId;
    private String memberContact;

    public Member(){}
    public Member(int memberLabel){
        this.memberLabel = memberLabel;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberContact() {
        return memberContact;
    }

    public int getMemberLabel() {
        return memberLabel;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setMemberContact(String memberContact) {
        this.memberContact = memberContact;
    }
}
