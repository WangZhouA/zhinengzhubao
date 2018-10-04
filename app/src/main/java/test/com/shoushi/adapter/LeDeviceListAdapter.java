package test.com.shoushi.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
import test.com.shoushi.ble.Globals;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;


public class LeDeviceListAdapter extends BaseAdapter {

    // Adapter for holding devices found through scanning.
    private UserInfo userInfo ;
    private ArrayList<Device> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    private OnMessageListener onMessageListener;


    private Handler handler =new Handler(){

        @Override
        public void handleMessage(Message msg) {
             switch (msg.what){
                 //添加设备
                 case 1:

                     notifyDataSetChanged();
                     onMessageListener.onMessage();

                     break;
             }
        }
    };

    public LeDeviceListAdapter(ArrayList<Device> mLeDevices, Activity mContext) {
        this.mLeDevices = mLeDevices;
        this.mInflator = mInflator.from(mContext);
        this.mContext = mContext;
        userInfo=new UserInfo(mContext);
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (view == null) {
            view = mInflator.inflate(R.layout.item_search_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) view.findViewById(R.id.dervice_name);
            viewHolder.dervice_back = (TextView) view.findViewById(R.id.img_back_dervice);
            viewHolder.duankai = (TextView) view.findViewById(R.id.img_back_dervice_duankai);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String deviceName = mLeDevices.get(i).getDname();
        String deviceMac = mLeDevices.get(i).getDmac();
//        viewHolder.deviceName.setText(deviceName+":"+deviceMac);
        viewHolder.deviceName.setText(deviceName);

        viewHolder.dervice_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("--->mac",mLeDevices.get(i).getDmac());
                if ( Globals.toothConnect.connect(mLeDevices.get(i).getDmac())==true){
                    okHttpAddDervice(i);
                }else {
                    Toast.makeText(mContext,"连接失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.duankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.toothConnect.close();

            }
        });



        return view;
    }

    private void okHttpAddDervice(final int position) {
      final   Device dervice =mLeDevices.get(position);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",dervice.getDname());
            jsonObject.put("mac", dervice.getDmac());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.ADD_DERVICE_MAC+userInfo.getStringInfo("id"))
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
                    Log.i("--->成功", "" + response.body().string());
                    mLeDevices.remove(position);
                    userInfo.setUserInfo("mac",dervice.getDmac());
                    Message msg =new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }

            }
        });
    }


    class ViewHolder {
        TextView deviceName;
        TextView dervice_back;
        TextView duankai;
    }

    public   void  setOnDismissListener(OnMessageListener onMessageListener){
           this.onMessageListener= onMessageListener;
    }

}
