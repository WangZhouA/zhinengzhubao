package test.com.shoushi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import test.com.shoushi.Entity.Device;
import test.com.shoushi.Iinface.OnMessageListener;
import test.com.shoushi.R;
import test.com.shoushi.adapter.CopyLeDerviceListAdapter;
import test.com.shoushi.adapter.LeDeviceListAdapter;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.ble.Globals;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.ForPlayDialog;

public class DerviceActivity extends BaseActivity implements View.OnClickListener,OnMessageListener{
    @BindView(R.id.search_iv)
    ImageView search_iv;
    @BindView(R.id.img_lanya)
    ImageView img_lanya;
    @BindView(R.id.pb_search)
    ProgressBar  pb_search;

    @BindView(R.id.list_Now_add)
    ListView list_Now_add;
    @BindView(R.id.list_mac)
    SwipeMenuListView list_mac;
    @BindView(R.id.relativeLayout_img)
    RelativeLayout relativeLayout_img;

    private List<Device>addLists =new ArrayList<>();

    private ArrayList<Device>lists=new ArrayList<>();//已经添加的

    private UserInfo userInfo =new UserInfo(this); //没有加添的

    @BindView(R.id.header_left)
    ImageButton header_left;
    @BindView(R.id.header_right)
    ImageButton header_right;
    @BindView(R.id.header_text)
    TextView header_text;


    //侧滑的适配器
    LeDeviceListAdapter searchDeviceAdapter;
    CopyLeDerviceListAdapter  copySearchDeviceAdapter;


    // 新建一个list专门用来存储设备的名字,用于判断使用
    List<String>derviceName =new ArrayList<>();



    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //查询已经添加的
                case 1:
                    copySearchDeviceAdapter.notifyDataSetChanged();
//                    if (addLists.size()>0) {
////                        if (!TextUtils.isEmpty(userInfo.getStringInfo("mac")) && userInfo.getStringInfo("mac") != null) {
//                            for (int i = 0; i < addLists.size(); i++) {
////                                if ((userInfo.getStringInfo("mac")).equals(addLists.get(i).getDmac())){
//                                    Log.i("--->对比mac地址",addLists.get(addLists.size()-1).getDmac());
//                                    Globals.toothConnect.ConnectTODevice(addLists.get(addLists.size()-1).getDmac());
////                                }
////                            }
//                        }
//                    }else {
//                        closeLoadDialog();
//                    }
//
//
//





                    break;

            }
        }
    };




    protected int getContentView() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_SCAN_FINISHED");//扫描完成
        filter.addAction("ACTION_BLUETOOTH_DEVICE");//正在扫描
        filter.addAction("QUXIAO");//
        registerReceiver(blueereceiver, filter);
        return R.layout.activity_dervice;
    }


    @Override
    protected void init() {
        //初始化
        Globals.init(DerviceActivity.this);
        header_text.setText("设备管理");
        header_right.setVisibility(View.VISIBLE);
        header_right.setImageResource(R.mipmap.btn_haoyou_sousuo);
        search_iv.setOnClickListener(this);
        searchDeviceAdapter = new LeDeviceListAdapter(lists, this);
        list_Now_add.setAdapter(searchDeviceAdapter);
        Log.e("--->search_iv", "1");
        list_Now_add.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Globals.toothConnect.connect(lists.get(position).getDmac()) == true) {
                    Log.e("--->连接成功", "连接成功");
                } else {
                    Log.e("--->连接失败", "连接失败");
                }

            }
        });
        copySearchDeviceAdapter = new CopyLeDerviceListAdapter(addLists, this);
        list_mac.setAdapter(copySearchDeviceAdapter);
        initView();
        searchDeviceAdapter.setOnDismissListener(this);


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(blueereceiver);
        super.onDestroy();
    }

    private void initView() {

        //加入侧滑显示的菜单
        //1.首先实例化SwipeMenuCreator对象
        SwipeMenuCreator creater = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem item1 = new SwipeMenuItem(DerviceActivity.this);
                item1.setBackground(new ColorDrawable(Color.rgb(0xC7, 0xC7, 0xCD)));
                item1.setWidth(dp2px(72));
                item1.setTitle("删除");
                item1.setTitleSize(17);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

                SwipeMenuItem reName = new SwipeMenuItem(DerviceActivity.this);
                reName.setBackground(new ColorDrawable(Color.parseColor("#FD6B6B")));
                reName.setWidth(dp2px(72));
                reName.setTitle("改名");
                reName.setTitleSize(17);
                reName.setTitleColor(Color.WHITE);
                menu.addMenuItem(reName);
            }
        };
        // set creator
        list_mac.setMenuCreator(creater);


        //2.菜单点击事件
        list_mac.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Device item = (Device) addLists.get(position);

                switch (index) {


                    case 1:
                        //修改设备名字
                        ReNameDialog(position);
                        break;
                    case 0:

                        //删除设备
                        okhttpDelete(position);
                        addLists.remove(position);
                        copySearchDeviceAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        //改名字
                        list_mac.removeAllViewsInLayout();
                        copySearchDeviceAdapter.notifyDataSetChanged();
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

        forPlayDialog = new ForPlayDialog(DerviceActivity.this);
        forPlayDialog.setTitle(getResources().getString(R.string.redervice_name));
        forPlayDialog.setYesOnclickListener(getResources().getString(R.string.determine), new ForPlayDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                ed_dervice_name = forPlayDialog.setMessage();

                if (ed_dervice_name != null) {
                    //已经拿到用户的名字，现在需要联网请求去改变名字了
                    //===============================
                    addLists.get(position).setDname(ed_dervice_name);
                    //==================================
                    copySearchDeviceAdapter.notifyDataSetChanged();
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

        Device itemEntity = addLists.get(position);

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("name", ed_dervice_name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Log.i("--->id",itemEntity.getId());
        final Request request = new Request.Builder()

                .url(StringUtils.RE_NAME+userInfo.getStringInfo("id")+"/"+itemEntity.getId())
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

    private void okhttpDelete(int position) {
        Device itemEntity = addLists.get(position);
        String str =StringUtils.DELETER_DERVICE+userInfo.getStringInfo("id")+"/"+itemEntity.getId();
        Log.i("--->str",str);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(str)
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


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    protected void onResume() {


        if (!TextUtils.isEmpty(userInfo.getStringInfo("mac"))  &&  userInfo.getStringInfo("mac")!=null ) {
            showLoadDialog("连接中",true);
            Log.i("--->lastAddress", userInfo.getStringInfo("mac"));
            Globals.toothConnect.ConnectTODevice(userInfo.getStringInfo("mac"));
        }
        okHttpDervice();
        super.onResume();

    }
    /**
     *
     *  查询哦
     */

    private void okHttpDervice() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.QUERY_DERVICE+userInfo.getStringInfo("id"))
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
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray  ary =json.getJSONArray("data");
                        addLists.clear();
                        for (int i=0;i<ary.length();i++){
                            Device dervice =new Device();
                            JSONObject obj =ary.getJSONObject(i);
                            String name =obj.getString("name");
                            String mac  = obj.getString("mac");
                            String id   = obj.getString("id");
                            String udName =obj.getString("udName");
                            dervice.setDname(name);
                            dervice.setDmac(mac);
                            dervice.setId(id);
                            dervice.setZdyDerviceName(udName);
                            addLists.add(dervice);
                            derviceName.add(name);
                        }

                        for (int  i =0;i<derviceName.size();i++){
                            Log.i("----------->Name",derviceName.get(i));
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

    @OnClick({R.id.search_iv,R.id.relativeLayout_img,R.id.header_left,R.id.header_right})

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_iv:
                Log.e("扫描状态", Globals.toothConnect.isConnect() + "");

                derviceName.clear();
                if (!Globals.toothConnect.isScanning()) {
                    Log.e("进来扫描了==========", "进来扫描了==========");
                    pb_search.setVisibility(View.VISIBLE);
                    lists.clear();
                    searchDeviceAdapter.notifyDataSetChanged();
                    Globals.toothConnect.scanLeDevice(true, 10000);
                } else {
                    Log.e("进来了","进来了");
                    Globals.toastor.showToast("正在扫描");
                }
                break;

            case R.id.header_left:

                finish();

                break;
            case R.id.header_right:
                /*
                * 扫描
                * **/
                Log.e("扫描状态", Globals.toothConnect.isConnect() + "");

                derviceName.clear();
                if (!Globals.toothConnect.isScanning()) {
                    Log.e("进来扫描了==========", "进来扫描了==========");
                    pb_search.setVisibility(View.VISIBLE);
                    lists.clear();
                    searchDeviceAdapter.notifyDataSetChanged();
                    Globals.toothConnect.scanLeDevice(true, 10000);
                } else {
                    Log.e("进来了","进来了");
                    Globals.toastor.showToast("正在扫描");
                }
                break;

        }
    }

    private BroadcastReceiver blueereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("进来的action", "++++++++++++++" + action);
            //扫描时间结束了需要干嘛
            if (action.contains("ACTION_SCAN_FINISHED")) {
                search_iv.setVisibility(View.VISIBLE);
                pb_search.setVisibility(View.GONE);
                //正在扫描到的蓝牙设备，扫到一个就加一个
            }else if (action.contains("ACTION_BLUETOOTH_DEVICE")){
                String name = intent.getStringExtra("name");
                String address = intent.getStringExtra("address");

                /**
                 * 此处逻辑用于去判断如果已经添加了设备就不会再扫描出来
                 * */

                if (derviceName.size()>0) {
                    for (int i = 0; i <derviceName.size();i++){
                        //如果一样的我就不加了
                        if (derviceName.get(i).contains(name)){
                            //不一样的我就加
                        }else {
                            lists.add(new Device(name, address, 0, "", ""));
                        }
                    }

                    searchDeviceAdapter.notifyDataSetChanged();

                }

                else {
                    lists.add(new Device(name, address, 0, "", ""));
                    searchDeviceAdapter.notifyDataSetChanged();

                }
            }else if (action.contains("QUXIAO")){
                closeLoadDialog();
            }
        }
    };


    @Override
    public void onMessage() {
        okHttpDervice();
    }
}
