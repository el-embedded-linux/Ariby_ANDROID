package com.el.ariby.ui.api.response;

import java.util.List;

public class MeasureRepoResponse {

    /**
     * MsrstnInfoInqireSvrVo : {"_returnType":"json","addr":"","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"vMgzVOM7K3D3t89QY//tYxGc7fTDhMi3AkGCqakZut7sDmQCzfeWtcT9NDRbrR4dK8OBJsR5d4QwhZkn/eTZ3w==","sggName":"","sidoName":"","stationCode":"","stationName":"","tm":0,"tmX":"188237.07711298","tmY":"444227.3206368848","totalCount":"","umdName":"","ver":"","vrml":"","year":""}
     * list : [{"_returnType":"json","addr":"서울 구로구 가마산로 27길 45구로고등학교","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sggName":"","sidoName":"","stationCode":"","stationName":"구로구","tm":1.9,"tmX":"","tmY":"","totalCount":"","umdName":"","ver":"","vrml":"","year":""},{"_returnType":"json","addr":"경기 광명시 시청로 20광명시청 제1별관","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sggName":"","sidoName":"","stationCode":"","stationName":"철산동","tm":2.7,"tmX":"","tmY":"","totalCount":"","umdName":"","ver":"","vrml":"","year":""},{"_returnType":"json","addr":"서울 양천구 중앙로52길 56신정4동 문화센터","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sggName":"","sidoName":"","stationCode":"","stationName":"양천구","tm":2.7,"tmX":"","tmY":"","totalCount":"","umdName":"","ver":"","vrml":"","year":""}]
     * parm : {"_returnType":"json","addr":"","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"vMgzVOM7K3D3t89QY//tYxGc7fTDhMi3AkGCqakZut7sDmQCzfeWtcT9NDRbrR4dK8OBJsR5d4QwhZkn/eTZ3w==","sggName":"","sidoName":"","stationCode":"","stationName":"","tm":0,"tmX":"188237.07711298","tmY":"444227.3206368848","totalCount":"","umdName":"","ver":"","vrml":"","year":""}
     * totalCount : 3
     */

    private MsrstnInfoInqireSvrVoBean MsrstnInfoInqireSvrVo;
    private ParmBean parm;
    private int totalCount;
    private List<ListBean> list;

    public MsrstnInfoInqireSvrVoBean getMsrstnInfoInqireSvrVo() {
        return MsrstnInfoInqireSvrVo;
    }

    public void setMsrstnInfoInqireSvrVo(MsrstnInfoInqireSvrVoBean MsrstnInfoInqireSvrVo) {
        this.MsrstnInfoInqireSvrVo = MsrstnInfoInqireSvrVo;
    }

    public ParmBean getParm() {
        return parm;
    }

    public void setParm(ParmBean parm) {
        this.parm = parm;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class MsrstnInfoInqireSvrVoBean {
        /**
         * _returnType : json
         * addr :
         * districtNum :
         * dmX :
         * dmY :
         * item :
         * mangName :
         * map :
         * numOfRows : 10
         * oper :
         * pageNo : 1
         * photo :
         * resultCode :
         * resultMsg :
         * rnum : 0
         * serviceKey : vMgzVOM7K3D3t89QY//tYxGc7fTDhMi3AkGCqakZut7sDmQCzfeWtcT9NDRbrR4dK8OBJsR5d4QwhZkn/eTZ3w==
         * sggName :
         * sidoName :
         * stationCode :
         * stationName :
         * tm : 0
         * tmX : 188237.07711298
         * tmY : 444227.3206368848
         * totalCount :
         * umdName :
         * ver :
         * vrml :
         * year :
         */

        private String _returnType;
        private String addr;
        private String districtNum;
        private String dmX;
        private String dmY;
        private String item;
        private String mangName;
        private String map;
        private String numOfRows;
        private String oper;
        private String pageNo;
        private String photo;
        private String resultCode;
        private String resultMsg;
        private int rnum;
        private String serviceKey;
        private String sggName;
        private String sidoName;
        private String stationCode;
        private String stationName;
        private int tm;
        private String tmX;
        private String tmY;
        private String totalCount;
        private String umdName;
        private String ver;
        private String vrml;
        private String year;

        public String get_returnType() {
            return _returnType;
        }

        public void set_returnType(String _returnType) {
            this._returnType = _returnType;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getDistrictNum() {
            return districtNum;
        }

        public void setDistrictNum(String districtNum) {
            this.districtNum = districtNum;
        }

        public String getDmX() {
            return dmX;
        }

        public void setDmX(String dmX) {
            this.dmX = dmX;
        }

        public String getDmY() {
            return dmY;
        }

        public void setDmY(String dmY) {
            this.dmY = dmY;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getMangName() {
            return mangName;
        }

        public void setMangName(String mangName) {
            this.mangName = mangName;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public String getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(String numOfRows) {
            this.numOfRows = numOfRows;
        }

        public String getOper() {
            return oper;
        }

        public void setOper(String oper) {
            this.oper = oper;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

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

        public int getRnum() {
            return rnum;
        }

        public void setRnum(int rnum) {
            this.rnum = rnum;
        }

        public String getServiceKey() {
            return serviceKey;
        }

        public void setServiceKey(String serviceKey) {
            this.serviceKey = serviceKey;
        }

        public String getSggName() {
            return sggName;
        }

        public void setSggName(String sggName) {
            this.sggName = sggName;
        }

        public String getSidoName() {
            return sidoName;
        }

        public void setSidoName(String sidoName) {
            this.sidoName = sidoName;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public int getTm() {
            return tm;
        }

        public void setTm(int tm) {
            this.tm = tm;
        }

        public String getTmX() {
            return tmX;
        }

        public void setTmX(String tmX) {
            this.tmX = tmX;
        }

        public String getTmY() {
            return tmY;
        }

        public void setTmY(String tmY) {
            this.tmY = tmY;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getUmdName() {
            return umdName;
        }

        public void setUmdName(String umdName) {
            this.umdName = umdName;
        }

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getVrml() {
            return vrml;
        }

        public void setVrml(String vrml) {
            this.vrml = vrml;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public static class ParmBean {
        /**
         * _returnType : json
         * addr :
         * districtNum :
         * dmX :
         * dmY :
         * item :
         * mangName :
         * map :
         * numOfRows : 10
         * oper :
         * pageNo : 1
         * photo :
         * resultCode :
         * resultMsg :
         * rnum : 0
         * serviceKey : vMgzVOM7K3D3t89QY//tYxGc7fTDhMi3AkGCqakZut7sDmQCzfeWtcT9NDRbrR4dK8OBJsR5d4QwhZkn/eTZ3w==
         * sggName :
         * sidoName :
         * stationCode :
         * stationName :
         * tm : 0
         * tmX : 188237.07711298
         * tmY : 444227.3206368848
         * totalCount :
         * umdName :
         * ver :
         * vrml :
         * year :
         */

        private String _returnType;
        private String addr;
        private String districtNum;
        private String dmX;
        private String dmY;
        private String item;
        private String mangName;
        private String map;
        private String numOfRows;
        private String oper;
        private String pageNo;
        private String photo;
        private String resultCode;
        private String resultMsg;
        private int rnum;
        private String serviceKey;
        private String sggName;
        private String sidoName;
        private String stationCode;
        private String stationName;
        private int tm;
        private String tmX;
        private String tmY;
        private String totalCount;
        private String umdName;
        private String ver;
        private String vrml;
        private String year;

        public String get_returnType() {
            return _returnType;
        }

        public void set_returnType(String _returnType) {
            this._returnType = _returnType;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getDistrictNum() {
            return districtNum;
        }

        public void setDistrictNum(String districtNum) {
            this.districtNum = districtNum;
        }

        public String getDmX() {
            return dmX;
        }

        public void setDmX(String dmX) {
            this.dmX = dmX;
        }

        public String getDmY() {
            return dmY;
        }

        public void setDmY(String dmY) {
            this.dmY = dmY;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getMangName() {
            return mangName;
        }

        public void setMangName(String mangName) {
            this.mangName = mangName;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public String getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(String numOfRows) {
            this.numOfRows = numOfRows;
        }

        public String getOper() {
            return oper;
        }

        public void setOper(String oper) {
            this.oper = oper;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

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

        public int getRnum() {
            return rnum;
        }

        public void setRnum(int rnum) {
            this.rnum = rnum;
        }

        public String getServiceKey() {
            return serviceKey;
        }

        public void setServiceKey(String serviceKey) {
            this.serviceKey = serviceKey;
        }

        public String getSggName() {
            return sggName;
        }

        public void setSggName(String sggName) {
            this.sggName = sggName;
        }

        public String getSidoName() {
            return sidoName;
        }

        public void setSidoName(String sidoName) {
            this.sidoName = sidoName;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public int getTm() {
            return tm;
        }

        public void setTm(int tm) {
            this.tm = tm;
        }

        public String getTmX() {
            return tmX;
        }

        public void setTmX(String tmX) {
            this.tmX = tmX;
        }

        public String getTmY() {
            return tmY;
        }

        public void setTmY(String tmY) {
            this.tmY = tmY;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getUmdName() {
            return umdName;
        }

        public void setUmdName(String umdName) {
            this.umdName = umdName;
        }

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getVrml() {
            return vrml;
        }

        public void setVrml(String vrml) {
            this.vrml = vrml;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public static class ListBean {
        /**
         * _returnType : json
         * addr : 서울 구로구 가마산로 27길 45구로고등학교
         * districtNum :
         * dmX :
         * dmY :
         * item :
         * mangName :
         * map :
         * numOfRows : 10
         * oper :
         * pageNo : 1
         * photo :
         * resultCode :
         * resultMsg :
         * rnum : 0
         * serviceKey :
         * sggName :
         * sidoName :
         * stationCode :
         * stationName : 구로구
         * tm : 1.9
         * tmX :
         * tmY :
         * totalCount :
         * umdName :
         * ver :
         * vrml :
         * year :
         */

        private String _returnType;
        private String addr;
        private String districtNum;
        private String dmX;
        private String dmY;
        private String item;
        private String mangName;
        private String map;
        private String numOfRows;
        private String oper;
        private String pageNo;
        private String photo;
        private String resultCode;
        private String resultMsg;
        private int rnum;
        private String serviceKey;
        private String sggName;
        private String sidoName;
        private String stationCode;
        private String stationName;
        private double tm;
        private String tmX;
        private String tmY;
        private String totalCount;
        private String umdName;
        private String ver;
        private String vrml;
        private String year;

        public String get_returnType() {
            return _returnType;
        }

        public void set_returnType(String _returnType) {
            this._returnType = _returnType;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getDistrictNum() {
            return districtNum;
        }

        public void setDistrictNum(String districtNum) {
            this.districtNum = districtNum;
        }

        public String getDmX() {
            return dmX;
        }

        public void setDmX(String dmX) {
            this.dmX = dmX;
        }

        public String getDmY() {
            return dmY;
        }

        public void setDmY(String dmY) {
            this.dmY = dmY;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getMangName() {
            return mangName;
        }

        public void setMangName(String mangName) {
            this.mangName = mangName;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public String getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(String numOfRows) {
            this.numOfRows = numOfRows;
        }

        public String getOper() {
            return oper;
        }

        public void setOper(String oper) {
            this.oper = oper;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

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

        public int getRnum() {
            return rnum;
        }

        public void setRnum(int rnum) {
            this.rnum = rnum;
        }

        public String getServiceKey() {
            return serviceKey;
        }

        public void setServiceKey(String serviceKey) {
            this.serviceKey = serviceKey;
        }

        public String getSggName() {
            return sggName;
        }

        public void setSggName(String sggName) {
            this.sggName = sggName;
        }

        public String getSidoName() {
            return sidoName;
        }

        public void setSidoName(String sidoName) {
            this.sidoName = sidoName;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public double getTm() {
            return tm;
        }

        public void setTm(double tm) {
            this.tm = tm;
        }

        public String getTmX() {
            return tmX;
        }

        public void setTmX(String tmX) {
            this.tmX = tmX;
        }

        public String getTmY() {
            return tmY;
        }

        public void setTmY(String tmY) {
            this.tmY = tmY;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getUmdName() {
            return umdName;
        }

        public void setUmdName(String umdName) {
            this.umdName = umdName;
        }

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getVrml() {
            return vrml;
        }

        public void setVrml(String vrml) {
            this.vrml = vrml;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
