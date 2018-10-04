package test.com.shoushi.bas;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.library.XPermissionActivity;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import test.com.shoushi.R;
import test.com.shoushi.ble.Globals;
import test.com.shoushi.service.MyToNotificationServiceTo;
import test.com.shoushi.utils.DialogUtils;
import test.com.shoushi.view.HandyTextView;
import test.com.shoushi.view.LoadingDialog;


/**
 * Created by zhaoanxing on 2016/9/30.
 */
public abstract class BaseActivity extends XPermissionActivity {
    protected Dialog dialog;
    private Toast mToast;
    protected static LoadingDialog mLoadingDialog;

    MyToNotificationServiceTo myToNotificationServiceTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getContentView());
//        initState();
//        StatusBarCompat.compat(this, Color.WHITE);
        Globals.init(this);
        getSupportActionBar().hide();
        MyApplication.getInstance() .addActyToList(this);
        JPushInterface.init(getApplicationContext());
        checkAndroidMPermission();
        dialog = DialogUtils.createLoadingDialog(this);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //用于显示当前位于哪个活动
        Log.e("BaseActivity", getClass().getSimpleName());
        init();

        /**
         * 第一步先判断是否有网络
         * */
        if (TureOrFalseNetwork() == true) {

        } else {

            Toast.makeText(this, "没有网络连接,请稍后再试", Toast.LENGTH_SHORT).show();

        }




    }
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     *
     *  判断手机号码是否合法
     * */

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy销毁了", this.getPackageName());
        MyApplication.getInstance().removeActyFromList(this);
    }

    //注入布局
    protected abstract int getContentView();

    //初始化
    protected abstract void init();

    protected void showProgressDialog() {
        dialog.show();
    }

    public void finish() {
        super.finish();
        MyApplication.getInstance().removeActyFromList(this);
    }

     //控制返回
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            finish();
//            Log.e("onKeyDown销毁了", this.getPackageName());
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 隐藏一个ProgressDialog
     */
    protected void dismissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 方便的吐司显示，已经做了线程处理，因此可以直接在子线程中使用
     *
     * @param c
     *            需要显示的提示的内容
     */
    public void showToast(final CharSequence c) {
        if (Thread.currentThread().getName().equals("main")) {
            showCustomToast(c.toString());
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showCustomToast(c.toString());
                }
            });
        }
    }
    /** 显示自定义Toast提示(来自String) **/
    protected void showCustomToast(String text) {

        // 判断程序是否在前台运行 如果程序是在后台运行 不显示toast
        // if (!MyApplication.getInstance().isTopActivity()) {
        // return;
        // }
        View toastRoot = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.common_toast, null);
        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        if (mToast != null) {
            ((HandyTextView) toastRoot.findViewById(R.id.toast_text))
                    .setText(text);
        } else {
            mToast = new Toast(getApplicationContext());
        }
        // mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(toastRoot);
        mToast.show(); // 显示toast信息

    }
    /**
     * 判断网络连接
     * */
    private boolean TureOrFalseNetwork() {

        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        return true;
    }



    /**
     * 设置页面的Title
     *
     * @param title
     */
    public ImageButton header_left;
    public TextView header_title;
    public TextView header_right_title;
    public ImageButton header_right;

    public void initToolbar(String title) {

        View view = LayoutInflater.from(this).inflate(R.layout.header_bar, null);
        header_left = (ImageButton) view.findViewById(R.id.header_left);
        header_title = (TextView) view.findViewById(R.id.header_text);
        header_right = (ImageButton) view.findViewById(R.id.header_right);
        header_right_title = (TextView) view.findViewById(R.id.header_right_msg);
        if (TextUtils.isEmpty(title)) {
            header_title.setText(title);

        }
    }


    private void checkAndroidMPermission(){
        requestPermission(new String[]{
                Manifest.permission.CAMERA,//相机权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//SD卡图库权限
                Manifest.permission.ACCESS_FINE_LOCATION//定位权限
        }, new PermissionHandler() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied() {
                super.onDenied();
                Toast.makeText(BaseActivity.this, "拒绝授权，软件将无法正常使用", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onNeverAsk() {
                permissionDialog("权限申请","在设置-应用-权限中开启相关权限，以保证功能的正常使用");
                return super.onNeverAsk();
            }
        });
    }

    /**
     * 显示加载等待框
     *
     * @param title
     *            标题
     * @param message
     *            内容
     * @param cancelable
     *            是否可以通过返回取消
     */
    public void showLoadDialog(String title, String message, boolean cancelable) {
        closeLoadDialog();
        mLoadingDialog = LoadingDialog.createDialog(this, title, message,
                cancelable);
        mLoadingDialog.show();
    }

    /**
     * 显示加载等待框，默认不可以通过返回键关闭
     *
     * @param message
     *            内容
     */
    public void showLoadDialog(String message, boolean isfalse) {
        closeLoadDialog();
        mLoadingDialog = LoadingDialog.createDialog(this, "", message, isfalse);
        mLoadingDialog.show();
    }
    /**
     * 关闭加载等待框
     */
    public static void closeLoadDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.dismiss();
            } catch (Exception e) {
                Log.e("--->", "加载框异常！");
            }
        }
        mLoadingDialog = null;
    }

}
