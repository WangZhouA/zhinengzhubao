package test.com.shoushi.histor;

/**
 * Created by 滕韬 on 2017/8/15.
 * 天的历史记录
 * {"num":6.0,"day":"2017-08-11","hour":"10","hourCount":3.0,"tds":7},
 * num：今日用水总量
 * day：当前日期
 * hour：小时
 * hourCount：小时用水量统计
 * tds：今日tds测试总量
 */

public class HDayBean {

    int num;
    String day;
    String hour;
    String t;
    int tds;

    public HDayBean() {


    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }


    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public int getTds() {
        return tds;
    }

    public void setTds(int tds) {
        this.tds = tds;
    }


    @Override
    public String toString() {
        return "num==" + num +
                "day==" + day +
                "hour==" + hour +
                "t==" + t +
                "tds==" + tds;
    }
}
