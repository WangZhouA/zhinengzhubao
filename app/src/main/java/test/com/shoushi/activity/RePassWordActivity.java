package test.com.shoushi.activity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;

public class RePassWordActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.reigst_number)
    EditText reigst_number;
    @BindView(R.id.reigst_code)
    EditText reigst_code;
    @BindView(R.id.reigst_password)
    EditText reigst_password;
    @BindView(R.id.btn_reigst_code)
    Button btn_reigst_code;
    @BindView(R.id.btn_reigst)
    Button btn_reigst;
    //定时器
    private CountDownTimer timer;

    @Override
    protected int getContentView() {
        return R.layout.activity_reigst;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //成功获取验证码后
                case 0:
                    btn_reigst_code.setEnabled(false);
                    timer.start();

                    break;
                case 1:
                    showToast("修改成功");
                    finish();

                    break;
            }
        }
    };

    @Override
    protected void init() {
        btn_reigst.setText(R.string.re_password);
        reigst_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //定时器
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //时间期间
                btn_reigst_code.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                //时间结束后
                btn_reigst_code.setText(R.string.get_code);
                btn_reigst_code.setEnabled(true);
            }
        };

    }

//    android:textColor="#FF61B4"

    @OnClick({R.id.btn_reigst_code, R.id.btn_reigst})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reigst_code:

                if (isMobile(reigst_number.getText().toString()) == true) {
                    okHttpGetCode();
                } else {
                    showCustomToast("请输入有效的手机号码");
                }
                break;

            case R.id.btn_reigst:
                if (isMobile(reigst_number.getText().toString()) == true) {
                    if (!TextUtils.isEmpty(reigst_code.getText().toString())) {
                        if (!TextUtils.isEmpty(reigst_password.getText().toString())) {
                            okokHttpReigst();
                        } else {
                            showCustomToast("密码不能为空");
                        }

                    } else {
                        showCustomToast("验证码不能为空");
                    }
                } else {
                    showCustomToast("手机号码不合法");
                }


                break;

        }
    }

    //注册
    private void okokHttpReigst() {

        final String reigstStr = StringUtils.RE_PASSWORD+reigst_code.getText().toString();

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",reigst_number.getText().toString());
            jsonObject.put("password", reigst_password.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(reigstStr)
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
                    Log.i("--->", "正确的修改信息" + response.body().string());

                    Message www = handler.obtainMessage();
                    www.what = 1;
                    handler.sendMessage(www);

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }

    //获取验证码
    private void okHttpGetCode() {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", reigst_number.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.RE_GET_CODE)
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
                    Log.i("--->result", response.body().string());
                    Message msg =new Message();
                    msg.what=0;
                    handler.sendMessage(msg);
                } else {
                    Log.i("--->", "错误的验证码信息" + response.body().string());
                }
            }
        });
    }
}
