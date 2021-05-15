package com.mandeep.firechat.Models;

import java.util.ArrayList;

public class GroupParticipant {
    ArrayList<String> adminlist;
    ArrayList<String> participantlist;

    public GroupParticipant() {
    }

    public GroupParticipant(ArrayList<String> adminlist, ArrayList<String> participantlist) {
        this.adminlist = adminlist;
        this.participantlist = participantlist;
    }

    public ArrayList<String> getAdminlist() {
        return adminlist;
    }

    public void setAdminlist(ArrayList<String> adminlist) {
        this.adminlist = adminlist;
    }

    public ArrayList<String> getParticipantlist() {
        return participantlist;
    }

    public void setParticipantlist(ArrayList<String> participantlist) {
        this.participantlist = participantlist;
    }
}
