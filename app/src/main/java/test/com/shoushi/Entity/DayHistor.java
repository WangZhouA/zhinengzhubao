package test.com.shoushi.Entity;

import java.util.List;

/**
 * Created by 陈姣姣 on 2017/9/22.
 */

public class DayHistor {


    /**
     * sum : 3047
     * rows : [{"id":125,"number":3047,"date":null,"uid":4,"sum":0,"time":"2017-11-01"}]
     * result : 1
     */

    private int sum;
    private int result;
    /**
     * id : 125
     * number : 3047
     * date : null
     * uid : 4
     * sum : 0
     * time : 2017-11-01
     */

    private List<RowsBean> rows;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private int id;
        private int number;
        private Object date;
        private int uid;
        private int sum;
        private String time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public Object getDate() {
            return date;
        }

        public void setDate(Object date) {
            this.date = date;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
