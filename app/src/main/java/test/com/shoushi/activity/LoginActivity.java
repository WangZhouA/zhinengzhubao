package test.com.shoushi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yescpu.keyboardchangelib.KeyboardChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import test.com.shoushi.MainActivity;
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;

public class LoginActivity extends BaseActivity implements View.OnClickListener,KeyboardChangeListener.KeyBoardListener

{
    @BindView(R.id.et_number)
    EditText et_number; //账号
    @BindView(R.id.et_password)
    EditText et_password; //密码

    @BindView(R.id.btn_loging)
    Button btn_loging; //登录

    @BindView(R.id.btn_reigst)
    Button btn_reigst; //注册

    @BindView(R.id.tv_repass)
    TextView tv_repass; // 忘记密码


    private Intent intent;

    private UserInfo userInfo;

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.Please_check_whether_the_account_password_is_correct),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    private KeyboardChangeListener mKeyboardChangeListener;
    @Override
    protected void init() {
        userInfo=new UserInfo(LoginActivity.this);
        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //判断用户是否不是第一次登陆
        if (userInfo.getStringInfo("userName")!=null && userInfo.getStringInfo("userPass")!=null){
            et_number.setText(userInfo.getStringInfo("userName"));
            et_password.setText(userInfo.getStringInfo("userPass"));
        }


//        mKeyboardChangeListener = new KeyboardChangeListener(this);
//        mKeyboardChangeListener.setKeyBoardListener(this);




    }


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.btn_loging,R.id.btn_reigst,R.id.tv_repass})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_loging:
//                intent =new Intent(this,MainActivity.class);
//                startActivity(intent);

                /**
                 * 测试阶段屏蔽，省开发时间
                 * */
                okHttpLogin();

                break;
            case R.id.btn_reigst:
                intent =new Intent(this,ReigstActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_repass:
                intent =new Intent(this,RePassWordActivity.class);
                startActivity(intent);
                break;
        }
    }

    //登录哦
    private void okHttpLogin() {
        //先判断有没有网络

        if (TureOrFalseNetwork()==true) {

            OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone",et_number.getText().toString() );
                jsonObject.put("password", et_password.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            final Request request = new Request.Builder()
                    .url(StringUtils.LOGIN)
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
//
                        String strResult = response.body().string();
                        Log.i("--->","正确的登录信息"+strResult);
                        try {
                            JSONObject jsonObjs = new JSONObject(strResult);
                            String s = jsonObjs.getString("result");
                            if (s.equals(1) || s.equals("1")) {
                                JSONObject obj = jsonObjs.getJSONObject("data");
                                String id = obj.getString("id");
                                Log.i("--->用户的id",id);
                                userInfo.setUserInfo("id", id);
                                userInfo.setUserInfo("userName", et_number.getText().toString().trim());
                                userInfo.setUserInfo("userPass", et_password.getText().toString());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else {

                                Message msg =handler.obtainMessage();
                                msg.what=1;
                                handler.sendMessage(msg);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.i("--->", "错误的登录信息" + response.body().string());
                    }
                }
            });
        }else {

            Toast.makeText(LoginActivity.this,"没有网络连接,请稍后再试",Toast.LENGTH_SHORT).show();
        }

    }



    //判断是否有网络连接
    private  boolean  TureOrFalseNetwork() {

        ConnectivityManager mConnectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        return   true;
    }










    /**
     *  回调的方法
     * */


    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        Log.i("-------->", "onKeyboardChange() called with: " + "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
    }
}
