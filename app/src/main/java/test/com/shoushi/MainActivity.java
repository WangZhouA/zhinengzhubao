package test.com.shoushi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.com.shoushi.Entity.QuanPaihang;
import test.com.shoushi.activity.DerviceActivity;
import test.com.shoushi.activity.FriendsAvtivity;
import test.com.shoushi.activity.JiBuActivity;
import test.com.shoushi.activity.MapsActivity;
import test.com.shoushi.activity.MessageActivity;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.service.MyToNotificationServiceTo;
import test.com.shoushi.service.StepService;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.step.StepDataDao;
import test.com.shoushi.step.entity.StepEntity;
import test.com.shoushi.step.utils.StepCountCheckUtil;
import test.com.shoushi.step.utils.TimeUtil;
import test.com.shoushi.utils.Util;
import test.com.shoushi.view.CircleImageView;

import static java.lang.Integer.parseInt;

public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener ,View.OnClickListener ,Handler.Callback{
    private DrawerLayout mDrawerLayout;
    private View mLeftMenuFragment;
    @BindView(R.id.header_left) //菜单
            ImageView imageMenu;

    @BindView(R.id.header_text)
    TextView header_text; //title
    @BindView(R.id.LinearLayout_gps)
    LinearLayout LinearLayout_gps;
    @BindView(R.id.LinearLayout_message)
    LinearLayout LinearLayout_message;
    @BindView(R.id.LinearLayout_friends)
    LinearLayout LinearLayout_friends;
    @BindView(R.id.img_add)
    ImageView img_add;
    private Intent intent;

    @BindView(R.id.tv_main_address)
    TextView tv_main_address;

    @BindView(R.id.first_img_jibu)
    ImageView first_img_jibu;

    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;

    @BindView(R.id.mian_name)
    TextView mian_name;

    @BindView(R.id.tv_paihang_num)
    TextView tv_paihang_num;

    UserInfo userInfo = new UserInfo(this);
    @BindView(R.id.tv_number_n)
    TextView tv_number_n;

    @BindView(R.id.min_img)
    ImageView min_img;
    @BindView(R.id.min_sum)
    TextView min_sum;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case  0:
                    //名字
                    if (name.contains("null")) {

                        if (phone.contains("null")) {
                            mian_name.setText("");
                        } else {
                            mian_name.setText(phone);
                        }

                    } else {
                        mian_name.setText(name);
                        userInfo.setUserInfo("name", name);
                    }

                    // 电话

                    if (phone.contains("null")) {

                    } else {

                        userInfo.setUserInfo("phone", phone);
                    }

                    //头像
                    if (headimg!=null) {
                        StringUtils.showImage(MainActivity.this, StringUtils.HTTP_SERVICE+headimg, R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, circleImageView);
                        userInfo.setUserInfo("img",StringUtils.HTTP_SERVICE+headimg);
                    } else {
                        circleImageView.setImageResource(R.mipmap.img_yonghumr);
                    }

                    if (num.contains("null")) {
                        tv_paihang_num.setText("暂无排名");
                    } else {
                        tv_paihang_num.setText(num);
                    }

                    //呗赞总数

                    if (sum.contains("null")) {
                        min_sum.setText("0");
                    } else {
                        min_sum.setText(sum);
                    }


                    //是否被点赞了
                    if (isf == null) {
                        min_img.setImageResource(R.mipmap.btn_z_wdian);
                    } else {
                        min_img.setImageResource(R.mipmap.btn_z_ydian);
                    }

                    break;

                case 1:
                    //名字
                    if (name.contains("null")) {

                        if (phone.contains("null")) {
                            mian_name.setText("");
                        } else {
                            mian_name.setText(phone);
                        }

                    } else {
                        mian_name.setText(name);
                        userInfo.setUserInfo("name", name);
                    }

                    if (phone.contains("null")) {

                    } else {

                        userInfo.setUserInfo("phone", phone);
                    }

                    //头像
                    if (headimg.contains("null")) {
                        circleImageView.setImageResource(R.mipmap.img_yonghumr);
                    } else {
                        userInfo.setUserInfo("img", StringUtils.HTTP_SERVICE +headimg);
                        StringUtils.showImage(MainActivity.this, StringUtils.HTTP_SERVICE + headimg, R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, circleImageView);
                    }

                    tv_paihang_num.setText("暂无排名");
                    min_sum.setText("0");
                    min_img.setImageResource(R.mipmap.btn_z_wdian);

                    break;

                case 2:



                    break;
            }
        }
    };

    //************  获取手机传感器的数据  ***************************************

    private String curSelDate;
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<StepEntity> stepEntityList = new ArrayList<>();
    private StepDataDao stepDataDao;

    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Messenger messenger;

    String totalStepsTv="1";  //全局步数
    String totalKmTv;  //总公里数
    String stepsTimeTv; //时间

    private int Timeten = 600000;
    /**
     *   定时器 20秒上传一次
     * */
    int xuStep=0;
    Runnable run =new Runnable() {
        @Override
        public void run() {

            okHttpTodayDate();
            handler.postDelayed(this,Timeten);

        }
    };



    @Override
    protected void init() {


        //侧滑
        mDrawerLayout = (DrawerLayout) findViewById(R.id.unbind_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        mDrawerLayout.setDrawerListener(this);
        Util.setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);
        mLeftMenuFragment = findViewById(R.id.left_menu);


        //view
        imageMenu.setImageResource(R.mipmap.btn_cebian);
        header_text.setText(R.string.left_zhubao);

        /**
         * 一进来就上传一次数据，然后每隔十分钟上传一次数据
         * */
        handler.postDelayed(run,1000);


        /**
         * 开启服务
         * */
        Intent intent =new Intent(this, MyToNotificationServiceTo.class);
        startService(intent);

        //开始定位
        startAmap();

        curSelDate = TimeUtil.getCurrentDate();
        /**
         * 这里判断当前设备是否支持计步
         */
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            getRecordList();
            setDatas();
            setupService();
        } else {
            totalStepsTv="0";
        }

    }



    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }


    //点击侧滑的
    @OnClick({R.id.header_left, R.id.LinearLayout_gps, R.id.LinearLayout_message, R.id.LinearLayout_friends, R.id.img_add, R.id.first_img_jibu})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:
                mDrawerLayout.openDrawer(mLeftMenuFragment);
                break;
            case R.id.LinearLayout_gps:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.LinearLayout_message:
                intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.LinearLayout_friends:
                intent = new Intent(this, FriendsAvtivity.class);
                startActivity(intent);
                break;
            case R.id.img_add:
                intent = new Intent(this, DerviceActivity.class);
                startActivity(intent);
                break;
            case R.id.first_img_jibu:
                intent = new Intent(this, JiBuActivity.class);
                startActivity(intent);
                break;
        }
    }


    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mapLocationClient = null;


    @Override
    protected void onResume() {
        okHttpQuery();
        super.onResume();
    }

    /**
     * 开始定位
     */
    private void startAmap() {
        // 初始化定位
        mapLocationClient = new AMapLocationClient(MainActivity.this);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        mapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    aMapLocation.getLatitude();
                    aMapLocation.getLongitude();

                    Log.i("--->经纬度",""+ aMapLocation.getLatitude()+ aMapLocation.getLongitude());


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
                    tv_main_address.setText(dq);

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
        mLocationOption.setInterval(2000);
        // 获取一次定位结果：
        // 该方法默认为false。
        mLocationOption.setOnceLocation(true);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mapLocationClient.startLocation();

    }

    /**
     * 下面四部分为侧滑重写方法
     * <p>
     * <p>
     * 部分一
     */
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

        View mContent = mDrawerLayout.getChildAt(0);
        float scale = 1 - slideOffset;
        if (drawerView.getTag().equals("LEFT")) {

            float leftScale = 1 - 0.3f * scale;

            ViewHelper.setScaleX(drawerView, leftScale);
            ViewHelper.setScaleY(drawerView, leftScale);
            ViewHelper.setTranslationX(mContent, drawerView.getMeasuredWidth()
                    * (1 - scale));
            ViewHelper.setPivotX(mContent, 0);
        } else {
            ViewHelper.setTranslationX(mContent, -drawerView.getMeasuredWidth()
                    * slideOffset);
            ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
        }
        ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
        mContent.invalidate();


    }

    /**
     * 部分二
     */
    @Override
    public void onDrawerOpened(View drawerView) {
        Log.e("--->2", "2");


        Intent intent = new Intent("UPDATE_TOUXIANG");
        sendBroadcast(intent);


    }

    /**
     * 部分三
     */
    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
        Log.e("--->3", "3");
    }

    /**
     * 部分四
     */
    @Override
    public void onDrawerStateChanged(int newState) {
    }

    String name;
    String headimg;
    String phone;
    String number;
    String num;
    Object isf;
    String sum;


    QuanPaihang quanPaihang;
    //查询用户信息
    private void okHttpQuery() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.USER_PAIHANGBANG+userInfo.getStringInfo("id"))
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
                    Log.i("--->jsons",jsons);
                    Gson gson = new Gson();
                    quanPaihang = gson.fromJson(jsons, QuanPaihang.class);

                    if (quanPaihang.getData2()!=null) {
                        name = quanPaihang.getData2().getUname();
                        Log.i("--->name", name);
                        headimg = quanPaihang.getData2().getHeadimg();
                        phone = quanPaihang.getData2().getPhone();
                        number = quanPaihang.getData2().getNumber();
                        num = quanPaihang.getData2().getNum();
                        isf = quanPaihang.getData2().getIsf();
                        sum = quanPaihang.getData2().getSum();

                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }else {

                        okHttpQueryUserMsg();

                    }

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });

    }

    private void okHttpQueryUserMsg() {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("phone", userInfo.getStringInfo("userName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.QUERY_USER_MSG)
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
                    String  data_result=response.body().string();
                    Log.i("--->", "" +data_result);
                    try {
                        JSONObject object =new JSONObject(data_result);
                        String   result= object.getString("result");
                        if (result.contains("1")){
                            JSONObject itemObj =object.getJSONObject("data");
                            name=itemObj.getString("name");
                            headimg=itemObj.getString("headimg");
                            phone=itemObj.getString("phone");
                            Message msg =handler.obtainMessage();
                            msg.what=1;
                            handler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) this.unbindService(conn);
    }


//========================(计步)================================

    /**
     * 获取全部运动历史纪录
     */
    private void getRecordList() {
        //获取数据库
        stepDataDao = new StepDataDao(this);
        stepEntityList.clear();
        stepEntityList.addAll(stepDataDao.getAllDatas());
        if (stepEntityList.size() >= 7) {
            // TODO: 2017/3/27 在这里获取历史记录条数，当条数达到7条或以上时，就开始删除第七天之前的数据,暂未实现


        }
    }

    /**
     * 设置记录数据
     *
     */
    String kmTimeTv ;
    private void setDatas() {
        StepEntity stepEntity = stepDataDao.getCurDataByDate(curSelDate);

        if (stepEntity != null) {
            int steps = parseInt(stepEntity.getSteps());

            //获取全局的步数
            totalStepsTv=String.valueOf(steps);
            //计算总公里数
            totalKmTv=countTotalKM(steps);
        } else {
            //获取全局的步数
            totalStepsTv="0";
            //计算总公里数
            totalKmTv="0";
        }

        //设置时间
        String time = TimeUtil.getWeekStr(curSelDate);
        kmTimeTv=time;
        stepsTimeTv=time;
    }
    /**
     * 简易计算公里数，假设一步大约有0.7米
     *
     * @param steps 用户当前步数
     * @return
     */
    private String countTotalKM(int steps) {
        double totalMeters = steps * 0.7;
        //保留两位有效数字
        return df.format(totalMeters / 1000);
    }
    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        messenger = new Messenger(service);
                        Message msg = Message.obtain(null, StringUtils.MSG_FROM_CLIENT);
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            //这里用来获取到Service发来的数据
            case StringUtils.MSG_FROM_SERVER:
                //如果是今天则更新数据
                if (curSelDate.equals(TimeUtil.getCurrentDate())) {
                    //记录运动步数
                    int steps = msg.getData().getInt("steps");
                    //设置的步数
                    totalStepsTv=String.valueOf(steps);
                    // TODO: 2017/11/1     这里判断是否是第一次进来上传数据。
                    tv_number_n.setText(totalStepsTv);
//                    if (count==0) {
//                        count++;
//                        userInfo.setUserInfo("oldDate",totalStepsTv);
//                        ToUpdateStep(totalStepsTv);
//                        /**
//                         *  第一次上传数据
//                         * */
//
//                        Toast.makeText(this, "" + count, Toast.LENGTH_SHORT).show();
//                    }else {
//
//                    }
                }
                break;
        }
        return false;
    }


    /**
     * 上传现在的步数
     * */

    private void ToUpdateStep(String  totalStepsTv){

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("number",totalStepsTv);
            jsonObject.put("sstarttime", test.com.shoushi.utils.TimeUtil.getCurrentTime());
            jsonObject.put("sendtime",test.com.shoushi.utils.TimeUtil.getCurrentTime());
            jsonObject.put("uid", userInfo.getStringInfo("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.ADD_DAY_STEP)
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
                    String  data_result=response.body().string();
                    Log.i("--->", "" +data_result);
                    try {
                        JSONObject object =new JSONObject(data_result);
                        String   result= object.getString("result");
                        if (result.contains("1")){

                            Message msg =handler.obtainMessage();
                            msg.what=2;
                            handler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });

    }


    /**
     * 觉得用循环来算出今天的步数总和
     * */
    private  List<Integer>lists;
    private int  ToCount=0;
    private  void   okHttpTodayDate(){
        lists=new ArrayList<>();
        //创建网络处理的对象
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(StringUtils.DAY_HISTOR_READ+userInfo.getStringInfo("id")+"?date="+ test.com.shoushi.utils.TimeUtil.getCurrentTime()).build();
        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e("get--->", "结果  " + e.toString());

            }
            @Override
            public void onResponse(Call call, Response response   ) throws IOException {

//                Log.e("get--->", "结果  " + response.body().string());
                String result = response.body().string();
                try {
                    JSONObject json =new JSONObject(result);
                    if (json.getInt("result")==1) {
                        JSONArray jsArr = json.getJSONArray("data");
                        lists.clear();
                        if (jsArr.length() > 0) {
                            for (int i = 0; i < jsArr.length(); i++) {
                                JSONObject item = jsArr.getJSONObject(i);
                                int number = item.getInt("number");
                                lists.add(number);
                            }
                            Log.i("--->lists.toString", lists.toString());
                            ToCount=0;
                            for (int i: lists) {
                                ToCount+=i;
                            }

                        }

                        Log.i("----->ToCount",""+ToCount);
                        Log.i("---->XIN","XIN"+Integer.parseInt(tv_number_n.getText().toString()));
                        Log.i("---->OLD","XIN"+ToCount);

                        if ( Integer.parseInt(totalStepsTv)!=0 ) {
                            xuStep = Integer.parseInt(totalStepsTv) - ToCount;
                            Log.i("---->222需要上传的","差"+xuStep);
                            if (xuStep==0 && xuStep<1){
                                Log.i("---->0步","没动");
                            }   else {
                                ToUpdateStep(String.valueOf(xuStep));
                                ToCount = 0;
                            }

                        }else {
                            xuStep=0;
                            Log.i("---->111需要上传的","差"+ToCount);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

