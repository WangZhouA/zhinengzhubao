package test.com.shoushi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

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
import test.com.shoushi.Entity.FriendsHome;
import test.com.shoushi.JarPhoto.PublishedActivity;
import test.com.shoushi.R;
import test.com.shoushi.adapter.FriendsHomeAdapter;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.interfaces.IUpadterDateListener;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.CircleImageView;

/**
 * Created by 陈姣姣 on 2017/9/15.
 */

public class FriendsHomeActivity extends BaseActivity implements View.OnClickListener,IUpadterDateListener{

    @BindView(R.id.header_left)
    ImageButton header_left;
    @BindView(R.id.header_right)
    ImageButton header_right;
    @BindView(R.id.header_text)
    TextView header_text;
    @BindView(R.id.header_all)
    RelativeLayout header_all;

    @BindView(R.id.lists_firends_home)
    ListView lists_firends_home;
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.imageView2)
    CircleImageView imageView2;

    private UserInfo userInfo;
    private List<FriendsHome.CommentBean>lists;
    private List<FriendsHome.CommentBean.MomentsBean>listMes;
    FriendsHomeAdapter friendsHomeAdapter;
    PublishedActivity publishedActivity;




    private android.os.Handler handler=new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    friendsHomeAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected int getContentView() {
        return R.layout.activity_friend_home;
    }


    @Override
    protected void init() {
        header_all.setBackgroundColor(Color.parseColor("#99242424"));
        header_text.setText("好友圈");
        header_right.setVisibility(View.VISIBLE);
        header_right.setImageResource(R.mipmap.btn_xinagjitb);
        userInfo=new UserInfo(this);
        lists=new ArrayList<>();
        listMes=new ArrayList<>();
        query();
        friendsHomeAdapter = new FriendsHomeAdapter(lists,this);
        lists_firends_home.setAdapter(friendsHomeAdapter);
        friendsHomeAdapter.setiUpadterDateListener(this);

        publishedActivity=new PublishedActivity();
        publishedActivity.setiUpadterDateListener(this);

        if (userInfo.getStringInfo("img")!=null){
            StringUtils.showImage(this, userInfo.getStringInfo("img"), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, imageView2);
        }



        /**
         * 广播收起
         * */
        IntentFilter IntentFilter =new IntentFilter("GEN_XIN");
        registerReceiver(broadcastReceiver,IntentFilter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @OnClick({R.id.header_right,R.id.header_left})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_right:

                Intent intent=new Intent(this,PublishedActivity.class);
                startActivity(intent);

                break;

            case R.id.header_left:

                finish();

                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        query();
    }

    // TODO: 2017/10/26  查询控件动态
    FriendsHome friendsHome;
    private void  query() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
//                .url(StringUtils.QUERY_SPACE_DYNAMIC + userInfo.getStringInfo("id"))
                .url(StringUtils.QUERY_SPACE_DYNAMIC + userInfo.getStringInfo("id"))
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
                    lists.clear();
                    Log.i("--->", "" + result);
                    Gson gson =new Gson();
                    friendsHome = gson.fromJson(result, FriendsHome.class);
                    lists.addAll(friendsHome.getComment());
                    Message msg =new Message();
                    msg.what=0;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void updateDate() {
        query();
    }



    private BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action =intent.getAction();
            if (action.contains("GEN_XIN")){
                query();
            }
        }
    };


}
