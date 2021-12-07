package cn.mdm.masterui.bean;

import java.io.Serializable;

public class BaseBean implements Serializable {
    protected String name;
    protected int id;
    public BaseBean(){}
    public BaseBean(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
