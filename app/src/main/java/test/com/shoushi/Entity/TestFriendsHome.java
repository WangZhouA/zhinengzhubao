package test.com.shoushi.Entity;

import java.util.List;

/**
 * Created by 陈姣姣 on 2017/10/28.
 */

public class TestFriendsHome {


    /**
     * id : null
     * img : 11
     * ufid : 0
     * createtime : 2017-10-12 14:10:29
     * uid : 2
     * context : 111
     * name : 懒猫
     * headimg : fileUpload/SmartJewelry/201710/20171018161727650_466.png
     * date : null
     * number : 5
     * sum : 0
     * fabulous : 0
     * shield : 1
     * state : 1
     * mid : 1
     * files : null
     * moments : [{"id":3,"content":"给你一脸","uid":3,"mid":1,"mrid":1,"date":"2017-10-17 11:44:00.0","name":"测试","phone":"18888888888","bname":"测试1号","headimg":null},{"id":1,"content":"six six six","uid":3,"mid":1,"mrid":null,"date":"2017-10-17 11:41:14.0","name":"测试","phone":"18888888888","bname":"测试1号","headimg":null}]
     */

    private List<CommentBean> comment;

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public static class CommentBean {
        private Object id;
        private String img;
        private int ufid;
        private String createtime;
        private int uid;
        private String context;
        private String name;
        private String headimg;
        private Object date;
        private String number;
        private String sum;
        private int fabulous;
        private int shield;
        private String state;
        private int mid;
        private Object files;
        /**
         * id : 3
         * content : 给你一脸
         * uid : 3
         * mid : 1
         * mrid : 1
         * date : 2017-10-17 11:44:00.0
         * name : 测试
         * phone : 18888888888
         * bname : 测试1号
         * headimg : null
         */

        private List<MomentsBean> moments;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getUfid() {
            return ufid;
        }

        public void setUfid(int ufid) {
            this.ufid = ufid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public Object getDate() {
            return date;
        }

        public void setDate(Object date) {
            this.date = date;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public int getFabulous() {
            return fabulous;
        }

        public void setFabulous(int fabulous) {
            this.fabulous = fabulous;
        }

        public int getShield() {
            return shield;
        }

        public void setShield(int shield) {
            this.shield = shield;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public Object getFiles() {
            return files;
        }

        public void setFiles(Object files) {
            this.files = files;
        }

        public List<MomentsBean> getMoments() {
            return moments;
        }

        public void setMoments(List<MomentsBean> moments) {
            this.moments = moments;
        }

        public static class MomentsBean {
            private int id;
            private String content;
            private int uid;
            private int mid;
            private int mrid;
            private String date;
            private String name;
            private String phone;
            private String bname;
            private Object headimg;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public int getMrid() {
                return mrid;
            }

            public void setMrid(int mrid) {
                this.mrid = mrid;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getBname() {
                return bname;
            }

            public void setBname(String bname) {
                this.bname = bname;
            }

            public Object getHeadimg() {
                return headimg;
            }

            public void setHeadimg(Object headimg) {
                this.headimg = headimg;
            }
        }
    }
}
