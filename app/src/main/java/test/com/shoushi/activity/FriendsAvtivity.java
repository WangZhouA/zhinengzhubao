package test.com.shoushi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
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
import test.com.shoushi.Entity.Friends;
import test.com.shoushi.R;
import test.com.shoushi.adapter.FriendsAdapter;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.ForPlayDialog;

/**
 * Created by 陈姣姣 on 2017/9/13.
 */

public class FriendsAvtivity  extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.header_haoyou)
    ImageButton header_haoyou;
    @BindView(R.id.header_text)
    TextView header_text;
    @BindView(R.id.header_right)
    ImageButton header_right;
    @BindView(R.id.header_left)
    ImageButton header_left;

    private UserInfo userInfo =new UserInfo(this);

    @BindView(R.id.list_haoyou_liebiao)
    SwipeMenuListView list_haoyou_liebiao;

    FriendsAdapter friendsAdapter;

    List<Friends>lists =new ArrayList<>();

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            friendsAdapter.notifyDataSetChanged();

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

                SwipeMenuItem item1 = new SwipeMenuItem(FriendsAvtivity.this);
                item1.setBackground(new ColorDrawable(Color.rgb(0xC7, 0xC7, 0xCD)));
                item1.setWidth(dp2px(72));
                item1.setTitle("删除");
                item1.setTitleSize(17);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

                SwipeMenuItem reName = new SwipeMenuItem(FriendsAvtivity.this);
                reName.setBackground(new ColorDrawable(Color.parseColor("#FD6B6B")));
                reName.setWidth(dp2px(72));
                reName.setTitle("更名");
                reName.setTitleSize(17);
                reName.setTitleColor(Color.WHITE);
                menu.addMenuItem(reName);
            }
        };
        // set creator
        list_haoyou_liebiao.setMenuCreator(creater);


        //2.菜单点击事件
        list_haoyou_liebiao.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Friends item = (Friends) lists.get(position);

                switch (index) {


                    case 1:
                        //修改设备名字
                        ReNameDialog(position);
                        break;
                    case 0:

                        //删除设备
                        okhttpDelete(position);
                        lists.remove(position);
                        friendsAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        //改名字
                        list_haoyou_liebiao.removeAllViewsInLayout();
                        friendsAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

    }
    //修改名字的弹窗
    private ForPlayDialog forPlayDialog;
    private String ed_dervice_name;
    private void ReNameDialog( final int position) {

        forPlayDialog = new ForPlayDialog(FriendsAvtivity.this);
        forPlayDialog.setTitle(getResources().getString(R.string.redervice_name));
        forPlayDialog.setYesOnclickListener(getResources().getString(R.string.determine), new ForPlayDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                ed_dervice_name = forPlayDialog.setMessage();

                if (ed_dervice_name != null) {
                    //已经拿到用户的名字，现在需要联网请求去改变名字了
                    //===============================
                    lists.get(position).setfName(ed_dervice_name);
                    //==================================
                    friendsAdapter.notifyDataSetChanged();
                    okHttpReName(position);
                }

                forPlayDialog.dismiss();
            }
        });
        forPlayDialog.setNoOnclickListener( getResources().getString(R.string.cancel), new ForPlayDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                forPlayDialog.dismiss();
            }
        });
        forPlayDialog.show();
    }


    private void okHttpReName(int position) {

        Friends itemEntity = lists.get(position);

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("name", ed_dervice_name);
            jsonObject.put("uid",userInfo.getStringInfo("id"));
            jsonObject.put("ufid", itemEntity.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Log.i("--->id",itemEntity.getId());
        final Request request = new Request.Builder()

                .url(StringUtils.RE_NAME_FRIENDS)
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
                    www.what = 2;
                    handler.sendMessage(www);

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }

    //删除设备
    private void okhttpDelete(int position) {
        Friends itemEntity = lists.get(position);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid",userInfo.getStringInfo("id"));
            jsonObject.put("ufid", itemEntity.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.DELETER_FRIENDS)
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
        return R.layout.activity_friends;
    }

    @Override
    protected void init() {
        header_haoyou.setVisibility(View.VISIBLE);
        header_text.setText("好友互动");
        header_right.setImageResource(R.mipmap.btn_penyouquan);
        header_right.setVisibility(View.VISIBLE);
        friendsAdapter =new FriendsAdapter(lists,this);
        list_haoyou_liebiao.setAdapter(friendsAdapter);
        initView();

    }

    @Override
    protected void onResume() {
        //查询用户
        okHttpQueryFriends();
        super.onResume();
    }

    private void okHttpQueryFriends() {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.QUERY_FRIENDS+userInfo.getStringInfo("id"))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->", "" + e.toString());
            }

            Friends friends;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray ary =json.getJSONArray("data");
                        lists.clear();
                        for (int i=0;i<ary.length();i++){
                            friends=new Friends();
                            JSONObject obj =ary.getJSONObject(i);
                            String name =obj.getString("name");
                            String phone  = obj.getString("phone");
                            String id   = obj.getString("id");
                            String sex =obj.getString("sex");
                            String headimg =obj.getString("headimg");
                            String age =obj.getString("age");
                            String fName =obj.getString("fName");

                            friends.setName(name);
                            friends.setPhone(phone);
                            friends.setId(id);
                            friends.setSex(sex);
                            friends.setHeadimg(headimg);
                            friends.setAge(age);
                            friends.setfName(fName);
                            lists.add(friends);
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
    Intent intent;
    @OnClick({R.id.header_haoyou,R.id.header_right,R.id.header_left})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_haoyou:
                intent =new Intent(this,AddFriendsActivity.class);
                startActivity(intent);

                break;

            case R.id.header_right:

                intent =new Intent(this,FriendsHomeActivity.class);
                startActivity(intent);
                break;

            case R.id.header_left:
                finish();
                break;
        }
    }



}
