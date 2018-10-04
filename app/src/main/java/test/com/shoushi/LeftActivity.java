package test.com.shoushi;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.com.shoushi.activity.LoginActivity;
import test.com.shoushi.activity.UserActivity;
import test.com.shoushi.ble.Globals;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.CircleImageView;
import test.com.shoushi.view.MyDialog;

public class LeftActivity extends Fragment implements View.OnClickListener{

    private ImageView left_fangdiu;  //防丢
    private boolean fangdiuFlag;
    private Button left_user;   //个人信息
    private Button left_make; //使用说明
    private Button left_exit;
    private Intent intent;
    private UserInfo userInfo;
    private CircleImageView img_left;
    private TextView left_name;
    private TextView left_phone;


    String name;
    String headimg;
    String phone;



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {


                case 1:
                    if (name.contains("null")) {

                        if (phone.contains("null")) {
                            left_name.setText("");
                        } else {
                            left_name.setText(phone);
                        }

                    } else {
                        left_name.setText(name);
                    }

                    if (phone.contains("null")) {

                    } else {
                        left_phone.setText(phone);
                    }


                    if (headimg.contains("null")) {
                        img_left.setImageResource(R.mipmap.img_yonghumr);
                    } else {


                        userInfo.setUserInfo("headimg", StringUtils.HTTP_SERVICE +headimg);
                        StringUtils.showImage(getActivity(), StringUtils.HTTP_SERVICE + headimg, R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, img_left);
                    }



                    break;
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_left, container, false);
        init(v);
        Globals.init(getActivity());
        userInfo=new UserInfo(getActivity());

        okHttpQueryUserMsg();
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction("UPDATE_TOUXIANG");
        intentFilter.addAction("DEVICE_YUAN");
        getActivity().registerReceiver(broadcastReceiver,intentFilter);



        return v;
    }


    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.contains("UPDATE_TOUXIANG")){
                okHttpQueryUserMsg();
            }else if (action.contains("DEVICE_YUAN")){
                Toast.makeText(context, "距离过远", Toast.LENGTH_SHORT).show();

            }
        }
    };


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void init(View view) {
        left_fangdiu= (ImageView) view.findViewById(R.id.left_fangdiu);
        left_fangdiu.setOnClickListener(this);
        left_user= (Button) view.findViewById(R.id.left_user);
        left_user.setOnClickListener(this);
        left_make= (Button) view.findViewById(R.id.left_make);
        left_make.setOnClickListener(this);
        left_exit= (Button) view.findViewById(R.id.left_exit);
        left_exit.setOnClickListener(this);
        img_left= (CircleImageView) view.findViewById(R.id.img_left);
        left_name= (TextView) view.findViewById(R.id.left_name);
        left_phone= (TextView) view.findViewById(R.id.left_phone);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_fangdiu:
       if (Globals.toothConnect.isEnable()) {

           if (fangdiuFlag == false) {
               left_fangdiu.setImageResource(R.mipmap.btn_kaiguan_kai);
               fangdiuFlag = true;
               userInfo.setUserInfo("boo",fangdiuFlag);
               Globals.toothConnect.writeCharacteristic(Globals.SERVICE_UUID, Globals.WRITE_UUID, Globals.KAIQI_QQ_OPEN);


           } else {
               left_fangdiu.setImageResource(R.mipmap.btn_kaiguan_guan);
               fangdiuFlag = false;
               userInfo.setUserInfo("boo",fangdiuFlag);
               Globals.toothConnect.writeCharacteristic(Globals.SERVICE_UUID, Globals.WRITE_UUID, Globals.KAIQI_GUANBI);
           }
       }else {
           Toast.makeText(getActivity(), "请连接设备", Toast.LENGTH_SHORT).show();
       }




                break;
            case R.id.left_user:

                intent=new Intent(getActivity(), UserActivity.class);
                getActivity().startActivity(intent);

                break;
            case R.id.left_make:



                break;
            case R.id.left_exit:

                showDialog();

                break;
        }
    }

    private void showDialog() {

        View view =LayoutInflater.from(getActivity()).inflate(R.layout.qiangzhi_xiaxian_layout,null);
        final MyDialog builder = new MyDialog(getActivity(), 0, 0, view, R.style.MyDialog);
        builder.setCancelable(false);
        Button  btn_no_xian_ss= (Button) view.findViewById(R.id.btn_no_xian_ss);
        Button  btn_yes_xiaxian_ss= (Button) view.findViewById(R.id.btn_yes_xiaxian_ss);

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

                intent  =new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
            }
        });

        builder.show();
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

}
