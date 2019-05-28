package com.el.ariby.ui.api.response;

import java.util.List;

public class WeatherRepoResponse {

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        private HeaderBean header;
        private BodyBean body;

        public HeaderBean getHeader() {
            return header;
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public static class HeaderBean {
            /**
             * resultCode : 0000
             * resultMsg : OK
             */

            private String resultCode;
            private String resultMsg;

            public String getResultCode() {
                return resultCode;
            }

            public void setResultCode(String resultCode) {
                this.resultCode = resultCode;
            }

            public String getResultMsg() {
                return resultMsg;
            }

            public void setResultMsg(String resultMsg) {
                this.resultMsg = resultMsg;
            }
        }

        public static class BodyBean {
            private ItemsBean items;
            private int numOfRows;
            private int pageNo;
            private int totalCount;

            public ItemsBean getItems() {
                return items;
            }

            public void setItems(ItemsBean items) {
                this.items = items;
            }

            public int getNumOfRows() {
                return numOfRows;
            }

            public void setNumOfRows(int numOfRows) {
                this.numOfRows = numOfRows;
            }

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }

            public static class ItemsBean {
                private List<ItemBean> item;

                public List<ItemBean> getItem() {
                    return item;
                }

                public void setItem(List<ItemBean> item) {
                    this.item = item;
                }

                public static class ItemBean {
                    private int baseDate;
                    private String baseTime;
                    private String category;
                    private int fcstDate;
                    private int fcstTime;
                    private Object fcstValue;
                    private int nx;
                    private int ny;

                    public int getBaseDate() {
                        return baseDate;
                    }

                    public void setBaseDate(int baseDate) {
                        this.baseDate = baseDate;
                    }

                    public String getBaseTime() {
                        return baseTime;
                    }

                    public void setBaseTime(String baseTime) {
                        this.baseTime = baseTime;
                    }

                    public String getCategory() {
                        return category;
                    }

                    public void setCategory(String category) {
                        this.category = category;
                    }

                    public int getFcstDate() {
                        return fcstDate;
                    }

                    public void setFcstDate(int fcstDate) {
                        this.fcstDate = fcstDate;
                    }

                    public int getFcstTime() {
                        return fcstTime;
                    }

                    public void setFcstTime(int fcstTime) {
                        this.fcstTime = fcstTime;
                    }

                    public Object getFcstValue() {
                        return fcstValue;
                    }

                    public void setFcstValue(int fcstValue) {
                        this.fcstValue = fcstValue;
                    }

                    public int getNx() {
                        return nx;
                    }

                    public void setNx(int nx) {
                        this.nx = nx;
                    }

                    public int getNy() {
                        return ny;
                    }

                    public void setNy(int ny) {
                        this.ny = ny;
                    }
                }
            }
        }
    }
}
