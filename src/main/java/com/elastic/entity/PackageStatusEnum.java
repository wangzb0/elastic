package com.elastic.entity;

public enum PackageStatusEnum {
    WAIT_AUDIT(1, "待审核"),
    ON_SELL(2, "已上架"),
    OFF_SELL(3, "已下架"),
    NOT_SHARE(4, "暂不共享");

    private Integer key;
    private String val;


    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }


    PackageStatusEnum(Integer key, String val) {
        this.key = key;
        this.val = val;
    }
}
