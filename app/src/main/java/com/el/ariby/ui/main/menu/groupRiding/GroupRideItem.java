package com.el.ariby.ui.main.menu.groupRiding;

public class GroupRideItem {

    String groupName;
    String start;
    String end;
    String location;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public GroupRideItem(String groupName, String start, String end, String location) {
        this.groupName = groupName;
        this.start = start;
        this.end = end;
        this.location = location;
    }


}
