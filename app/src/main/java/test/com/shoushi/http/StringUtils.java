package test.com.shoushi.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by 陈姣姣 on 2017/9/9.
 */

public class StringUtils  {



    //服务器
//    public  static  String  HTTP_SERVICE ="http://172.16.2.23:8080/";
//    public  static  String  HTTP_SERVICE ="http://172.16.2.27:8080/";       //内
    public  static  String  HTTP_SERVICE ="http://39.108.55.73:80/";       //外

    //注册的验证码
    public  static  String  GET_CODE =  HTTP_SERVICE+ "SmartJewelry/user/getAddCode";
    //注册
    public  static  String  Reigst =  HTTP_SERVICE+ "SmartJewelry/user/addUser/";
    //登录
    public  static  String  LOGIN =  HTTP_SERVICE+"SmartJewelry/user/Login";
    //修改密码的验证码
    public  static  String  RE_GET_CODE =  HTTP_SERVICE +"SmartJewelry/user/getUpdateCode";
    //修改密码
    public  static  String  RE_PASSWORD = HTTP_SERVICE + "SmartJewelry/user/updatePassWord/";
    //添加设备
    public  static  String  ADD_DERVICE_MAC = HTTP_SERVICE + "SmartJewelry/device/addDevice/";
    //查询设备信息
    public  static  String  QUERY_DERVICE = HTTP_SERVICE + "SmartJewelry/device/queryDevice/";
    //删除设备
    public  static  String  DELETER_DERVICE = HTTP_SERVICE +"SmartJewelry/device/DeleteDevice/";
    //改名字
    public  static  String  RE_NAME = HTTP_SERVICE + "SmartJewelry/device/updateDeviceName/";
    //查询好友
    public  static  String   QUERY_FRIENDS = HTTP_SERVICE + "SmartJewelry/Friend/getFriendList/";
    //删除好友
    public  static  String DELETER_FRIENDS = HTTP_SERVICE  + "SmartJewelry/Friend/deleteFriend";
    //修改好友备注名
    public  static  String RE_NAME_FRIENDS = HTTP_SERVICE+ "SmartJewelry/Friend/updateFName";
    //获取好友申请
    public  static  String  QUERT_FRIENDS_ADD =HTTP_SERVICE +"SmartJewelry/Friend/getFriendRequest/";
    //通过好友申请
    public  static  String TONG_GUO_FRIENDS_ADD =HTTP_SERVICE+"SmartJewelry/Friend/adoptRequest/";
    //删除好友申请
    public  static  String DELETE_FRIENDS_SHENQING = HTTP_SERVICE + "SmartJewelry/Friend/refuseRequest/";
    //申请好友
    public  static  String SHENQING_FRIENDS =HTTP_SERVICE +"SmartJewelry/Friend/friendRequest/";
    //修改用户信息
    public  static  String  REUSER_MSG =HTTP_SERVICE+"SmartJewelry/user/updateUser";
    //查询用户信息
    public  static  String QUERY_USER_MSG = HTTP_SERVICE +"SmartJewelry/user/queryUser";
    //上传头像
    public  static  String UPDATE_USER_IMG = HTTP_SERVICE + "SmartJewelry/user/uploadHeadimg/";
    //搜索好友
    public  static  String SOUSUO_FRIENDS = HTTP_SERVICE+ "SmartJewelry/user/queryUser";
    //计步排行榜
    public  static  String RUN_PAIHANGBANG = HTTP_SERVICE + "SmartJewelry/StepCounter/friendCounterRank/";
    //排行榜点赞
    //查询每天的步数总数
    public  static  String QUERY_DAY =  HTTP_SERVICE+ "SmartJewelry/StepCounter/SameDay/";
    //查询历史记录
    public  static  String HISTOR_READ = HTTP_SERVICE + "SmartJewelry/StepCounter/historyCounter";
    //查询每天历史记录
    public  static  String DAY_HISTOR_READ = HTTP_SERVICE + "SmartJewelry/StepCounter/queryDayHistory/";
    //查询用户个人的排行榜
    public  static  String USER_PAIHANGBANG = HTTP_SERVICE + "SmartJewelry/StepCounter/userCounterRank/";
    //添加计步
    public  static  String ADD_DAY_STEP = HTTP_SERVICE + "SmartJewelry/StepCounter/addStepCounter";
    //添加坐标点
    public  static  String ADD_DAY_POINT = HTTP_SERVICE+ "SmartJewelry/StepCounter/addTrajectory";
    //查询每天的坐标点
    public  static  String QUERY_DAY_POINT = HTTP_SERVICE+ "SmartJewelry/StepCounter/queryTrajectory";
    //发表心情
    public  static  String SEND_DYNAMICS = HTTP_SERVICE + "SmartJewelry/Moments/addMoments";
    //排行榜点赞
    public  static  String DIAN_ZANG =  HTTP_SERVICE + "SmartJewelry/StepCounter/addCounterFabulous";
    //取消点赞
    public  static  String QUXIAO_DIAN_ZANG =  HTTP_SERVICE + "SmartJewelry/StepCounter/delCounterFabulous";
    //查看动态
    public  static  String QUERY_SPACE_DYNAMIC = HTTP_SERVICE+ "SmartJewelry/Moments/queryMomets/";
    //动态点赞
    public  static  String DONGTAI_DIANZANG = HTTP_SERVICE + "SmartJewelry/Moments/fabulous/";
    //取消动态点赞
    public  static  String QUXIAO_DONGTAI_DIANZANG = HTTP_SERVICE + "SmartJewelry/Moments/cancelFabulous/";
    //评论动态
    public  static  String PINGLUN_DONGTAI = HTTP_SERVICE + "SmartJewelry/Moments/comment";
    //回复评论
    public  static  String HUIFU_PINGLUN =  HTTP_SERVICE + "SmartJewelry/Moments/comment";


    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;



    public  static  void showImage(Context context,String url, int erro , int loadpic, ImageView imageView){
        Glide.with(context).load(url).asBitmap().fitCenter().placeholder(loadpic).error(erro).dontAnimate().into(imageView);
    }

}
