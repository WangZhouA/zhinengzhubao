package test.com.shoushi.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
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
import test.com.shoushi.Entity.QuanPaihang;
import test.com.shoushi.R;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.interfaces.IUpadterDateListener;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.CircleImageView;

/**
 * Created by 陈姣姣 on 2017/9/19.
 */

public class JiBuPaiHangBangAdapter extends BaseAdapter {


    private Handler handler =new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case 0:
                    /**
                     *  去刷新排行榜
                     * */
                    iUpadterDateListener.updateDate();
                    break;
                case 1:
                    /**
                     *  去刷新排行榜
                     * */
                    iUpadterDateListener.updateDate();
                    break;
            }
        }
    };






    // Adapter for holding devices found through scanning.
    private UserInfo userInfo;
    private List<QuanPaihang.DataBean> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;
    private boolean ischeck;



    private IUpadterDateListener iUpadterDateListener;

    public void setiUpadterDateListener(IUpadterDateListener iUpadterDateListener) {
        this.iUpadterDateListener = iUpadterDateListener;
    }

    public JiBuPaiHangBangAdapter(List<QuanPaihang.DataBean> mLeDevices, Activity mContext) {
        this.mLeDevices = mLeDevices;
        this.mInflator = mInflator.from(mContext);
        this.mContext = mContext;
        userInfo = new UserInfo(mContext);
    }

    public void setData(List<QuanPaihang.DataBean> mLeDevices) {
        this.mLeDevices = mLeDevices;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        Log.i("---> mLeDevices.size()",""+ mLeDevices.size());
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
            view = mInflator.inflate(R.layout.item_paihangbang, null);
            viewHolder = new ViewHolder();
            viewHolder.paihang_haoyou_tv_name = (TextView) view.findViewById(R.id.paihang_haoyou_tv);
            viewHolder.paihang_number = (TextView) view.findViewById(R.id.paihang_number);
            viewHolder.paihang_runs = (TextView) view.findViewById(R.id.paihang_runs);
            viewHolder.paihang_people = (TextView) view.findViewById(R.id.paihang_people);
            viewHolder.paihang_xin = (ImageView) view.findViewById(R.id.paihang_xin);
            viewHolder.paihang_img = (CircleImageView) view.findViewById(R.id.paihang_img);
            viewHolder. linear_dianzang= (LinearLayout) view.findViewById(R.id.linear_dianzang);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        /**
         *  照片
         * */

        if (mLeDevices.get(i).getHeadimg()!=null){
            StringUtils.showImage(mContext,StringUtils.HTTP_SERVICE+mLeDevices.get(i).getHeadimg(),R.mipmap.img_yonghumr,R.mipmap.img_yonghumr, viewHolder.paihang_img);
        }else {
            viewHolder.paihang_img.setImageResource(R.mipmap.img_yonghumr);
        }



        //如若备注名不为空
        if (mLeDevices.get(i).getFname()!=null) {
            viewHolder.paihang_haoyou_tv_name.setText(mLeDevices.get(i).getFname());
            //如果备注名为空用户名不为空
        }else if (mLeDevices.get(i).getFname()==null && mLeDevices.get(i).getUname()!=null){
            viewHolder.paihang_haoyou_tv_name.setText(mLeDevices.get(i).getUname());
            //如果 备注名为空并且用户名为空
        }else if (mLeDevices.get(i).getFname()==null && mLeDevices.get(i).getUname()==null){
            viewHolder.paihang_haoyou_tv_name.setText(mLeDevices.get(i).getPhone());
        }


        /**
         * 用户名
         * */

        if (mLeDevices.get(i).getHeadimg()!=null) {
            viewHolder.paihang_img.setImageResource(R.mipmap.img_yonghumr);
        } else {
            StringUtils.showImage(mContext, StringUtils.HTTP_SERVICE +mLeDevices.get(i).getHeadimg(), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.paihang_img);
        }


        viewHolder.paihang_number.setText("第 " + mLeDevices.get(i).getNum() + " 名");

        /**
         * 步数
         * */
        if ("0".equals(mLeDevices.get(i).getNumber())) {
            viewHolder.paihang_runs.setText("0");
        } else {
            viewHolder.paihang_runs.setText(mLeDevices.get(i).getNumber());
        }


        /**
         * 排名
         * */
        viewHolder.paihang_people.setText(mLeDevices.get(i).getSum());

        /**
         * 点赞
         * */
        if (mLeDevices.get(i).getIsf()==null) {
            viewHolder.paihang_xin.setImageResource(R.mipmap.btn_z_wdian);

        } else {
            viewHolder.paihang_xin.setImageResource(R.mipmap.btn_z_ydian);

        }



        viewHolder.linear_dianzang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ischeck == false) {
                    okHttpDianZang(i);
                    viewHolder.paihang_xin.setImageResource(R.mipmap.btn_z_ydian);
                    ischeck = true;
                    viewHolder.paihang_people.setText(String.valueOf(Integer.parseInt(mLeDevices.get(i).getSum())+1));

                } else {
                    okHttpQuXiao(i);
                    viewHolder.paihang_xin.setImageResource(R.mipmap.btn_z_wdian);
                    ischeck = false;
                    viewHolder.paihang_people.setText(String.valueOf(Integer.parseInt(mLeDevices.get(i).getSum())-1));
                }

            }
        });


        return view;
    }
    /**
     * 取消点赞
     * */
    private void okHttpQuXiao(int i) {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userInfo.getStringInfo("id"));
            Log.e("--->dsid",""+mLeDevices.get(i).getId());
            jsonObject.put("dsid", mLeDevices.get(i).getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.QUXIAO_DIAN_ZANG)
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

                    Log.i("--->", "正确的信息" + response.body().string());
                    Message msg =handler.obtainMessage();
                    msg.what=1;
                    handler.sendMessage(msg);
                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });


    }

    class ViewHolder {

        CircleImageView paihang_img;
        TextView paihang_haoyou_tv_name;
        TextView paihang_number;
        TextView paihang_runs;
        TextView paihang_people;
        ImageView paihang_xin;
        LinearLayout linear_dianzang;

    }


    /**
     *  点赞
     * */
    private void okHttpDianZang(int position) {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userInfo.getStringInfo("id"));
            Log.e("--->dsid",""+mLeDevices.get(position).getId());
            jsonObject.put("dsid", mLeDevices.get(position).getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.DIAN_ZANG)
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

                    String results = response.body().string();
                    Log.i("--->", "正确信息" + results);
                    Message msg =handler.obtainMessage();
                    msg.what=1;
                    handler.sendMessage(msg);
                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });


    }

}