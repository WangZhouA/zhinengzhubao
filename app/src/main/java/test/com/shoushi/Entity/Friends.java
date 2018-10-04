package test.com.shoushi.Entity;

/**
 * Created by 陈姣姣 on 2017/9/13.
 */

public class Friends {

  private String age;
  private String fName;  //备注名
  private String headimg;
  private String id;
  private String name;
  private String password;
  private String phone;
  private String sex;

    public Friends() {
    }

    public Friends(String age, String fName, String headimg, String id, String name, String password, String phone, String sex) {
        this.age = age;
        this.fName = fName;
        this.headimg = headimg;
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
