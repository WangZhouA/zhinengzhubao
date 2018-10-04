package test.com.shoushi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.com.shoushi.R;
import test.com.shoushi.bas.MyApplication;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.pickTime.pickerview.util.PopBirthHelper;
import test.com.shoushi.pickTime.pickerview.util.TimeUtil;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.widget.CustomDatePicker;


public class MapsActivity extends Activity implements LocationSource,OnClickListener,AMapLocationListener {
    public ImageButton header_left;
    public TextView header_title;
    private ImageButton header_right;
    private AMap aMap;
    private MapView mapView;
    MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private UiSettings mUiSettings;//定义一个UiSettings对象

    PopBirthHelper popBirthHelper;
    private  TextView pick_time;

    private String timeDate; //选择的时间



    CustomDatePicker customDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MyApplication.getInstance().addActyToList(this);
        mapView = (MapView) findViewById(R.id.maps);
        header_left = (ImageButton)findViewById(R.id.header_left);
        header_title = (TextView)findViewById(R.id.header_text);
        header_left.setOnClickListener(this);
        header_title.setText("GPS定位");
        header_right= (ImageButton) findViewById(R.id.header_right);
        header_right.setOnClickListener(this);
        header_right.setVisibility(View.VISIBLE);
        header_right.setImageResource(R.mipmap.btn_riqixz);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        myLocationStyle = new MyLocationStyle();
        aMap.setTrafficEnabled(false);// 显示实时交通状况

        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);

        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种

        //自定义点位图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.img_weizhi));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 自定义精度范围的圆形边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//圆圈的颜色,设为透明的时候就可以去掉园区区域了
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        pick_time= (TextView) findViewById(R.id.pick_time);
        pick_time.setText(TimeUtil.getCurrentTimeChinese());

        initDatePicker();

//        popBirthHelper = new PopBirthHelper(this);
//
//        popBirthHelper.setOnClickOkListener(new PopBirthHelper.OnClickOkListener() {
//            @Override
//            public void onClickOk(String birthday) {
//                pick_time.setText(birthday);
//                Log.i("----->2",""+birthday);
//                timeDate=birthday;
//                /**
//                 *  刚进来我就要去取坐标点
//                 * */
//                okHttpToUdapterPoint();
//            }
//        });

        /***
         * 开定时器，每隔5分钟定一次位置
         * */
        Intent intent =new Intent("SHOUSHI_LOCATION");
        sendBroadcast(intent);


    }
    /**
     * 取出后台的数据显示
     * */
    List<LatLng> latLngs = new ArrayList<LatLng>();
    private void okHttpToUdapterPoint() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("startTime", timeDate);
            Log.i("----->1",""+test.com.shoushi.utils.TimeUtil.getCurrentAllTime());
            jsonObject.put("uid",userInfo.getStringInfo("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.QUERY_DAY_POINT)
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
                    try {
                        JSONObject json =new JSONObject(jsons);
                        String result =json.getString("result");
                        if (!result.equals("-1")){

                            Log.i("--->我有数据","我有数据");
                            JSONObject item =json.getJSONObject("result");
                            String coordinate=item.getString("coordinate");
                            if(coordinate.contains(";")){
                                /**
                                 * 取点
                                 * */
                                String [] point =coordinate.split(";");
                                for (int i =0;i<point.length;i++){
                                    String  str= point[i];
                                    String [] strs =str.split(",");
                                    latLngs.add(new LatLng(Double.valueOf(strs[0]),Double.valueOf(strs[1])));

                                }
                                /**
                                 * 吧坐标点画出来
                                 * */

                                PolylineOptions polylineOptions = new PolylineOptions();
                                //设置线的宽度
                                polylineOptions.width(10); //设置线的颜色
                                polylineOptions.color(Color.RED); //设置线是否可见
                                polylineOptions.visible(true);
                                for(int i1 = 0;i1<latLngs.size();i1++){
                                    polylineOptions.add(latLngs.get(i1));
                                    if (latLngs.get(i1)==latLngs.get(0)){
                                           drawMarker(latLngs.get(i1),
                                                R.mipmap.img_dingwei_z);
                                    }
                                }
                                mapView.getMap().addPolyline(polylineOptions);
                            }else{
                                Log.i("--->不包含","");
                            }
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


    private void drawMarker(LatLng a ,int id) {
        marker = aMap.addMarker(markerOptions
                .position(a)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),id)))
                .draggable(true));

        marker.showInfoWindow();// 设置默认显示一个infowinfow

    }


    @Override
    public void activate(OnLocationChangedListener listener) {

        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位


        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        MyApplication.getInstance().removeActyFromList(this);
    }


    private CameraUpdate mUpdata;
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.header_left:

                finish();

                break;
            case R.id.header_right:
//                popBirthHelper.show(header_right);

                customDatePicker.show( splitTimeUtilsXie(pick_time.getText().toString()) );

                break;
        }
    }



    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
//        pick_time.setText(now.split(" ")[0]);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

                timeDate=time.split(" ")[0];
                pick_time.setText(splitTimeUtils(time.split(" ")[0]));
                /**
                 *  刚进来我就要去取坐标点
                 * */
                okHttpToUdapterPoint();

            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动


    }

    private  String  splitTimeUtils(String timeDate){

        String [] DateTime = timeDate.split(" ")[0].split("-");
        String NowTime  =DateTime[0]+"年"+DateTime[1]+"月"+DateTime[2]+"日";
        return  NowTime;
    }
    private  String  splitTimeUtilsXie(String timeDate){
        String []  myBabyTimeDayAarry=  timeDate.split("年");
        Log.i("--->我的宝贝的年限",myBabyTimeDayAarry[0]);

        String [] myBabyTimeDayAarryM= timeDate.split("月");

        String [] myBabyTimeDayAarryMM=  myBabyTimeDayAarryM[0].split("年");

        Log.i("--->我的宝贝的月限",myBabyTimeDayAarryMM[1]);

        String [] myBabyTimeDayAarryR=timeDate.split("日");
        String [] myBabyTimeDayAarryRR= myBabyTimeDayAarryR[0].split("月");
        Log.i("--->我的宝贝的日限",myBabyTimeDayAarryRR[1]);


        return  myBabyTimeDayAarry[0]+"-"+myBabyTimeDayAarryMM[1]+"-"+myBabyTimeDayAarryRR[1];
    }









    Marker marker;
    MarkerOptions markerOptions=new MarkerOptions();
    private void drawMarkers(String a ,String b,int id) {
        marker = aMap.addMarker(markerOptions
                .position(new LatLng(Double.valueOf(a),Double.valueOf(b)))
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),id)))
                .draggable(true));

        marker.showInfoWindow();// 设置默认显示一个infowinfow

    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }



    // 开始轨迹绘画


    private UserInfo userInfo=new UserInfo(this);
    @Override
    protected void onResume() {



        super.onResume();
    }
}
