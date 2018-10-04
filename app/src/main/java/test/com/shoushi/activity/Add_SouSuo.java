package test.com.shoushi.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;

/**
 * Created by 陈姣姣 on 2017/9/15.
 */

public class Add_SouSuo extends BaseActivity implements View.OnClickListener,TextWatcher{

    @BindView(R.id.sousuo_btn)
    Button sousuo_btn;

    @BindView(R.id.add_ed)
    EditText add_ed;

    @BindView(R.id.tv_sousuo_tiaojian)
    TextView tv_sousuo_tiaojian;

    @BindView(R.id.add_lin_sousuo)
   LinearLayout add_lin_sousuo;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_sousuo;
    }

    @Override
    protected void init() {
        add_ed.addTextChangedListener(this);
    }

    @OnClick({R.id.sousuo_btn,R.id.add_lin_sousuo})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sousuo_btn:
                finish();

                break;
            case R.id.add_lin_sousuo:

                if (isMobile(add_ed.getText().toString())==true){

                    Intent intent =new Intent("SOUSUO_PHONE_HAOYOU");
                    intent.putExtra("phone",add_ed.getText().toString());
                    sendBroadcast(intent);
                    finish();
                }else {
                    showCustomToast("手机号码不合法");
                }

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.e("beforeTextChanged", "-----------------------");
        Log.e("beforeTextChanged", "s:" + s + " start:" + start + " count:"
                + count + " after:" + after);
        // s:之前的文字内容
        // start:添加文字的位置(从0开始)
        // count:不知道 一直是0
        // after:添加的文字总数
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.e("onTextChanged", "-----------------------");
        Log.e("onTextChanged", "s:" + s + " start:" + start + " before:"
                + before + " count:" + count);
        // s:之后的文字内容
        // start:添加文字的位置(从0开始)
        // before:不知道 一直是0
        // before:添加的文字总数
    }

    @Override
    public void afterTextChanged(Editable s) {

        Log.e("afterTextChanged", "s:" + s);
        // s:之后的文字内容
        if (s.length()>0){
            add_lin_sousuo.setVisibility(View.VISIBLE);
            tv_sousuo_tiaojian.setText(s);
        }else {
            add_lin_sousuo.setVisibility(View.GONE);
        }



    }
}
