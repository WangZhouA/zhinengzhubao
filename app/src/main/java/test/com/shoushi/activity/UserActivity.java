package test.com.shoushi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.photo.LogUtils;
import test.com.shoushi.photo.PhotoClipActivity;
import test.com.shoushi.photo.PhotoUtil;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.MyDialog;

/**
 * Created by 陈姣姣 on 2017/9/16.
 */

public class UserActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.header_left)
    public ImageButton header_left;
    @BindView(R.id.header_text)
    public TextView header_text;

    @BindView(R.id.user_touxiang)
    ImageView user_touxiang;
    private PhotoUtil photoUtil;

    @BindView(R.id.rl_name)
    RelativeLayout rl_name;
    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.rl_phone)
    RelativeLayout rl_phone;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.rl_address)
    RelativeLayout rl_address;
    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.rl_repass)
    RelativeLayout rl_repass;

    //Bitmap转成的字符串
    String  base64code;

    @BindView(R.id.zhanghao_tv)
    TextView zhanghao_tv;


    private int flag ;

    private UserInfo userInfo=new UserInfo(this);
    private Dialog dialog;

    private Handler handler =new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //刚进来去刷新数据
                case 0:
                    if (name.contains("null")){

                        if (phone.contains("null")){
                            tv_name.setText("");
                        }else {
                            tv_name.setText(phone);

                        }

                    }else {
                        tv_name.setText(name);
                        userInfo.setUserInfo("name",name);
                    }


                    if (phone.contains("null")){
                        tv_phone.setText("");
                    }else {
                        tv_phone.setText(phone);
                        userInfo.setUserInfo("phone",phone);
                    }


                    if (headimg.contains("null")){
                        user_touxiang.setImageResource(R.mipmap.img_yonghumr);
                    }else {
//
//                        String [] str = headimg.split(":/");
//                        Log.e("--->tupian",StringUtils.HTTP_SERVICE+str[1]);
//                        userInfo.setUserInfo("headimg",StringUtils.HTTP_SERVICE+str[1]);
                        StringUtils.showImage(UserActivity.this,StringUtils.HTTP_SERVICE+headimg,R.mipmap.img_yonghumr,R.mipmap.img_yonghumr,user_touxiang);
                    }

                    if (address.contains("null")){
                        tv_address.setText("");
                    }else {
                        tv_address.setText(address);
                    }

                    break;
                case 1:
                    okHttpQuery();
                    break;
            }
        }
    };


    @Override
    protected void  onResume() {
        if (userInfo.getStringInfo("userName")!=null) {
            zhanghao_tv.setText(userInfo.getStringInfo("userName"));
        }
        okHttpQuery();

        super.onResume();
    }



    String name;
    String headimg;
    String phone;
    String sex;
    String address;


    //查询用户信息
    private void okHttpQuery() {
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
                            sex=itemObj.getString("sex");
                            address=itemObj.getString("address");
                            Message msg =handler.obtainMessage();
                            msg.what=0;
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
    protected int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void init() {
        header_text.setText("个人中心");

    }
    @OnClick({R.id.header_left, R.id.user_touxiang,R.id.rl_name,R.id.rl_phone,R.id.rl_address,R.id.rl_repass})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_left:

                Intent intent1 =new Intent("UPDATE_TOUXIANG");
                sendBroadcast(intent1);
                finish();
                break;

            case R.id.user_touxiang:
                photoUtil = new PhotoUtil(UserActivity.this);
                photoUtil.showDialog();
                break;

            case R.id.rl_name:
                flag=1;
                showdialog(getResources().getString(R.string.Re_name),tv_name,flag);
                break;

            case R.id.rl_phone:
//                flag=2;
//                showdialog(getResources().getString(R.string.Re_phone),tv_phone,flag);
                break;
            case R.id.rl_address:
                flag=3;
                showdialog(getResources().getString(R.string.Re_address),tv_address,flag);
                break;
            case R.id.rl_repass:
                Intent intent =new Intent(this,RePassWordActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     *
     *  添加设备弹窗
     * */

    private void showdialog(String text ,final TextView textview,final int flag){

        View view = LayoutInflater.from(UserActivity.this).inflate(R.layout.add_message,null);
        final MyDialog builder = new MyDialog(UserActivity.this, 0, 0, view, R.style.MyDialog);
        builder.setCancelable(false);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);
        Button  btn_determine= (Button) view.findViewById(R.id.btn_determine);
        final EditText ed_text= (EditText) view.findViewById(R.id.ed_text);
        SpannableString string = new SpannableString(text);//这里输入自己想要的提示文字
        ed_text.setHint(string);
        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        //确认按钮
        btn_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ed_text.getText().toString())){
                    textview.setText(ed_text.getText().toString());
                    builder.dismiss();
                    //修改信息
                    okHttpReMessage(textview,flag);
                }else {
                    showCustomToast(getResources().getString(R.string.cannot_be_empty));
                }
            }
        });

        builder.show();
    }


    //修改信息
    private void okHttpReMessage( TextView textview,int flag) {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();

        try {
            if (flag==1) {
                jsonObject.put("id", userInfo.getStringInfo("id"));
                jsonObject.put("name", textview.getText().toString());
            }else if (flag==2){
                jsonObject.put("id", userInfo.getStringInfo("id"));
                jsonObject.put("phone", textview.getText().toString());
            }else if (flag==3){
                jsonObject.put("id", userInfo.getStringInfo("id"));
                jsonObject.put("address", textview.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.REUSER_MSG)
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

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // 相册返回
        if (PhotoUtil.CAMRA_SETRESULT_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                // 相册选中图片路径
                String cameraPath = photoUtil.getCameraPath(data);
                Bitmap bitmap = photoUtil.readBitmapAutoSize(cameraPath);
                String str = photoUtil.bitmaptoString(bitmap);
                LogUtils.d("相相册选中路径  = " + cameraPath);
                startClipActivity(cameraPath);
            }
        }
        // 相机返回
        else if (PhotoUtil.PHOTO_SETRESULT_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                String photoPath = photoUtil.getPhotoPath();
                Bitmap bitmap = photoUtil.readBitmapAutoSize(photoPath);
                String str = photoUtil.bitmaptoString(bitmap);
                LogUtils.d("相机选中路径  = " + photoPath);
                startClipActivity(photoPath);

            }
        }
        // 裁剪返回
        else if (PhotoUtil.PHOTO_CORPRESULT_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                LogUtils.d("裁剪返回  = ");
                String path = data.getStringExtra("path");
//                BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
//                Bitmap bitmap = bitmapUtils.decodeFile(path);
//                user_touxiang.setImageBitmap(bitmap);
//                //吧图片转成了base64字符串
//                base64code= BitmapUtil.bitmaptoString(bitmap);
                /**
                 *  //去上传图片
                 * */
                File file =new File(path);
                Log.i("--->pathss",path);
                okHttpUpload("file",file);

            }
        }

    }

    public static final String MULTIPART_FORM_DATA = "image/jpg";       // 指明要上传的文件格式
    public  void okHttpUpload(String partName, File file){
        // 需要上传的文件
        RequestBody requestFile =               // 根据文件格式封装文件
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // 初始化请求体对象，设置Content-Type以及文件数据流
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)            // multipart/form-data
                .addFormDataPart(partName, file.getName(), requestFile)
                .build();

//        String  strurl ="http://106.14.33.148:8080/babystroller/sale/updateHeadimg/"+Consts.usernamephone;
//        Log.i("--->去上传图片的url",strurl);
        // 封装OkHttp请求对象，初始化请求参数
        Request request = new Request.Builder()
                .url(StringUtils.UPDATE_USER_IMG+userInfo.getStringInfo("id"))   // 上传url地址
                .post(requestBody)              // post请求体
                .build();
//        showLoadDialog("图片上传中...", true);

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                .connectTimeout(100000, TimeUnit.SECONDS)          // 设置请求超时时间
                .writeTimeout(100000,TimeUnit.SECONDS)
                .build();
        // 发起异步网络请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String result =response.body().string();
                    Log.i("--->result",result);
                    Message msg =new Message();
                    msg.what=1;
                    handler.sendMessage(msg);



                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->","失败"+e.toString());
            }
        });
    }



    //点击跳转到图片处理的界面
    public void startClipActivity(String path) {

        Intent intent = new Intent(this, PhotoClipActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, PhotoUtil.PHOTO_CORPRESULT_CODE);
    }

   boolean key;
    @Override
    public void onBackPressed() {
        if (key == false) {
            Intent intent1 =new Intent("UPDATE_TOUXIANG");
            sendBroadcast(intent1);
            finish();
        }

        else  if (key==true){
            super.onBackPressed();
        }
    }
}
