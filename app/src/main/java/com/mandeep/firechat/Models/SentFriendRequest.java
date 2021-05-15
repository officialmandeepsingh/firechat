package com.mandeep.firechat.Models;

public class SentFriendRequest {
    String to,from,date;

    public SentFriendRequest(String to, String from, String date) {
        this.to = to;
        this.from = from;
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
