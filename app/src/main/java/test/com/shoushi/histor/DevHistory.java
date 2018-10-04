package test.com.shoushi.histor;

/**
 * Created by Administrator on 2017/8/17.
 * 获取用水历史提交
 * {"mac":"AA5566","dateTime":"2017-08-03"}
 */

public class DevHistory {

    String mac;
    String dateTime;
    String type;

    public final static String Day = "Day";
    public final static String Week = "Week";
    public final static String Month = "Month";
    public final static String Year = "Year";

    public DevHistory() {

    }

    public DevHistory(String mac, String type, String dateTime) {
        this.mac = mac;
        this.dateTime = dateTime;
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
