package test.com.shoushi.Entity;

import java.util.List;

/**
 * Created by qinwei on 2017年3月15日16:00:33
 * 用户信息实体类
 */
public class User {


    /**
     * userID : 7
     * sex :
     * Email :
     * nickname :
     * age :
     * name :
     * list : [{"bindingID":29,"userID":1,"equipmentID":18,"jurisdiction":"1","interpretation":"1","remarks":"啦啦","number":"","mac":"7c:ec:79:e9:f6:6d","name":"他人"},{"bindingID":30,"userID":6,"equipmentID":18,"jurisdiction":"0","interpretation":"1","remarks":"","number":"17688564261","mac":"7c:ec:79:e9:f6:6d","name":"他人"}]
     * number : 13007312924
     * headPicByte :
     * city :
     */

    private int userID;
    private String sex;
    private String Email;
    private String nickname;
    private String age;
    private String name;
    private String number;
    private String headPicByte;
    private String city;
    private List<ListBean> list;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHeadPicByte() {
        return headPicByte;
    }

    public void setHeadPicByte(String headPicByte) {
        this.headPicByte = headPicByte;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        public ListBean(int bindingID, int userID, int equipmentID, String jurisdiction, String interpretation, String remarks, String number, String mac, String name) {
            this.bindingID = bindingID;
            this.userID = userID;
            this.equipmentID = equipmentID;
            this.jurisdiction = jurisdiction;
            this.interpretation = interpretation;
            this.remarks = remarks;
            this.number = number;
            this.mac = mac;
            this.name = name;
        }
        public ListBean() {
        }

        /**
         * bindingID : 29
         * userID : 1
         * equipmentID : 18
         * jurisdiction : 1
         * interpretation : 1
         * remarks : 啦啦
         * number :
         * mac : 7c:ec:79:e9:f6:6d
         * name : 他人
         */

        private int bindingID;
        private int userID;
        private int equipmentID;
        private String jurisdiction;
        private String interpretation;
        private String remarks;
        private String number;
        private String mac;
        private String name;

        public int getBindingID() {
            return bindingID;
        }

        public void setBindingID(int bindingID) {
            this.bindingID = bindingID;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public int getEquipmentID() {
            return equipmentID;
        }

        public void setEquipmentID(int equipmentID) {
            this.equipmentID = equipmentID;
        }

        public String getJurisdiction() {
            return jurisdiction;
        }

        public void setJurisdiction(String jurisdiction) {
            this.jurisdiction = jurisdiction;
        }

        public String getInterpretation() {
            return interpretation;
        }

        public void setInterpretation(String interpretation) {
            this.interpretation = interpretation;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
