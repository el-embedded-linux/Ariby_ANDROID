package com.el.ariby.ui.api.response;

import java.util.List;

public class MapFindRepoResponse {


    /**
     * type : FeatureCollection
     * features : [{"type":"Feature","geometry":{"type":"Point","coordinates":[126.86558595428724,37.50039090826784]},"properties":{"totalDistance":843,"totalTime":665,"index":0,"pointIndex":0,"name":"","description":"중앙로6길 을 따라 113m 이동","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":200,"pointType":"SP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.86558595428724,37.50039090826784],[126.86567761299129,37.50037702262708],[126.86647198855674,37.50025205131585],[126.8665330942555,37.50024649749529],[126.86661642015162,37.50024094407084],[126.86674140938582,37.50021872665951],[126.86684140027397,37.500218728442036]]},"properties":{"index":1,"lineIndex":0,"name":"중앙로6길","description":"중앙로6길, 113m","distance":113,"time":81,"roadType":0,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.86684140027397,37.500218728442036],[126.86697194393352,37.50021873076923],[126.86726359042027,37.4999909846637],[126.86736358333656,37.49991877261793],[126.86749412957029,37.499827118932295]]},"properties":{"index":2,"lineIndex":1,"name":"경인로47길","description":"경인로47길, 75m","distance":75,"time":54,"roadType":21,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.86749412957029,37.499827118932295]},"properties":{"index":3,"pointIndex":1,"name":"","description":"좌회전 후 경인로47길 을 따라 12m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":12,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.86749412957029,37.499827118932295],[126.86753579329842,37.499796567670735],[126.86756079125448,37.499788235751566],[126.86761634198197,37.49977990437707]]},"properties":{"index":4,"lineIndex":2,"name":"경인로47길","description":"경인로47길, 12m","distance":12,"time":9,"roadType":21,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.86761634198197,37.49977990437707],[126.86766355990143,37.499779905218844],[126.86775799566234,37.499782684357314],[126.86789131661271,37.4997910190989],[126.86801908274775,37.49979102137666],[126.86825239513246,37.499779915716324],[126.86849404016925,37.49976603274965],[126.86854125816674,37.49976325613651],[126.86864402650176,37.499766035423605]]},"properties":{"index":5,"lineIndex":3,"name":"경인로","description":"경인로, 91m","distance":91,"time":65,"roadType":21,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.86864402650176,37.499766035423605]},"properties":{"index":6,"pointIndex":2,"name":"","description":"횡단보도 후 보행자도로 을 따라 46m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"동양공전앞","facilityType":"11","facilityName":"","turnType":211,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.86864402650176,37.499766035423605],[126.86878568189836,37.499707711395345],[126.8691300973765,37.499621616432556]]},"properties":{"index":7,"lineIndex":4,"name":"보행자도로","description":"보행자도로, 46m","distance":46,"time":61,"roadType":21,"categoryRoadType":0,"facilityType":"11","facilityName":""}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.8691300973765,37.499621616432556],[126.87134379819827,37.49913560128613],[126.8714382346616,37.499113383330375]]},"properties":{"index":8,"lineIndex":5,"name":"","description":", 212m","distance":212,"time":151,"roadType":21,"categoryRoadType":0,"facilityType":"","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.8714382346616,37.499113383330375]},"properties":{"index":9,"pointIndex":3,"name":"","description":"직진 후 경인로 을 따라 10m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":11,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.8714382346616,37.499113383330375],[126.87148822979384,37.49912449404156],[126.87154655718807,37.49914671472109]]},"properties":{"index":10,"lineIndex":6,"name":"경인로","description":"경인로, 10m","distance":10,"time":7,"roadType":21,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.87154655718807,37.49914671472109]},"properties":{"index":11,"pointIndex":4,"name":"","description":"좌회전 후 10m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":12,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.87154655718807,37.49914671472109],[126.87155210981902,37.499232815923364]]},"properties":{"index":12,"lineIndex":7,"name":"","description":", 10m","distance":10,"time":8,"roadType":21,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.87155210981902,37.499232815923364]},"properties":{"index":13,"pointIndex":5,"name":"","description":"좌회전 후 30m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":12,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.87155210981902,37.499232815923364],[126.8712160273821,37.4993022463046]]},"properties":{"index":14,"lineIndex":8,"name":"","description":", 30m","distance":30,"time":26,"roadType":0,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.8712160273821,37.4993022463046]},"properties":{"index":15,"pointIndex":6,"name":"","description":"우회전 후 28m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":13,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.8712160273821,37.4993022463046],[126.87124379552971,37.499554995199496]]},"properties":{"index":16,"lineIndex":9,"name":"","description":", 28m","distance":28,"time":23,"roadType":0,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.87124379552971,37.499554995199496]},"properties":{"index":17,"pointIndex":7,"name":"","description":"좌회전 후 49m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":12,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.87124379552971,37.499554995199496],[126.87071050735844,37.49967719370851]]},"properties":{"index":18,"lineIndex":10,"name":"","description":", 49m","distance":49,"time":41,"roadType":0,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.87071050735844,37.49967719370851]},"properties":{"index":19,"pointIndex":8,"name":"","description":"좌회전 후 보행자도로 을 따라 167m 이동 ","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":12,"pointType":"GP"}},{"type":"Feature","geometry":{"type":"LineString","coordinates":[[126.87071050735844,37.49967719370851],[126.87069662098324,37.49963275418185],[126.87059663165499,37.49957720330026],[126.87040499867817,37.49899948925556],[126.87020225841012,37.498321786634456],[126.87019670515488,37.498257905071696]]},"properties":{"index":20,"lineIndex":11,"name":"보행자도로","description":"보행자도로, 167m","distance":167,"time":139,"roadType":23,"categoryRoadType":0,"facilityType":"1","facilityName":""}},{"type":"Feature","geometry":{"type":"Point","coordinates":[126.87019670515488,37.498257905071696]},"properties":{"index":21,"pointIndex":9,"name":"342","description":"도착","direction":"","nearPoiName":"342","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"342","facilityType":"","facilityName":"","turnType":201,"pointType":"EP"}}]
     */

    private String type;
    private List<FeaturesBean> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FeaturesBean> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeaturesBean> features) {
        this.features = features;
    }

    public static class FeaturesBean {
        /**
         * type : Feature
         * geometry : {"type":"Point","coordinates":[126.86558595428724,37.50039090826784]}
         * properties : {"totalDistance":843,"totalTime":665,"index":0,"pointIndex":0,"name":"","description":"중앙로6길 을 따라 113m 이동","direction":"","nearPoiName":"","nearPoiX":"0.0","nearPoiY":"0.0","intersectionName":"","facilityType":"1","facilityName":"","turnType":200,"pointType":"SP"}
         */

        private String type;
        private GeometryBean geometry;
        private PropertiesBean properties;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(PropertiesBean properties) {
            this.properties = properties;
        }

        public static class GeometryBean {
            /**
             * type : Point
             * coordinates : [126.86558595428724,37.50039090826784]
             */

            private String type;
            private List<Double> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<Double> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<Double> coordinates) {
                this.coordinates = coordinates;
            }
        }

        public static class PropertiesBean {
            /**
             * totalDistance : 843
             * totalTime : 665
             * index : 0
             * pointIndex : 0
             * name :
             * description : 중앙로6길 을 따라 113m 이동
             * direction :
             * nearPoiName :
             * nearPoiX : 0.0
             * nearPoiY : 0.0
             * intersectionName :
             * facilityType : 1
             * facilityName :
             * turnType : 200
             * pointType : SP
             */

            private int totalDistance;
            private int totalTime;
            private int index;
            private int pointIndex;
            private String name;
            private String description;
            private String direction;
            private String nearPoiName;
            private String nearPoiX;
            private String nearPoiY;
            private String intersectionName;
            private String facilityType;
            private String facilityName;
            private int turnType;
            private String pointType;

            public int getTotalDistance() {
                return totalDistance;
            }

            public void setTotalDistance(int totalDistance) {
                this.totalDistance = totalDistance;
            }

            public int getTotalTime() {
                return totalTime;
            }

            public void setTotalTime(int totalTime) {
                this.totalTime = totalTime;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getPointIndex() {
                return pointIndex;
            }

            public void setPointIndex(int pointIndex) {
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

            public int getTurnType() {
                return turnType;
            }

            public void setTurnType(int turnType) {
                this.turnType = turnType;
            }

            public String getPointType() {
                return pointType;
            }

            public void setPointType(String pointType) {
                this.pointType = pointType;
            }
        }
    }
}
