package test.com.shoushi.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.ble.Globals;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.MyDialog;

public class MessageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.header_left)
    ImageButton header_left;
    @BindView(R.id.header_right)
    ImageButton header_right;
    @BindView(R.id.header_text)
    TextView header_text;
    @BindView(R.id.img_phone)
    ImageView img_phone;

    @BindView(R.id.header_right_msg)
    TextView header_right_msg;


    boolean phone_flag = true;

    @BindView(R.id.img_weixin)
    ImageView img_weixin;

    boolean weixin_flag = true;
    @BindView(R.id.img_qq)
    ImageView img_qq;

    boolean qq_flag = true;

    MyDialog builder;
    View view;
    @BindView(R.id.header_haoyou)
    ImageButton headerHaoyou;
    @BindView(R.id.header_all)
    RelativeLayout headerAll;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_qq)
    TextView tvQq;

    private UserInfo userInfo;





    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void init() {
        Globals.init(this);

        /**
         * 创建  dialog
         * */
        view = LayoutInflater.from(MessageActivity.this).inflate(R.layout.qiangzhi_xiaxian_layout, null);
        builder = new MyDialog(MessageActivity.this, 0, 0, view, R.style.MyDialog);


        userInfo = new UserInfo(this);


        header_text.setText("来电提醒");
        header_right_msg.setText("权限");
        header_right_msg.setVisibility(View.VISIBLE);

    }

    String DH="0";
    String WX="0";
    String QQ="0";
    @OnClick({R.id.header_left, R.id.img_phone, R.id.img_weixin, R.id.img_qq, R.id.header_right_msg})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:

                finish();

                break;
            case R.id.header_right_msg:

                showConfirmDialog();

                break;
            case R.id.img_phone:

                if (Globals.toothConnect.isEnable()) {
                    if (phone_flag == false) {
                        img_phone.setImageResource(R.mipmap.btn_tixing_dianhua);
                        phone_flag = true;
                        tvPhone.setText("电话提醒");
                         DH ="0";
                        userInfo.setUserInfo("DH",DH);
                    } else {
                        img_phone.setImageResource(R.mipmap.btn_tixing_dianhuah);
                        phone_flag = false;
                        tvPhone.setText("电话提醒(点击开启)");
                         DH ="1";
                        userInfo.setUserInfo("DH",DH);

                    }
                } else {
                    builder.dismiss();
                    showToast("设备未连接");
                }

                break;
            case R.id.img_weixin:


                if (Globals.toothConnect.isEnable()) {
                    if (weixin_flag == false) {
                        img_weixin.setImageResource(R.mipmap.btn_tixing_weixin);
                        weixin_flag = true;
                        tvWeixin.setText("微信提醒");
                        WX="0";
                        userInfo.setUserInfo("WX",WX);
                    } else {
                        img_weixin.setImageResource(R.mipmap.btn_tixing_weixinh);
                        weixin_flag = false;
                        tvWeixin.setText("微信提醒(点击开启)");
                        WX="1";
                        userInfo.setUserInfo("WX",WX);

                    }
                } else {
                    showToast("设备未连接");
                }

                break;
            case R.id.img_qq:

                if (Globals.toothConnect.isEnable()) {
                    if (qq_flag == false) {
                        img_qq.setImageResource(R.mipmap.btn_tixing_qq);
                        qq_flag = true;
                        tvQq.setText("QQ提醒");
                        QQ="0";
                        userInfo.setUserInfo("QQ",QQ);
                    } else {
                        img_qq.setImageResource(R.mipmap.btn_tixing_qqh);
                        qq_flag = false;
                        tvQq.setText("QQ提醒(点击开启)");
                        QQ="1";
                        userInfo.setUserInfo("QQ",QQ);
                    }
                } else {
                    showToast("设备未连接");
                }

                break;
        }
    }

    private boolean isEnabledNLS = false;

    @Override
    protected void onResume() {
        super.onResume();
//
        if (!TextUtils.isEmpty(userInfo.getStringInfo("mac")) && userInfo.getStringInfo("mac") != null) {
            Log.i("--->lastAddress", userInfo.getStringInfo("mac"));
            Globals.toothConnect.ConnectTODevice(userInfo.getStringInfo("mac"));
        }

        isEnabledNLS = isEnabled();
        Log.i("isEnabledNLS = ", "" + isEnabledNLS);
        if (isEnabledNLS == false) {
            showConfirmDialog();
        } else if (isEnabledNLS == true) {
        }

/**
 *  去判断电话的
 * */

        if (userInfo.getStringInfo("DH")!=null){

            if (userInfo.getStringInfo("DH").contains("0")){
                img_phone.setImageResource(R.mipmap.btn_tixing_dianhua);
                phone_flag = true;
                tvPhone.setText("电话提醒");
                DH ="0";
                userInfo.setUserInfo("DH",DH);
            }else if (userInfo.getStringInfo("DH").contains("1")){
                img_phone.setImageResource(R.mipmap.btn_tixing_dianhuah);
                phone_flag = false;
                tvPhone.setText("电话提醒(点击开启)");
                DH ="1";
                userInfo.setUserInfo("DH",DH);

            }else {

                img_phone.setImageResource(R.mipmap.btn_tixing_dianhua);
                phone_flag = true;
                tvPhone.setText("电话提醒");
                DH ="0";
                userInfo.setUserInfo("DH",DH);

            }

        }


        /**
         *  去判断微信的
         *
         * */

        if (userInfo.getStringInfo("WX")!=null) {

             if (userInfo.getStringInfo("WX").contains("0")){

                 img_weixin.setImageResource(R.mipmap.btn_tixing_weixin);
                 weixin_flag = true;
                 tvWeixin.setText("微信提醒");
                 WX="0";
                 userInfo.setUserInfo("WX",WX);

             }else if (userInfo.getStringInfo("WX").contains("1")){

                 img_weixin.setImageResource(R.mipmap.btn_tixing_weixinh);
                 weixin_flag = false;
                 tvWeixin.setText("微信提醒(点击开启)");
                 WX="1";
                 userInfo.setUserInfo("WX",WX);

             }else {

                 img_weixin.setImageResource(R.mipmap.btn_tixing_weixin);
                 weixin_flag = true;
                 tvWeixin.setText("微信提醒");
                 WX="0";
                 userInfo.setUserInfo("WX",WX);

             }


        }

        /**
         *   电话
         * */

        if (userInfo.getStringInfo("QQ")!=null){

            if (userInfo.getStringInfo("QQ").contains("0")) {

                img_qq.setImageResource(R.mipmap.btn_tixing_qq);
                qq_flag = true;
                tvQq.setText("QQ提醒");
                QQ="0";
                userInfo.setUserInfo("QQ",QQ);


            }else if (userInfo.getStringInfo("QQ").contains("1")){

                img_qq.setImageResource(R.mipmap.btn_tixing_qqh);
                qq_flag = false;
                tvQq.setText("QQ提醒(点击开启)");
                QQ="1";
                userInfo.setUserInfo("QQ",QQ);

            }else {

                img_qq.setImageResource(R.mipmap.btn_tixing_qq);
                qq_flag = true;
                tvQq.setText("QQ提醒");
                QQ="0";
                userInfo.setUserInfo("QQ",QQ);

            }
        }






    }

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName
                        .unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showConfirmDialog() {


        builder.setCancelable(false);
        Button btn_no_xian_ss = (Button) view.findViewById(R.id.btn_no_xian_ss);
        Button btn_yes_xiaxian_ss = (Button) view.findViewById(R.id.btn_yes_xiaxian_ss);
        TextView for_tv_titles = (TextView) view.findViewById(R.id.for_tv_titles);
        TextView text_for_tv = (TextView) view.findViewById(R.id.text_for_tv);
        for_tv_titles.setText("通知权限");
        text_for_tv.setText("请启动通知权限访问");

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
                openNotificationAccess();
                builder.dismiss();
            }
        });

        builder.show();
    }


    private void openNotificationAccess() {
        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));

    }


}

