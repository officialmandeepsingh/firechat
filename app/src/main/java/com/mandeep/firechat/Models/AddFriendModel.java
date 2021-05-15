package com.mandeep.firechat.Models;

public class AddFriendModel {
    String friend_userid,date;

    public AddFriendModel(String friend_userid, String date) {
        this.friend_userid = friend_userid;
        this.date = date;
    }

    public String getFriend_userid() {
        return friend_userid;
    }

    public void setFriend_userid(String friend_userid) {
        this.friend_userid = friend_userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
