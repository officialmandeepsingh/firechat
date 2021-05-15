package com.mandeep.firechat.Models;

import androidx.annotation.NonNull;

public class MainChatViewModel {
    String pic_url,name,accstatus,message,time,uid;

    public MainChatViewModel(String pic_url, String name, String accstatus,String uid) {
        this.pic_url = pic_url;
        this.name = name;
        this.accstatus = accstatus;
        this.uid = uid;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccstatus() {
        return accstatus;
    }

    public void setAccstatus(String accstatus) {
        this.accstatus = accstatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name : "+name+" message : "+message;
    }
}
