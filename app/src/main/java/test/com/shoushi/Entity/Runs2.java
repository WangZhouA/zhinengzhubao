package test.com.shoushi.Entity;

import java.util.List;

/**
 * Created by 陈姣姣 on 2017/9/19.
 */

public class Runs2 {

    /**
     * data : [{"date":1,"headimg":1,"fname":"测试1号","id":2,"isf":"2","num":1,"number":2889,"phone":"18888888888","sum":2,"uid":3,"uname":"测试"},{"date":1,"headimg":1,"fname":"null","id":1,"isf":"null","num":2,"number":2356,"phone":"18166037549","sum":0,"uid":2,"uname":"懒猫"}]
     * result : 1
     */

    private String result;
    /**
     * date : 1
     * headimg : 1
     * fname : 测试1号
     * id : 2
     * isf : 2
     * num : 1
     * number : 2889
     * phone : 18888888888
     * sum : 2
     * uid : 3
     * uname : 测试
     */

    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String date;
        private String headimg;
        private String fname;
        private String id;
        private String isf;
        private String num;
        private String number;
        private String phone;
        private String sum;
        private String uid;
        private String uname;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsf() {
            return isf;
        }

        public void setIsf(String isf) {
            this.isf = isf;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }
    }
}
