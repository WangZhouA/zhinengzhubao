package test.com.shoushi.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import test.com.shoushi.Entity.DayHistor;
import test.com.shoushi.Entity.Histor;
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.photo.LogUtils;
import test.com.shoushi.pickTime.pickerview.util.PopBirthHelper;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.widget.CustomDatePicker;
import test.com.shoushi.water.AreaChart03View;


/**
 * Created by 陈姣姣 on 2017/9/16.
 */

public class LiShiJiBuActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.histor_tv)
    TextView histor_tv;
    @BindView(R.id.histor_day)
    TextView histor_day;
    @BindView(R.id.histor_week)
    TextView histor_week;
    @BindView(R.id.histor_mouth)
    TextView histor_mouth;

    @BindView(R.id.header_text)
    TextView header_text;
    @BindView(R.id.header_left)
    ImageButton header_left;
    @BindView(R.id.header_right)
    ImageButton header_right;

    PopBirthHelper popBirthHelper;

    @BindView(R.id.histor_linechart)
    AreaChart03View histor_linechart;

    List<TextView> tvList = new ArrayList<>();
    private UserInfo userInfo=new UserInfo(this);

    @BindView(R.id.tv_max_number)
    TextView    tv_max_number;

    @BindView(R.id.tv_text)
    TextView tv_text;






    List<Histor.RowsBean> listshistor =new ArrayList<>();
    List<Histor.RowsBean> listshistor_Mount =new ArrayList<>();

    private int dayDate ;
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    setWeek(dayRespon);
                    tv_text.setText("本周总共行走");
                    break;
                case 1:
                    if (dayDate==0) {
                        tv_max_number.setText("0");
                        tv_text.setText("本日总共行走");
                    }else {
                        tv_max_number.setText(""+dayDate);
                        tv_text.setText("本日总共行走");
                    }
                    break;
                case 2:
                    setMouth(mount_histor);
                    tv_text.setText("本月总共行走");
                    break;
                case  4:
                    tv_max_number.setText("0");
                    tv_text.setText("本日总共行走");

                    break;

            }
        }
    };

    private void setMouth(Histor mount_histor) {
        List<String> list = new ArrayList<>();
        List<Double> dlist = new ArrayList<>();

        for (int i = 1; i < 32; i++) {
            list.add(i + "");
            dlist.add(0d);
        }
        for (int i = 0; i < mount_histor.getRows().size(); i++) {
            Histor.RowsBean  hDayBean = mount_histor.getRows().get(i);
            LogUtils.e(hDayBean.toString());

            String d = String.valueOf(hDayBean.getNumber()/100);
            for (int j = 0; j < list.size(); j++) {
                if (Integer.parseInt(toSqilt(hDayBean.getTime())) == j) {
                    LogUtils.e(j + "=====" + dlist.get(j));
                    dlist.set(j - 1, Double.valueOf(d));
                }
            }
        }
        histor_linechart.refreshChart(AreaChart03View.TYPE_MOUTH, list, dlist, getMaxInListMount(dlist), 0, getStepInListMount(dlist));
        tv_text.setText("本日总共行走");
        tv_max_number.setText(""+mount_histor.getSum());
    }


    /**
     * 截取字符串
     *
     * */
//                       date ="2017-9-12"
    private String toSqilt(String date){
        String [] str =date.split("-");
        String  time =str[2];
        return  time;
    }


    Histor dayRespon;// 每月
    DayHistor dayHistor; //每日
    Histor mount_histor; //每月


    private  String time;
    SimpleDateFormat sdf;  //用于时间格式的
    Date dateWeek; //时间




    CustomDatePicker customDatePicker;
    String timeDate;

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
//        histor_tv.setText(now.split(" ")[0]);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

                timeDate=time.split(" ")[0];
                histor_tv.setText(splitTimeUtils(time.split(" ")[0]));
                /**
                 *  每次点击我就去刷新变成月的
                 * */
                selectView(2);
                getMouthinfo();

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


    @Override
    protected int getContentView() {
        return R.layout.activity_lishi_jibu;
    }

    @Override
    protected void init() {
        histor_tv.setText(test.com.shoushi.pickTime.pickerview.util.TimeUtil.getCurrentTimeChinese());
        header_right.setImageResource(R.mipmap.btn_riqixz);
        header_right.setVisibility(View.VISIBLE);
        popBirthHelper = new PopBirthHelper(this);
        header_text.setText("历史计步");

        initDatePicker();

        popBirthHelper.setOnClickOkListener(new PopBirthHelper.OnClickOkListener() {
            @Override
            public void onClickOk(String birthday) {
                histor_tv.setText(birthday);
                time=birthday;

            }
        });

        tvList.add(histor_day);
        tvList.add(histor_week);
        tvList.add(histor_mouth);
        setFirst();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    private  void  setFirst(){
        selectView(2);
        getMouthinfo();
        tv_text.setText("本月总共行走");
    }



    @OnClick({R.id.header_left, R.id.header_right, R.id.histor_day, R.id.histor_week, R.id.histor_mouth})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:
                finish();
                break;
            case R.id.header_right:
//                popBirthHelper.show(header_right);

                customDatePicker.show( splitTimeUtilsXie(histor_tv.getText().toString()) );

                break;
            case R.id.histor_day:
                selectView(0);
                getDataDay();
                break;
            case R.id.histor_week:
                /**
                 * 第一步我先去  用当前时间    拿到了这一周的时间
                 *
                 * */
                selectView(1);
                getWeekinfo();
                break;
            case R.id.histor_mouth:
                selectView(2);
                getMouthinfo();



                break;
        }
    }


    //用于获取当月的最后一天的日期
    public static int daysCount(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 0);
        return cal.get(Calendar.DATE);
    }



    //查询每个月的步数
    private void getMouthinfo() {

       String s = splitTimeUtilsXie(histor_tv.getText().toString());

       // "2017-9-11"

        String [] str =s.split("-");

        // [2017,9,11]

        /**
         *   当月第一天  用  firstA  表示   最后一天用  endA   表示
         *
         * */

        Log.e("----->starttime",str[0]+"-"+str[1]+"-1");
        Log.e("----->endtime",str[0]+"-"+str[1]+"-"+daysCount(Integer.parseInt(str[0]),Integer.parseInt(str[1])));


        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("starttime",str[0]+"-"+str[1]+"-1");
            jsonObject.put("endtime",str[0]+"-"+str[1]+"-"+daysCount(Integer.parseInt(str[0]),Integer.parseInt(str[1])));
            jsonObject.put("uid",userInfo.getStringInfo("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.HISTOR_READ)
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
                    String jsons =response.body().string();
                    Log.i("--->jsons",jsons);
                    Gson gson = new Gson();
                    mount_histor = gson.fromJson(jsons, Histor.class);
                    listshistor_Mount = mount_histor.getRows();
                    int  resuly = mount_histor.getResult();
                    Log.i("--->resuly",""+resuly);
                    if (resuly==1){

                        Message msg =new Message();
                        msg.what=2;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }


    // 查询每周的步数
    private void getWeekinfo() {

        List<String>listsTotime=new ArrayList<>();
        try {
            dateWeek = sdf.parse(splitTimeUtilsXie(histor_tv.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i=0;i<dateToWeekA(dateWeek).size();i++) {
            listsTotime.add(sdf.format(dateToWeekA(dateWeek).get(i)));
        }
        Log.e("----->选择的是这周",  listsTotime.toString());
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("starttime",listsTotime.get(1));
            jsonObject.put("endtime",listsTotime.get(listsTotime.size()-1));
            jsonObject.put("uid",userInfo.getStringInfo("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.HISTOR_READ)
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
                    String jsons =response.body().string();
                    Log.i("--->jsons",jsons);
                    Gson gson = new Gson();
                    dayRespon = gson.fromJson(jsons, Histor.class);
                    listshistor = dayRespon.getRows();
                    int resuly = dayRespon.getResult();
                    Log.i("--->resuly",""+resuly);
                    if (resuly==1){
                        Message msg =new Message();
                        msg.what=0;
                        handler.sendMessage(msg);
                    }

                }
            }
        });
    }

    private void setWeek(Histor response) {

        List<String> list = new ArrayList<>();
        List<Double> dlist = new ArrayList<>();

        list.add("日");
        list.add("一");
        list.add("二");
        list.add("三");
        list.add("四");
        list.add("五");
        list.add("六");
        dlist.add(0, 0d);
        dlist.add(1, 0d);
        dlist.add(2, 0d);
        dlist.add(3, 0d);
        dlist.add(4, 0d);
        dlist.add(5, 0d);
        dlist.add(6, 0d);

        for (int i = 0; i < response.getRows().size(); i++) {
            Histor.RowsBean hDayBean = response.getRows().get(i);
            LogUtils.e(hDayBean.toString());
            String d = String.valueOf(hDayBean.getNumber()/100);

            //日期对应
            for (int j = 0; j < list.size(); j++) {
                Log.i("--->日期",dateToWeek(hDayBean.getTime()));
                if (Integer.parseInt(dateToWeek(hDayBean.getTime()))==(j)) {
                    LogUtils.e(j + "=====" + dlist.get(j));
                    dlist.set(j, Double.valueOf(d));
                }
            }
        }

        histor_linechart.refreshChart(AreaChart03View.TYPE_WEEK, list, dlist, getMaxInList(dlist), 0,  getStepInList(dlist));
        tv_max_number.setText(""+response.getSum());
    }
    /**
     * 查询当天的步数
     * */

    private void getDataDay() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("starttime",splitTimeUtilsXie(histor_tv.getText().toString()));
            jsonObject.put("endtime",histor_tv.getText().toString());
            jsonObject.put("uid",userInfo.getStringInfo("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.HISTOR_READ)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->IOException",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response   ) throws IOException {

                String jsons =response.body().string();
                Log.i("--->jsons",jsons);
                Gson gson = new Gson();
                dayHistor = gson.fromJson(jsons, DayHistor.class);
                int resuly =dayHistor.getResult();

                if (resuly==1){
                    if (dayHistor.getRows().size()>0) {
                        dayDate = dayHistor.getRows().get(0).getNumber();
                        Log.i("--->dayDate",""+ dayDate);
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }else {
                        /**
                         * 没有数据数据为0
                         * */
                        Message msg = handler.obtainMessage();
                        msg.what = 4;
                        handler.sendMessage(msg);
                    }
                }
            }
        });

    }



    void selectView(int position) {
        for (int i = 0; i < tvList.size(); i++) {
            if (position == i) {
                tvList.get(i).setBackgroundResource(R.mipmap.btn_riqi_xuan);
                tvList.get(i).setTextColor(0xffffffff);
            } else {
                tvList.get(i).setBackgroundResource(R.mipmap.btn_riqi_wxuan);
                tvList.get(i).setTextColor(Color.GRAY);
            }
        }
    }

    //获取最大刻度
    int getMaxInList(List<Double> b) {
        double d = 2000;
//        for (int i = 0; i < b.size(); i++) {
//            if (b.get(i) > d) {
//                d = b.get(i);
//            }
//        }
//        return ((int) d / 10) * 10 ;

        return (int)d;
    }
    //获取最大刻度
    int getMaxInListMount(List<Double> b) {
        double d = 7000;
//        for (int i = 0; i < b.size(); i++) {
//            if (b.get(i) > d) {
//                d = b.get(i);
//            }
//        }
//        return ((int) d / 10) * 10 ;

        return (int)d;
    }



    //获取平均值距离
    int getStepInList(List<Double> b) {
        LogUtils.e("getMaxInList" + getMaxInList(b));
        LogUtils.e("getStepInList" + getMaxInList(b) / 5);
        return getMaxInList(b) / 5;
    }
    //获取平均值距离
    int getStepInListMount(List<Double> b) {
        LogUtils.e("getMaxInList" + getMaxInListMount(b));
        LogUtils.e("getStepInList" + getMaxInListMount(b) / 5);
        return getMaxInListMount(b) / 5;
    }



    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     *  //判断日期
     * */
    public  List<Date> dateToWeekA(Date mdate){
        int b=mdate.getDay();
        Date fdate ;
        List <Date> list = new ArrayList();
        Long fTime=mdate.getTime()-b*24*3600000;
        for(int a=0;a<7;a++){
            fdate= new Date();
            fdate.setTime(fTime+(a*24*3600000));
            list.add(a, fdate);
        }
        return list;
    }


}
