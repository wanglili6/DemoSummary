package com.example.wll.ceshitablayout.bean;

import java.util.List;

/**
 * Created by phillip on 2018/1/18.
 */

public class ModeGoupBean {
    private String id;
    private String name;
    private List<ModeParentBean> adapterBeanList;

    public List<ModeParentBean> getAdapterBeanList() {
        return adapterBeanList;
    }

    public void setAdapterBeanList(List<ModeParentBean> adapterBeanList) {
        this.adapterBeanList = adapterBeanList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
