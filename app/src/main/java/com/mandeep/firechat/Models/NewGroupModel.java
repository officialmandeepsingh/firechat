package com.mandeep.firechat.Models;

public class NewGroupModel {
    String groupname,createdby,createdon,groupicon,groupid;
    GroupParticipant groupParticipant;

    public NewGroupModel() {}

    public NewGroupModel(String groupname, String createdby, String createdon, GroupParticipant groupParticipant,String groupicon,String groupid) {
        this.groupname = groupname;
        this.createdby = createdby;
        this.createdon = createdon;
        this.groupParticipant = groupParticipant;
        this.groupicon = groupicon;
        this.groupid = groupid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public GroupParticipant getGroupParticipant() {
        return groupParticipant;
    }

    public void setGroupParticipant(GroupParticipant groupParticipant) {
        this.groupParticipant = groupParticipant;
    }

    public String getGroupicon() {
        return groupicon;
    }

    public void setGroupicon(String groupicon) {
        this.groupicon = groupicon;
    }
}
