package com.mandeep.firechat.Models;

public class NewUser {
    String name,email,contact,pic_url,active,lastseen,status,isverified,accstatus,userid;

    public NewUser() {

    }

    public NewUser(String name, String email, String contact, String pic_url, String active, String lastseen, String status, String isverified, String accstatus, String userid) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.pic_url = pic_url;
        this.active = active;
        this.lastseen = lastseen;
        this.status = status;
        this.isverified = isverified;
        this.accstatus = accstatus;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccstatus() {
        return accstatus;
    }

    public void setAccstatus(String accstatus) {
        this.accstatus = accstatus;
    }

    public String getName() {
        return name;
    }

    public String getIsverified() {
        return isverified;
    }

    public void setIsverified(String isverified) {
        this.isverified = isverified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
