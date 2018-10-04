package test.com.shoushi.Entity;

/**
 * Created by 覃微
 * Data:2017/4/5.
 * 设备实体类(蓝牙扫描到的设备)
 */

public class Device {
    private String dname;//设备名字
    private String dmac;//设备mac地址
    private int isstate;//连接状态 0 为未连接 1 正在连接  2已连接
    private String id;
    private String zdyDerviceName;

    public Device() {
        this.dname = "";
        this.dmac = "";
        this.isstate = 0;

    }

    public Device(String dname, String dmac, int isstate,String id,String zdyDerviceName) {
        this.dname = dname;
        this.dmac = dmac;
        this.isstate = isstate;
        this.id =id;
        this.zdyDerviceName=zdyDerviceName;
    }

    public String getZdyDerviceName() {
        return zdyDerviceName;
    }

    public void setZdyDerviceName(String zdyDerviceName) {
        this.zdyDerviceName = zdyDerviceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsstate() {
        return isstate;
    }

    public void setIsstate(int isstate) {
        this.isstate = isstate;
    }


    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDmac() {
        return dmac;
    }

    public void setDmac(String dmac) {
        this.dmac = dmac;
    }
}
