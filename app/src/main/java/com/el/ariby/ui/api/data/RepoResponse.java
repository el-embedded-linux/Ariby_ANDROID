package com.el.ariby.ui.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepoResponse {
    @SerializedName("CtprvnMesureLIstVo2")
    private CtprvnMesureLIstVo2Bean cvrVo;
    @SerializedName("list")
    private List<CtprvnMesureLIstVo2Bean> list;
    @SerializedName("parm")
    private CtprvnMesureLIstVo2Bean parm;
    @SerializedName("totalCount")
    private int totalCount;

    public CtprvnMesureLIstVo2Bean getSvrVo() {
        return cvrVo;
    }

    public void setSvrVo(CtprvnMesureLIstVo2Bean svrVo) {
        this.cvrVo = svrVo;
    }

    public List<CtprvnMesureLIstVo2Bean> getList() {
        return list;
    }

    public void setList(List<CtprvnMesureLIstVo2Bean> list) {
        this.list = list;
    }

    public CtprvnMesureLIstVo2Bean getParm() {
        return parm;
    }

    public void setParm(CtprvnMesureLIstVo2Bean parm) {
        this.parm = parm;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}


