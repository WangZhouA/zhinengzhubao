package test.com.shoushi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
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
import test.com.shoushi.Entity.AddFriends;
import test.com.shoushi.R;
import test.com.shoushi.adapter.AddFriedsAdapter;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.CircleImageView;

/**
 * Created by 陈姣姣 on 2017/9/15.
 */

public class AddFriendsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.header_left)
    public ImageButton header_left;
    @BindView(R.id.header_text)
    public TextView header_text;
    @BindView(R.id.lists_friends_add)
    SwipeMenuListView lists_friends_add;
    private AddFriedsAdapter addFriedsAdapter;
    private List<AddFriends>friendses=new ArrayList<>();
    @BindView(R.id.add_tv)
    EditText add_tv;
    UserInfo userInfo=new UserInfo(this);
    @BindView(R.id.zhudong_shen)
    LinearLayout zhudong_shen;

    @BindView(R.id.haoyou_tv)
    TextView haoyou_tv;

    @BindView(R.id.haoyou_img)
    CircleImageView haoyou_img;

    @BindView(R.id.add_tianjia)
    Button add_tianjia;





    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    addFriedsAdapter.notifyDataSetChanged();
                    break;
                case  2:

                    zhudong_shen.setVisibility(View.VISIBLE);

                    if (name.contains("null")){
                        haoyou_tv.setText("");
                    }else {
                        haoyou_tv.setText(name);

                    }

                    if (headimg.contains("null")){
                        haoyou_img.setImageResource(R.mipmap.img_yonghumr);
                    }else {

                        StringUtils.showImage(AddFriendsActivity.this,StringUtils.HTTP_SERVICE+headimg,R.mipmap.img_yonghumr,R.mipmap.img_yonghumr,haoyou_img);
                    }


                    break;
                case  3:

                    showToast("没有查询出该用户信息");

                    break;
                case  4:

                    showToast("已发送添加好友请求");
                    zhudong_shen.setVisibility(View.GONE);
                    break;

            }


        }
    };
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private void initView() {

        //加入侧滑显示的菜单
        //1.首先实例化SwipeMenuCreator对象
        SwipeMenuCreator creater = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem item1 = new SwipeMenuItem(AddFriendsActivity.this);
                item1.setBackground(new ColorDrawable(Color.parseColor("#FD6B6B")));
                item1.setWidth(dp2px(72));
                item1.setTitle("删除");
                item1.setTitleSize(17);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

            }
        };
        // set creator
        lists_friends_add.setMenuCreator(creater);

        //2.菜单点击事件
        lists_friends_add.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AddFriends item = (AddFriends) friendses.get(position);

                switch (index) {

                    case 0:
                        //删除设备
                        okhttpDelete(position);
                        friendses.remove(position);
                        addFriedsAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }

    private void okhttpDelete(int position) {

        AddFriends  itemEntity = friendses.get(position);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.DELETE_FRIENDS_SHENQING+itemEntity.getId())
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

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_add_friends;
    }

    @Override
    protected void init() {
        header_text.setText("好友管理");
        addFriedsAdapter=new AddFriedsAdapter(friendses,this);
        lists_friends_add.setAdapter(addFriedsAdapter );
        initView();
        okHttpQueryFriendsAdd();



    }

    private void okHttpQueryFriendsAdd() {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.QUERT_FRIENDS_ADD+userInfo.getStringInfo("id"))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->", "" + e.toString());
            }

            AddFriends friends;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray ary =json.getJSONArray("data");
                        friendses.clear();
                        for (int i=0;i<ary.length();i++){
                            friends=new AddFriends();
                            JSONObject obj =ary.getJSONObject(i);
                            String afuid =obj.getString("afuid");
                            String information  = obj.getString("information");
                            String id   = obj.getString("id");
                            String name =obj.getString("name");
                            String state =obj.getString("state");
                            String uid =obj.getString("uid");
                            friends.setAfuid(afuid);
                            friends.setInformation(information);
                            friends.setId(id);
                            friends.setName(name);
                            friends.setState(state);
                            friends.setUid(uid);
                            friendses.add(friends);
                        }
                        Message msg = handler.obtainMessage();
                        msg.what=1;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    @OnClick({R.id.header_left,R.id.add_tv,R.id.add_tianjia})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_left:
                finish();
                break;
            case R.id.add_tv:

//                Intent intent =new Intent(this,Add_SouSuo.class);
//                startActivity(intent);

                break;
            case R.id.add_tianjia:

                /***
                 * 去申请添加好友
                 * */


                okHttpAddFriends();

                break;

        }
    }

    private void okHttpAddFriends() {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("information","你好");
            jsonObject.put("uid",userInfo.getStringInfo("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.SHENQING_FRIENDS+add_tv.getText().toString())
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
                    Message msg = handler.obtainMessage();
                    msg.what = 4;
                    handler.sendMessage(msg);

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });





    }
    ;
    private BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action =intent.getAction();
            String  phone =intent.getStringExtra("phone");
            Log.e("-->phone",phone);
            if (action.contains("SOUSUO_PHONE_HAOYOU")){

                try {
                    okHttpSouSuo(phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    String name;
    String headimg;
    String friends_id;

    private void okHttpSouSuo(String phone) throws JSONException {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone",phone);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.SOUSUO_FRIENDS)
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

                    String result =response.body().string();
                    try {
                        JSONObject obj =new JSONObject(result);
                        String results= obj.getString("result");
                        if (results.contains("1")) {
                            JSONObject item = obj.getJSONObject("data");
                            name = item.getString("name");
                            headimg = item.getString("headimg");
                            friends_id=item.getString("id");
                            Message msg = handler.obtainMessage();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }else {

                            Message msg = handler.obtainMessage();
                            msg.what = 3;
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(AddFriendsActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            if (isMobile(add_tv.getText().toString())) {
                add_tv.setText(add_tv.getText().toString());
                try {

                    okHttpSouSuo(add_tv.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                showToast("没有查询出该用户信息");
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }






}
