package test.com.shoushi.Entity;

import java.util.List;

/**
 * Created by 陈姣姣 on 2017/9/22.
 */

public class QuanPaihang {


    /**
     * result : 1
     * data : [{"id":34,"number":60,"date":null,"uid":4,"uname":"182222","fname":"123baobei","phone":"15827707520","headimg":"D:/fileUpload/SmartJewelry/201709/20170921150058540_939.jpg","isf":null,"sum":0,"num":1,"time":null},{"id":32,"number":0,"date":null,"uid":2,"uname":"懒猫","fname":"tjn","phone":"18166037549","headimg":"D:/fileUpload/SmartJewelry/201709/20170919153952752_231.jpg","isf":null,"sum":0,"num":2,"time":null}]
     * data2 : {"id":34,"number":60,"date":null,"uid":4,"uname":"182222","fname":"123baobei","phone":"15827707520","headimg":"D:/fileUpload/SmartJewelry/201709/20170921150058540_939.jpg","isf":null,"sum":0,"num":1,"time":null}
     */

    private String result;
    /**
     * id : 34
     * number : 60
     * date : null
     * uid : 4
     * uname : 182222
     * fname : 123baobei
     * phone : 15827707520
     * headimg : D:/fileUpload/SmartJewelry/201709/20170921150058540_939.jpg
     * isf : null
     * sum : 0
     * num : 1
     * time : null
     */

    private Data2Bean data2;
    /**
     * id : 34
     * number : 60
     * date : null
     * uid : 4
     * uname : 182222
     * fname : 123baobei
     * phone : 15827707520
     * headimg : D:/fileUpload/SmartJewelry/201709/20170921150058540_939.jpg
     * isf : null
     * sum : 0
     * num : 1
     * time : null
     */

    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Data2Bean getData2() {
        return data2;
    }

    public void setData2(Data2Bean data2) {
        this.data2 = data2;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class Data2Bean {
        private String id;
        private String number;
        private Object date;
        private String uid;
        private String uname;
        private String fname;
        private String phone;
        private String headimg;
        private Object isf;
        private String sum;
        private String num;
        private Object time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public Object getDate() {
            return date;
        }

        public void setDate(Object date) {
            this.date = date;
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

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public Object getIsf() {
            return isf;
        }

        public void setIsf(Object isf) {
            this.isf = isf;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }
    }

    public static class DataBean {
        private String id;
        private String number;
        private Object date;
        private String uid;
        private String uname;
        private String fname;
        private String phone;
        private String headimg;
        private Object isf;
        private String sum;
        private String num;
        private Object time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public Object getDate() {
            return date;
        }

        public void setDate(Object date) {
            this.date = date;
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

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public Object getIsf() {
            return isf;
        }

        public void setIsf(Object isf) {
            this.isf = isf;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }
    }
}
