package com.el.ariby.ui.api.response;

import java.util.List;

public class WeatherRepoResponse {

    /**
     * response : {"header":{"resultCode":"0000","resultMsg":"OK"},"body":{"items":{"item":[{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1100,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1200,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1100,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1200,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"RN1","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"RN1","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127}]},"numOfRows":10,"pageNo":1,"totalCount":40}}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * header : {"resultCode":"0000","resultMsg":"OK"}
         * body : {"items":{"item":[{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1100,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1200,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1100,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1200,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"RN1","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"RN1","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127}]},"numOfRows":10,"pageNo":1,"totalCount":40}
         */

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
            /**
             * items : {"item":[{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1100,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"LGT","fcstDate":20190528,"fcstTime":1200,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1100,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"PTY","fcstDate":20190528,"fcstTime":1200,"fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"RN1","fcstDate":20190528,"fcstTime":"0900","fcstValue":0,"nx":60,"ny":127},{"baseDate":20190528,"baseTime":"0830","category":"RN1","fcstDate":20190528,"fcstTime":1000,"fcstValue":0,"nx":60,"ny":127}]}
             * numOfRows : 10
             * pageNo : 1
             * totalCount : 40
             */

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
                    /**
                     * baseDate : 20190528
                     * baseTime : 0830
                     * category : LGT
                     * fcstDate : 20190528
                     * fcstTime : 0900
                     * fcstValue : 0
                     * nx : 60
                     * ny : 127
                     */

                    private int baseDate;
                    private String baseTime;
                    private String category;
                    private int fcstDate;
                    private String fcstTime;
                    private int fcstValue;
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

                    public String getFcstTime() {
                        return fcstTime;
                    }

                    public void setFcstTime(String fcstTime) {
                        this.fcstTime = fcstTime;
                    }

                    public int getFcstValue() {
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
