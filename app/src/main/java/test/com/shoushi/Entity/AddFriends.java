package test.com.shoushi.Entity;

/**
 * Created by 陈姣姣 on 2017/9/16.
 */

public class AddFriends {

     String afuid;
     String id;
     String information;
     String name;
     String state;
     String uid;


    public AddFriends() {
    }

    public AddFriends(String afuid, String id, String information, String name, String state, String uid) {
        this.afuid = afuid;
        this.id = id;
        this.information = information;
        this.name = name;
        this.state = state;
        this.uid = uid;
    }

    public String getAfuid() {
        return afuid;
    }

    public void setAfuid(String afuid) {
        this.afuid = afuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
