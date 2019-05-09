package com.el.ariby.ui.main.menu.navigation;

public class MapData {
    String mapname;
    String lat;


    public MapData(String mapname, String lat) {
        this.mapname = mapname;
        this.lat = lat;
    }

    public String getMapname() {
        return mapname;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
