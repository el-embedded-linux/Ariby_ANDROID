package com.el.ariby.ui.main.menu.groupRiding.groupRidingMap;

public class Group_mapData {
    public Group_mapData(String groupName, String mapName, String lat, String lon) {
        this.groupName = groupName;
        this.mapName = mapName;
        this.lat = lat;
        this.lon = lon;
    }

    String groupName;
    String mapName;
    String lat;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    String lon;


}
