package test.com.shoushi.histor;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 * {"result":"result",
 * "data":[{"num":6.0,"day":"2017-08-11","hour":"10","hourCount":3.0,"tds":7},
 * {""num":6.0,"day":"2017-08-11","hour":"11","hourCount":3.0,"tds":7}]}
 * num：今日用水总量
 * day：当前日期
 * hour：小时
 * hourCount：小时用水量统计
 * tds：今日tds测试总量
 */

public class DayRespon {

    String result;
    List<HDayBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<HDayBean> getData() {
        return data;
    }

    public void setData(List<HDayBean> data) {
        this.data = data;
    }
}
