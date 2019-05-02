package com.el.ariby.ui.api.response;

import java.util.List;

public class CoordRepoResponse {

    /**
     * meta : {"total_count":1}
     * documents : [{"x":126.57740680000002,"y":33.453357700000005}]
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
         * x : 126.57740680000002
         * y : 33.453357700000005
         */

        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
