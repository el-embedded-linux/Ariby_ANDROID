package com.el.ariby.ui.api.response;

import java.util.List;

public class GeoRepoResponse {
    /**
     * meta : {"total_count":1}
     * documents : [{"road_address":{"address_name":"경기도 안성시 죽산면 죽산초교길 69-4","region_1depth_name":"경기","region_2depth_name":"안성시","region_3depth_name":"죽산면","road_name":"죽산초교길","underground_yn":"N","main_building_no":"69","sub_building_no":"4","building_name":"무지개아파트","zone_no":"17519"},"address":{"address_name":"경기 안성시 죽산면 죽산리 343-1","region_1depth_name":"경기","region_2depth_name":"안성시","region_3depth_name":"죽산면 죽산리","mountain_yn":"N","main_address_no":"343","sub_address_no":"1","zip_code":""}}]
     */

    private MetaBean meta;
    private List<DocumentsBean> documents;

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public List<DocumentsBean> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentsBean> documents) {
        this.documents = documents;
    }

    public static class MetaBean {
        /**
         * total_count : 1
         */

        private int total_count;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }
    }

    public static class DocumentsBean {
        /**
         * road_address : {"address_name":"경기도 안성시 죽산면 죽산초교길 69-4","region_1depth_name":"경기","region_2depth_name":"안성시","region_3depth_name":"죽산면","road_name":"죽산초교길","underground_yn":"N","main_building_no":"69","sub_building_no":"4","building_name":"무지개아파트","zone_no":"17519"}
         * address : {"address_name":"경기 안성시 죽산면 죽산리 343-1","region_1depth_name":"경기","region_2depth_name":"안성시","region_3depth_name":"죽산면 죽산리","mountain_yn":"N","main_address_no":"343","sub_address_no":"1","zip_code":""}
         */

        private RoadAddressBean road_address;
        private AddressBean address;

        public RoadAddressBean getRoad_address() {
            return road_address;
        }

        public void setRoad_address(RoadAddressBean road_address) {
            this.road_address = road_address;
        }

        public AddressBean getAddress() {
            return address;
        }

        public void setAddress(AddressBean address) {
            this.address = address;
        }

        public static class RoadAddressBean {
            /**
             * address_name : 경기도 안성시 죽산면 죽산초교길 69-4
             * region_1depth_name : 경기
             * region_2depth_name : 안성시
             * region_3depth_name : 죽산면
             * road_name : 죽산초교길
             * underground_yn : N
             * main_building_no : 69
             * sub_building_no : 4
             * building_name : 무지개아파트
             * zone_no : 17519
             */

            private String address_name;
            private String region_1depth_name;
            private String region_2depth_name;
            private String region_3depth_name;
            private String road_name;
            private String underground_yn;
            private String main_building_no;
            private String sub_building_no;
            private String building_name;
            private String zone_no;

            public String getAddress_name() {
                return address_name;
            }

            public void setAddress_name(String address_name) {
                this.address_name = address_name;
            }

            public String getRegion_1depth_name() {
                return region_1depth_name;
            }

            public void setRegion_1depth_name(String region_1depth_name) {
                this.region_1depth_name = region_1depth_name;
            }

            public String getRegion_2depth_name() {
                return region_2depth_name;
            }

            public void setRegion_2depth_name(String region_2depth_name) {
                this.region_2depth_name = region_2depth_name;
            }

            public String getRegion_3depth_name() {
                return region_3depth_name;
            }

            public void setRegion_3depth_name(String region_3depth_name) {
                this.region_3depth_name = region_3depth_name;
            }

            public String getRoad_name() {
                return road_name;
            }

            public void setRoad_name(String road_name) {
                this.road_name = road_name;
            }

            public String getUnderground_yn() {
                return underground_yn;
            }

            public void setUnderground_yn(String underground_yn) {
                this.underground_yn = underground_yn;
            }

            public String getMain_building_no() {
                return main_building_no;
            }

            public void setMain_building_no(String main_building_no) {
                this.main_building_no = main_building_no;
            }

            public String getSub_building_no() {
                return sub_building_no;
            }

            public void setSub_building_no(String sub_building_no) {
                this.sub_building_no = sub_building_no;
            }

            public String getBuilding_name() {
                return building_name;
            }

            public void setBuilding_name(String building_name) {
                this.building_name = building_name;
            }

            public String getZone_no() {
                return zone_no;
            }

            public void setZone_no(String zone_no) {
                this.zone_no = zone_no;
            }
        }

        public static class AddressBean {
            /**
             * address_name : 경기 안성시 죽산면 죽산리 343-1
             * region_1depth_name : 경기
             * region_2depth_name : 안성시
             * region_3depth_name : 죽산면 죽산리
             * mountain_yn : N
             * main_address_no : 343
             * sub_address_no : 1
             * zip_code :
             */

            private String address_name;
            private String region_1depth_name;
            private String region_2depth_name;
            private String region_3depth_name;
            private String mountain_yn;
            private String main_address_no;
            private String sub_address_no;
            private String zip_code;

            public String getAddress_name() {
                return address_name;
            }

            public void setAddress_name(String address_name) {
                this.address_name = address_name;
            }

            public String getRegion_1depth_name() {
                return region_1depth_name;
            }

            public void setRegion_1depth_name(String region_1depth_name) {
                this.region_1depth_name = region_1depth_name;
            }

            public String getRegion_2depth_name() {
                return region_2depth_name;
            }

            public void setRegion_2depth_name(String region_2depth_name) {
                this.region_2depth_name = region_2depth_name;
            }

            public String getRegion_3depth_name() {
                return region_3depth_name;
            }

            public void setRegion_3depth_name(String region_3depth_name) {
                this.region_3depth_name = region_3depth_name;
            }

            public String getMountain_yn() {
                return mountain_yn;
            }

            public void setMountain_yn(String mountain_yn) {
                this.mountain_yn = mountain_yn;
            }

            public String getMain_address_no() {
                return main_address_no;
            }

            public void setMain_address_no(String main_address_no) {
                this.main_address_no = main_address_no;
            }

            public String getSub_address_no() {
                return sub_address_no;
            }

            public void setSub_address_no(String sub_address_no) {
                this.sub_address_no = sub_address_no;
            }

            public String getZip_code() {
                return zip_code;
            }

            public void setZip_code(String zip_code) {
                this.zip_code = zip_code;
            }
        }
    }
}
