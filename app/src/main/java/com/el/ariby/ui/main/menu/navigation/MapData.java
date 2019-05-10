package com.el.ariby.ui.main.menu.navigation;

public class MapData {
    String mapName;
    String lat;


    public MapData(String mapName, String lat) {
        this.mapName = mapName;
        this.lat = lat;
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
}
