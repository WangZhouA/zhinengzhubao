package test.com.shoushi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import test.com.shoushi.R;
import test.com.shoushi.adapter.JiBuPaiHangBangAdapter;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.interfaces.IUpadterDateListener;
import test.com.shoushi.sharedPreferences.UserInfo;

/**
 * Created by 陈姣姣 on 2017/9/16.
 */

public class JiBuActivity extends BaseActivity implements View.OnClickListener,IUpadterDateListener {

    @BindView(R.id.header_left)
    public ImageButton header_left;
    @BindView(R.id.header_text)
    public TextView header_text;
    @BindView(R.id.header_right)
    public ImageButton header_right;

    @BindView(R.id.header_all)
    RelativeLayout header_all;

    @BindView(R.id.jibu_listview)
    ListView jibu_listview;

    private List<QuanPaihang.DataBean> lists;

    UserInfo userInfo=new UserInfo(this);

    JiBuPaiHangBangAdapter adpater;

    @BindView(R.id.haoyou_img)
    ImageView haoyou_img;

    @BindView(R.id.haoyou_tv)
    TextView haoyou_tv;

    @BindView(R.id.tv_num)
    TextView tv_num;

    @BindView(R.id.tv_number_bushu)
    TextView tv_number_bushu;

    @BindView(R.id.jibu_img)
    ImageView jibu_img;

    @BindView(R.id.ji_sum)
    TextView ji_sum;


    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){


                case 1:
                    adpater.setData(lists);
                    if (name.contains("null")){

                        if (phone.contains("null")){
                            haoyou_tv.setText("");
                        }else {
                            haoyou_tv.setText(phone);
                        }

                    }else {
                        haoyou_tv.setText(name);
                        userInfo.setUserInfo("name",name);
                    }


                    if (phone.contains("null")){

                    }else {

                        userInfo.setUserInfo("phone",phone);
                    }


                    if (headimg!=null){
                        userInfo.setUserInfo("headimg",StringUtils.HTTP_SERVICE+headimg);
                        StringUtils.showImage(JiBuActivity.this,StringUtils.HTTP_SERVICE+headimg,R.mipmap.img_yonghumr,R.mipmap.img_yonghumr,haoyou_img);
                    }else {
                        haoyou_img.setImageResource(R.mipmap.img_yonghumr);

                    }

                    if (num.contains("null")){
                        tv_num.setText("暂无排名");
                    }else {
                        tv_num.setText("第 "+num+" 名");
                    }

                    if (number.contains("null")){
                        tv_number_bushu.setText("0");
                    }else {
                        tv_number_bushu.setText(number);
                    }


                    if (sum.contains("null")){
                        ji_sum.setText("0");
                    }else {
                        ji_sum.setText(sum);
                    }

                    if (isf==null){
                        jibu_img.setImageResource(R.mipmap.btn_z_wdian);
                    }else {
                        jibu_img.setImageResource(R.mipmap.btn_z_ydian);
                    }


                    break;


                case 2:
                    if (name.contains("null")){

                        if (phone.contains("null")){
                            haoyou_tv.setText("");
                        }else {
                            haoyou_tv.setText(phone);
                        }

                    }else {
                        haoyou_tv.setText(name);
                        userInfo.setUserInfo("name",name);
                    }


                    if (headimg.contains("null")){
                        haoyou_img.setImageResource(R.mipmap.img_yonghumr);
                    }else {


                        userInfo.setUserInfo("headimg",StringUtils.HTTP_SERVICE+headimg);
                        StringUtils.showImage(JiBuActivity.this,StringUtils.HTTP_SERVICE+headimg,R.mipmap.img_yonghumr,R.mipmap.img_yonghumr,haoyou_img);
                    }

                    tv_num.setText("暂无排名");

                    tv_number_bushu.setText("0");

                    jibu_img.setImageResource(R.mipmap.btn_z_wdian);

                    ji_sum.setText("0");




                    break;
            }
        }
    };




    @OnClick({R.id.header_left,R.id.header_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_left:
                finish();
                break;
            case R.id.header_right:

                Intent intent =new Intent(this,LiShiJiBuActivity.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_jibu;
    }

    @Override
    protected void init() {

        jibu_listview= (ListView) findViewById(R.id.jibu_listview);
        lists = new ArrayList<>();
        header_all.setBackgroundColor(Color.parseColor("#99242424"));
        header_text.setText("计步");
        header_right.setVisibility(View.VISIBLE);
        header_right.setImageResource(R.mipmap.btn_lishijl);
        okHttpQuery();
        adpater=new JiBuPaiHangBangAdapter(lists,this);
        adpater.setiUpadterDateListener(this);
        jibu_listview.setAdapter(adpater);
    }

    @Override
    protected  void  onResume() {

        super.onResume();
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
                    lists.clear();
                    if (quanPaihang.getResult().contains("1")) {
                        if (quanPaihang.getData().size() > 0) {
                            /**
                             *  uName 为空
                             * */
                            name = quanPaihang.getData2().getUname();
                            Log.i("--->name", name);
                            headimg = quanPaihang.getData2().getHeadimg();
                            phone = quanPaihang.getData2().getPhone();
                            number = quanPaihang.getData2().getNumber();
                            num = quanPaihang.getData2().getNum();
                            isf = quanPaihang.getData2().getIsf();
                            sum = quanPaihang.getData2().getSum();

                            lists = quanPaihang.getData();
                            Log.i("--->", lists.toString());
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            handler.sendMessage(msg);

                        }
                    }else {

                        okHttpQuerUserMsg();
                    }
                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });

    }

    private void okHttpQuerUserMsg() {

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

    @Override
    public void updateDate() {
        okHttpQuery();
    }
}
