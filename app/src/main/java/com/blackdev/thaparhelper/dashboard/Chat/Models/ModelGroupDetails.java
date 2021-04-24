package com.blackdev.thaparhelper.dashboard.Chat.Models;

import java.util.List;

public class ModelGroupDetails {

    String groupId;
    String groupName;
    String groupCourseName;

    String groupIconLink;
    String groupNotificationKey;
    List<String> participants;

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCourseName() {
        return groupCourseName;
    }

    public void setGroupCourseName(String groupCourseName) {
        this.groupCourseName = groupCourseName;
    }


    public String getGroupIconLink() {
        return groupIconLink;
    }

    public void setGroupIconLink(String groupIconLink) {
        this.groupIconLink = groupIconLink;
    }

    public String getGroupNotificationKey() {
        return groupNotificationKey;
    }

    public void setGroupNotificationKey(String groupNotificationKey) {
        this.groupNotificationKey = groupNotificationKey;
    }

    public ModelGroupDetails(String groupId, String groupName, String groupCourseName, String groupIconLink, String groupNotificationKey, List<String> participants) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCourseName = groupCourseName;
        this.groupIconLink = groupIconLink;
        this.groupNotificationKey = groupNotificationKey;
        this.participants = participants;
    }
}
