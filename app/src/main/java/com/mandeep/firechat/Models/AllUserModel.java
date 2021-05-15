package com.mandeep.firechat.Models;

import androidx.annotation.NonNull;

public class AllUserModel {
    String userid,name,pic_url;

    public AllUserModel(String userid, String name, String pic_url) {
        this.userid = userid;
        this.name = name;
        this.pic_url = pic_url;
    }



    public AllUserModel() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    @NonNull
    @Override
    public String toString() {
        String str=" Name : "+name+" User Id : "+userid;
        return str;
    }
}
