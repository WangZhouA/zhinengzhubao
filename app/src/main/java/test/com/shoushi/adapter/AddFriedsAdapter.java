package test.com.shoushi.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.com.shoushi.Entity.AddFriends;
import test.com.shoushi.R;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;

/**
 * Created by 陈姣姣 on 2017/9/15.
 */

public class AddFriedsAdapter extends BaseAdapter {
    // Adapter for holding devices found through scanning.
    private UserInfo userInfo ;
    private List<AddFriends> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;

    private Handler handler =   new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(mContext,"添加成功",Toast.LENGTH_SHORT).show();
            mContext.finish();
        }
    };
    public AddFriedsAdapter(List<AddFriends> mLeDevices, Activity mContext) {
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
            view = mInflator.inflate(R.layout.item_add_friends , null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) view.findViewById(R.id.haoyou_tv_add_item);
            viewHolder.dervice_back = (ImageView) view.findViewById(R.id.haoyou_img_add_item);
            viewHolder.add_btn = (Button) view.findViewById(R.id.add_btn);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.add_btn.setText("同意");
        viewHolder.deviceName.setText(mLeDevices.get(i).getName());

        viewHolder.add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                okHttpAddTongyi(i);


            }
        });
        return view;
    }

    /**
     * 我同意了
     * */
    private void okHttpAddTongyi(final  int position) {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.TONG_GUO_FRIENDS_ADD+mLeDevices.get(position).getId())
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

                    Message msg = handler.obtainMessage();
                    msg.what=0;
                    handler.sendMessage(msg);
                }
            }
        });

    }



    class ViewHolder {
        TextView deviceName;
        ImageView dervice_back;
        Button add_btn;

    }
}
