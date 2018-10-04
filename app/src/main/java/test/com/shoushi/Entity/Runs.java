package test.com.shoushi.Entity;

/**
 * Created by 陈姣姣 on 2017/9/16.
 */

public class Runs {

    String fname;  //备注名
    String headimg;    //头像
    String num; //名次排行
    String number;      // 步数
    String id; //计步id
    String isf; //是否点赞
    String phone;// 电话号码
    String sum;  //获取点赞的总数
    String uname;  //用户名
    String uid;   //用户id
    String did;
    String name;

    public Runs(String fname, String headimg, String num, String number, String id, String isf, String phone, String sum, String uname, String uid, String did,String name) {
        this.fname = fname;
        this.headimg = headimg;
        this.num = num;
        this.number = number;
        this.id = id;
        this.isf = isf;
        this.phone = phone;
        this.sum = sum;
        this.uname = uname;
        this.uid = uid;
        this.did = did;
        this.name=name;
    }

    public Runs() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }
}
