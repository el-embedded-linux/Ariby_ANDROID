package com.el.ariby.ui.api.response;

import com.google.android.gms.common.Feature;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapFindRepoResponse {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }


    public static class Feature {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("properties")
        @Expose
        private Properties properties;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

    }

    public static class Geometry {
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("coordinates")
        @Expose
        private List<Object> coordinates = null;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Object> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Object> coordinates) {
            this.coordinates = coordinates;
        }

    }

    public static class Properties {
        @SerializedName("totalDistance")
        @Expose
        private Integer totalDistance;
        @SerializedName("totalTime")
        @Expose
        private Integer totalTime;
        @SerializedName("index")
        @Expose
        private Integer index;
        @SerializedName("pointIndex")
        @Expose
        private Integer pointIndex;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("direction")
        @Expose
        private String direction;
        @SerializedName("nearPoiName")
        @Expose
        private String nearPoiName;
        @SerializedName("nearPoiX")
        @Expose
        private String nearPoiX;
        @SerializedName("nearPoiY")
        @Expose
        private String nearPoiY;
        @SerializedName("intersectionName")
        @Expose
        private String intersectionName;
        @SerializedName("facilityType")
        @Expose
        private String facilityType;
        @SerializedName("facilityName")
        @Expose
        private String facilityName;
        @SerializedName("turnType")
        @Expose
        private Integer turnType;
        @SerializedName("pointType")
        @Expose
        private String pointType;
        @SerializedName("lineIndex")
        @Expose
        private Integer lineIndex;
        @SerializedName("distance")
        @Expose
        private Integer distance;
        @SerializedName("time")
        @Expose
        private Integer time;
        @SerializedName("roadType")
        @Expose
        private Integer roadType;
        @SerializedName("categoryRoadType")
        @Expose
        private Integer categoryRoadType;

        public Integer getTotalDistance() {
            return totalDistance;
        }

        public void setTotalDistance(Integer totalDistance) {
            this.totalDistance = totalDistance;
        }

        public Integer getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(Integer totalTime) {
            this.totalTime = totalTime;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Integer getPointIndex() {
            return pointIndex;
        }

        public void setPointIndex(Integer pointIndex) {
            this.pointIndex = pointIndex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getNearPoiName() {
            return nearPoiName;
        }

        public void setNearPoiName(String nearPoiName) {
            this.nearPoiName = nearPoiName;
        }

        public String getNearPoiX() {
            return nearPoiX;
        }

        public void setNearPoiX(String nearPoiX) {
            this.nearPoiX = nearPoiX;
        }

        public String getNearPoiY() {
            return nearPoiY;
        }

        public void setNearPoiY(String nearPoiY) {
            this.nearPoiY = nearPoiY;
        }

        public String getIntersectionName() {
            return intersectionName;
        }

        public void setIntersectionName(String intersectionName) {
            this.intersectionName = intersectionName;
        }

        public String getFacilityType() {
            return facilityType;
        }

        public void setFacilityType(String facilityType) {
            this.facilityType = facilityType;
        }

        public String getFacilityName() {
            return facilityName;
        }

        public void setFacilityName(String facilityName) {
            this.facilityName = facilityName;
        }

        public Integer getTurnType() {
            return turnType;
        }

        public void setTurnType(Integer turnType) {
            this.turnType = turnType;
        }

        public String getPointType() {
            return pointType;
        }

        public void setPointType(String pointType) {
            this.pointType = pointType;
        }

        public Integer getLineIndex() {
            return lineIndex;
        }

        public void setLineIndex(Integer lineIndex) {
            this.lineIndex = lineIndex;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }

        public Integer getRoadType() {
            return roadType;
        }

        public void setRoadType(Integer roadType) {
            this.roadType = roadType;
        }

        public Integer getCategoryRoadType() {
            return categoryRoadType;
        }

        public void setCategoryRoadType(Integer categoryRoadType) {
            this.categoryRoadType = categoryRoadType;
        }
    }
}