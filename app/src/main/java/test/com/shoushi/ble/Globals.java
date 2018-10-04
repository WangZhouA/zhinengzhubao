package test.com.shoushi.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.com.shoushi.Entity.User;
import test.com.shoushi.utils.Toastor;

/**
 * Created by 陈姣姣 on 2017/9/8.
 */

public class Globals {
    //弹出提示类
    public static Toastor toastor;
    public static BlueToothConnect toothConnect;//当前的蓝牙连接对象
    public static BluetoothManager mBluetoothManager;
    public static BluetoothAdapter mBluetoothAdapter;
    public static Date oldtime;
    public static List<User.ListBean> dlist = new ArrayList<>();//保存后台绑定设备的集合
    public static User.ListBean choicedevice;//当前连接的对象的实体类

    public static String lastAddress="";//保存上一次连接的地址




    public static String SERVICE_UUID = "00008000-abcd-efaa-bbcc-ddeeff123456";
    public static String WRITE_UUID = "00008001-abcd-efaa-bbcc-ddeeff123456";//写
    public static String Notifi_UUID = "00008003-abcd-efaa-bbcc-ddeeff123456"; //通知

    public  static byte[] KAIQI_FANGDIU = {(byte) 0xaa, (byte)0x0a, 0x01};
    public  static byte[] KAIQI_GUANBI = {(byte) 0xaa, (byte)0x0a, 0x00};
    public  static byte[] KAIQI_DIANHUA_OPEN={(byte) 0xaa, (byte)0x0b,0x01};  //电话打开
    public  static byte[] KAIQI_DIANHUA_CLOSE={(byte) 0xaa, (byte)0x0b,0x00};  //电话关闭

    public  static byte[] KAIQI_QQ_OPEN={(byte) 0xaa, (byte)0x0b,0x02}; //打开QQ


    public  static byte[] KAIQI_WEIXIN_OPEN={(byte) 0xaa, (byte)0x0b,0x03}; //打开微信


    public  static byte[] KAIQI_QQ;
    public static void init(Activity context) {
        Log.i("覃微初始化工具类=====", "");
        toastor = new Toastor(context);
        toothConnect = new BlueToothConnect(context);

    }


}
