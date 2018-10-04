package test.com.shoushi.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.com.shoushi.Iinface.OnDismissListener;
import test.com.shoushi.R;
import test.com.shoushi.ble.Globals;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.utils.AMapUtils;
import test.com.shoushi.utils.LngLat;
import test.com.shoushi.utils.TimeUtil;
import test.com.shoushi.view.MyDialog;

/**
 * Created by 陈姣姣 on 2017/9/22.
 */

public class MyToNotificationServiceTo extends NotificationListenerService  {
    public static List<String> list = new ArrayList<String>();
    String DataFactory = ""; //全部大数据
    private String BM = ""; //包名
    private String neirong = ""; //内容

    String qqFlag="1" ; // QQ开关  1是开  0是关
    private String QQXINGXI;
    String weixinFlag="1" ; // QQ开关  1是开  0是关
    private String WEIXINXINGXI;

    private String dianhua;
    private  UserInfo userInfo;


    private List<String>lists=new ArrayList<>();


    /**
     * 为了开启事实定位
     * */
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    private Handler handler =new Handler();

    Runnable runnable  = new Runnable() {
        @Override
        public void run() {
            startAmap();
            handler.postDelayed(this,300000);
        }
    };




    private  OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("qianyi", "Srevice is open" + "-----");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {

        Log.e("--->service","isOpen");
        userInfo =new UserInfo(this);

        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction("SHOUSHI_LOCATION");
        intentFilter.addAction("FUWU");
        intentFilter.addAction("DUANKAI");
        intentFilter.addAction("ACTION_GATT_DISCONNECTED");
        registerReceiver(receiverW,intentFilter);

        super.onCreate();

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // 没有获取具体的类容(大数据包)
        DataFactory = sbn.toString();
        // 获取具体的包名
        BM = sbn.getPackageName();
        Log.e("--->bao", "===========================" + BM);
        // 知道了具体类容
        neirong = sbn.getNotification().tickerText.toString();//从通知获取消息推送的信息
        Log.e("--->neirong", "===========================" + neirong);


        ///如果是QQ消息的话
        if (BM.contains("com.tencent.mobileqq")) {
            if (qqFlag.equals("1")) {
                QQXINGXI = neirong;
                Log.e("--->QQ消息", "======================" + QQXINGXI);

                if(!TextUtils.isEmpty(QQXINGXI)){
                    if (userInfo.getStringInfo("QQ")!=null && userInfo.getStringInfo("QQ").contains("0") ) {
                        Globals.toothConnect.writeCharacteristic(Globals.SERVICE_UUID, Globals.WRITE_UUID, Globals.KAIQI_QQ_OPEN);
                    }
                }

                String gg = convertStringToUTF8(QQXINGXI);

                Log.e("--->这条消息", "============" + gg);
                int lennew = gg.replace(" ", "").length() % 2 == 0 ? gg
                        .replace(" ", "").length() / 2 : gg.replace(" ", "")
                        .length() / 2 + 1;
                String qlennew = makeup(Integer.toHexString(lennew));
                Log.e("--->这条消息的总长度", "============" + qlennew);
                // get(gg);
                getStr(gg);
                for (int i = 0; i < list.size(); i++) {
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 这是第几条消息
                    int q = i + 1;
                    String q1 = makeup(String.valueOf(q));
                    // 这是第几帧消息
                    int f = i + 1;
                    String frames = singlezero(String.valueOf(f));
                    String ggnew = list.get(i);
                    int qqxxlength = ggnew.replace(" ", "").length() % 2 == 0 ? ggnew
                            .replace(" ", "").length() / 2 : ggnew.replace(" ",
                            "").length() / 2 + 1;
                    String newqqxxlength1 = Integer.toHexString(qqxxlength);
                    String newqqxxlength = newqqxxlength1.replace(" ", "");
                    int string_len = newqqxxlength.length();
                    int len = string_len / 2;
                    if (string_len < 2) {
                        newqqxxlength = "0" + newqqxxlength;
                        Log.e("--->当前消息长度", "==============" + newqqxxlength);

                    }
                    Log.e("--->当前消息", "================" + ggnew);


                    // QQ信息指令
                    // 当前消息长度+命令符+第几帧数据+第几条数据+消息总长度
//                    String qqorder = newqqxxlength + "0901" + frames + "0104"
//                            + q1 + qlennew;

                }
            } else {
                Log.e("xxx", "=====QQ推送处于关闭状态=====");
            }

        } else if (BM.contains("com.tencent.mm")) {
            // 获取微信包
            if (weixinFlag.equals("1")) {
                WEIXINXINGXI = neirong;
                Log.e("--->", "--微信消息--" + WEIXINXINGXI);

                if(!TextUtils.isEmpty(WEIXINXINGXI)){

                    if (userInfo.getStringInfo("WX")!=null && userInfo.getStringInfo("WX").contains("0") ) {
                        Globals.toothConnect.writeCharacteristic(Globals.SERVICE_UUID, Globals.WRITE_UUID, Globals.KAIQI_QQ_OPEN);
                    }
                }

                String gg = convertStringToUTF8(WEIXINXINGXI);
                // String gg = convertStringToUTF8("不错哦下班了");
                Log.e("--->", "完整数据" + gg);
                int lennew = gg.replace(" ", "").length() % 2 == 0 ? gg
                        .replace(" ", "").length() / 2 : gg.replace(" ", "")
                        .length() / 2 + 1;
                String qlennew = makeup(Integer.toHexString(lennew));
                Log.e("--->", "=这条消息的总长度=" + qlennew);

                // get(gg);
                getStr(gg);
                for (int i = 0; i < list.size(); i++) {
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 这是第几条消息
                    int q = i + 1;
                    String q1 = makeup(String.valueOf(q));
                    // 这是第几帧消息
                    int f = i + 1;
                    String frames = singlezero(String.valueOf(f));
                    String ggnew = list.get(i);
                    int qqxxlength = ggnew.replace(" ", "").length() % 2 == 0 ? ggnew
                            .replace(" ", "").length() / 2 : ggnew.replace(" ",
                            "").length() / 2 + 1;
                    String newqqxxlength1 = Integer.toHexString(qqxxlength);
                    String newqqxxlength = newqqxxlength1.replace(" ", "");
                    int string_len = newqqxxlength.length();
                    int len = string_len / 2;
                    if (string_len < 2) {
                        newqqxxlength = "0" + newqqxxlength;
                        Log.e("", "--当前消息长度---" + newqqxxlength);

                    }
                    Log.e("", "----当前消息----" + ggnew);
                    // QQ信息指令
                    // 当前消息长度+命令符+第几帧数据+第几条数据+消息总长度
//                    String qqorder = newqqxxlength + "0901" + frames + "0103"
//                            + q1 + qlennew;

                }
            } else {
                Log.e("xxx", "=====微信推送处于关闭状态=====");
            }
        }
    }


    public static String singlezero(String str) {
        String ss = str.replace(" ", "");
        int string_len = ss.length();
        if (string_len < 2) {
            str = "0" + str;
        }
        return str;
    }



    public static List<String> getStr(String gg) {
        // List<String>list =new ArrayList<String>();
        list = new ArrayList<String>();
        int n = gg.length() % 30 == 0 ? gg.length() / 30
                : (gg.length() / 30 + 1);
        for (int i = 0; i < n; i++) {
            int len;
            if (i == n - 1) {
                len = gg.length() % 30;
                list.add(gg.substring(30 * i, gg.length()));

            } else {
                len = 10;
                list.add(gg.substring(30 * i, 30 * (i + 1)));
            }
        }
        return list;

    }

    /**
     * 加00
     *
     * @return
     */
    public static String makeup(String str) {
        String ss = str.replace(" ", "");
        int string_len = ss.length();
        if (string_len < 2) {
            str = "000" + str;
        } else if (string_len < 3) {
            str = "00" + str;
        } else if (string_len < 4) {
            str = "0" + str;
        }

        return str;
    }



    /**
     * 转成UTF-8的格式
     * */
    public static String convertStringToUTF8(String s) {
        // Log.i(TAG, "convertStringToUTF8 s:" + s);
        if (s == null) {
            return s;
        }
        String temp = null;
        try {
            temp = new String(s.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return str2HexStr(temp);
    }
    /**
     * 转16进制字符串
     *
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }




    String point;
    private void startAmap() {
        // 初始化定位
        mlocationClient = new AMapLocationClient(MyToNotificationServiceTo.this);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    /**
                     * 拿到当前的经纬度
                     * */
                    point = aMapLocation.getLatitude()+","+ aMapLocation.getLongitude();

                    /**
                     * 把坐标点存进去
                     * */
                    if (lists.size()==0) {
                        lists.add(point);
                        okHttpUpdatePoint();
                    }else {
                        String [] stry =lists.get(lists.size()-1).split(",");
                        Log.i("--->[] stry", Arrays.toString(stry));
                        LngLat end = new LngLat(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        LngLat start = new LngLat(Double.valueOf(stry[0]),Double.valueOf(stry[1]));
                        if ((int)AMapUtils.calculateLineDistance(start,end)>=100){
                            lists.add(point);
                            okHttpUpdatePoint();
                        }else {
                            Log.i("--->当前距离","不够十米不存");
                        }

                    }
                    Log.i("--->经纬度",point);
                    aMapLocation.getProvince();// 省信息
                    Log.i("--->省", aMapLocation.getProvince());
                    aMapLocation.getCity();// 城市信息
                    Log.i("--->市", aMapLocation.getCity());
                    Log.i("--->区", aMapLocation.getDistrict());
                    // 获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getAddress()
                    );

                    String dq = buffer.toString();
                    Log.i("---dq", dq);

                } else {
                    // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        });
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(5000);
        // 获取一次定位结果：
        // 该方法默认为false。
        mLocationOption.setOnceLocation(true);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 给定位客户端对象设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mlocationClient.startLocation();


        for (int  i=0;i<lists.size();i++){
            Log.i("--->lists",""+lists.get(i));

        }

        UserInfo userInfo =new UserInfo(MyToNotificationServiceTo.this);
        userInfo.setUserInfo("list",lists.toString());

    }

    MyDialog builder;
    private BroadcastReceiver receiverW=new  BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /**
             * 隔几分钟定位一次
             * */
            if (action.contains("SHOUSHI_LOCATION")){
                handler.postDelayed(runnable,1000);
            }  else if (action.equals("FUWU")) { //蓝牙连接，发现服务了，设置通知
                Log.e("蓝牙锁发现服务了的数据", intent.getStringExtra("address"));
                String address = intent.getStringExtra("address");
//                Globals.toothConnect.setCharacteristicNotification(Globals.SERVICE_UUID, Globals.WRITE_UUID, true);
                Intent intent1 =new Intent("QUXIAO");
                sendBroadcast(intent1);
                Globals.toastor.showToast("连接成功");
//
            } else if (action.equals("DUANKAI")) {

                if ( userInfo.getBooleanInfo("boo")==true) {

                    Globals.toothConnect.writeCharacteristic(Globals.SERVICE_UUID, Globals.WRITE_UUID, Globals.KAIQI_QQ_OPEN);
                }

            }else if (action.equals("ACTION_GATT_DISCONNECTED")) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.qiangzhi_xiaxian_layout, null);
                if (builder == null) {
                    builder = new MyDialog(getApplicationContext(), 0, 0, view, R.style.MyDialog);
                    builder.setCancelable(false);
                    Button btn_no_xian_ss = (Button) view.findViewById(R.id.btn_no_xian_ss);
                    Button btn_yes_xiaxian_ss = (Button) view.findViewById(R.id.btn_yes_xiaxian_ss);
                    TextView for_tv_titles = (TextView) view.findViewById(R.id.for_tv_titles);
                    TextView text_for_tv = (TextView) view.findViewById(R.id.text_for_tv);

                    for_tv_titles.setText("设备已断开");
                    text_for_tv.setText("是否重新连接");
                    //取消按钮
                    btn_no_xian_ss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });


                    //确认按钮
                    btn_yes_xiaxian_ss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!TextUtils.isEmpty(userInfo.getStringInfo("mac")) && userInfo.getStringInfo("mac") != null) {
                                Log.i("--->lastAddress", userInfo.getStringInfo("mac"));
                                Globals.toothConnect.ConnectTODevice(userInfo.getStringInfo("mac"));
                            }
                            builder.dismiss();
                        }
                    });

                    builder.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                    builder.show();
                }
            }else {
                Toast.makeText(context, "已经存在", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onDestroy() {

        unregisterReceiver(receiverW);
        super.onDestroy();


    }


    public  class MyPhoneState extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) ;

            //获取电话号码
            String number = intent.getStringExtra("incoming_number");
            Log.i("test", "有电话进来了" + number);
//        Toast.makeText(context, "电话来了"+number, Toast.LENGTH_SHORT).show();


            //获取电话状态
            //电话管理者
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int state = tm.getCallState();
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i("test", "来电话了");

                   if (userInfo.getStringInfo("DH")!=null && userInfo.getStringInfo("DH").contains("0") ) {
                       Globals.toothConnect.writeCharacteristic(Globals.SERVICE_UUID, Globals.WRITE_UUID, Globals.KAIQI_QQ_OPEN);
                   }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("test", "通话中");
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i("test", "挂断了");
                    break;
            }
        }

    }



    private void okHttpUpdatePoint(){

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("coordinate", point );
            jsonObject.put("startTime", TimeUtil.getCurrentTime());
            jsonObject.put("uid",userInfo.getStringInfo("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.ADD_DAY_POINT)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->", "" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //用于获取json的字符串
                    String jsons =response.body().string();
                    Log.i("--->上传坐标点",jsons);
                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });


    }






}
