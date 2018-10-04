package test.com.shoushi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import test.com.shoushi.Entity.FriendsHome;
import test.com.shoushi.R;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.InputManager;
import test.com.shoushi.view.MyDialog;

/**
 * Created by 陈姣姣 on 2017/10/28.
 */

public class PingLunAdapter  extends BaseAdapter {

    /**
     * handler
     * */

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 0:

                    Toast.makeText(context, "回复成功", Toast.LENGTH_SHORT).show();
                    builder.dismiss();
                    Intent i =new Intent("GEN_XIN");
                    context.sendBroadcast(i);

                    break;


            }
        }
    };




    private List<FriendsHome.CommentBean.MomentsBean>lists;
    private Context context;
    private LayoutInflater mInflator;
    private UserInfo userInfo ;

    FriendsHomeAdapter friendsHomeAdapter;

    /**
     * dialog 布局
     * */
    MyDialog builder;
    View view;
    EditText editText;
    LinearLayout linear_layout;
    Button item_bt;


    public PingLunAdapter(List<FriendsHome.CommentBean.MomentsBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
        this.mInflator = mInflator.from(context);
        this.userInfo=new UserInfo(context);

        /**
         *  dialog
    * */
        view =LayoutInflater.from(context).inflate(R.layout.pinglun,null);
        builder = new MyDialog(context,  view, R.style.MyDialog,0);
        editText = (EditText) view.findViewById(R.id.item_et);
        linear_layout= (LinearLayout) view.findViewById(R.id.linear_layout);
        item_bt= (Button) view.findViewById(R.id.item_bt);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (view == null) {
            view = mInflator.inflate(R.layout.item_pinglun , null);
            viewHolder = new ViewHolder();
            viewHolder.pinglun_name= (TextView) view.findViewById(R.id.pinglun_name);
            viewHolder.wu_context= (TextView) view.findViewById(R.id.wu_context);
            viewHolder.bei_pinglun_name= (TextView) view.findViewById(R.id.bei_pinglun_name);
            viewHolder.beidong_name= (TextView) view.findViewById(R.id.beidong_name);
            viewHolder.you_huifu_context= (TextView) view.findViewById(R.id.you_huifu_context);
            viewHolder.item_HuiFu= (LinearLayout) view.findViewById(R.id.item_HuiFu);
            viewHolder.item_PeiHuiFu= (LinearLayout) view.findViewById(R.id.item_PeiHuiFu);
            viewHolder.et_text_context= (EditText) view.findViewById(R.id.et_text_context);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

/**
 *   去判断有没有回复，回复的是那一个
 *  */

        /**
         * 只有评论没有回复
         * */
        if (lists.get(position).getMrid()==null) {
            viewHolder.item_HuiFu.setVisibility(View.VISIBLE);
            viewHolder.item_PeiHuiFu.setVisibility(View.GONE);

            if (lists.get(position).getBname() != null) {
                Log.i("--->有几条数据",""+lists.get(position).getContent());
                viewHolder.pinglun_name.setText(lists.get(position).getBname());

            } else if (lists.get(position).getBname() == null && lists.get(position).getName() != null) {
                viewHolder.pinglun_name.setText(lists.get(position).getName());

            } else {
                viewHolder.pinglun_name.setText(lists.get(position).getPhone());
            }

            if (lists.get(position).getContent() != null) {
                viewHolder.wu_context.setText(lists.get(position).getContent());
            }
        }
        /**
         * 判断是否有回复
         * */
        if (lists.get(position).getMrid()!=null){
            viewHolder.item_PeiHuiFu.setVisibility(View.VISIBLE);
            if (lists.get(position).getBname() != null) {
                viewHolder.bei_pinglun_name.setText(lists.get(position).getBname());
            } else if (lists.get(position).getBname() == null && lists.get(position).getName() != null) {
                viewHolder.bei_pinglun_name.setText(lists.get(position).getName());
            } else {
                viewHolder.bei_pinglun_name.setText(lists.get(position).getPhone());
            }
            if (lists.get(position).getContent() != null) {
                viewHolder.you_huifu_context.setText(lists.get(position).getContent());
            }

            viewHolder.beidong_name.setText(""+lists.get(position).getMridName());

        }

        viewHolder.item_HuiFu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText.setCursorVisible(true);
                builder.show();
                InputManager.getInstances(context).totleShowSoftInput();
                Toast.makeText(context, ""+lists.get(position).getId(), Toast.LENGTH_SHORT).show();


                item_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                            try {
                                okHttpPinglun(position);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                    }
                });

            }
        });






        return view;

    }


    /**
     *  评论回复
     * */
    private void okHttpPinglun(final  int position) throws JSONException {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",editText.getText().toString());
        jsonObject.put("mid",lists.get(position).getMid());
        jsonObject.put("mrid",lists.get(position).getId());
        jsonObject.put("uid",userInfo.getStringInfo("id"));

//        Log.i("-------------->",""+mid);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.HUIFU_PINGLUN)
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
                    try {
                        JSONObject jsonb =new JSONObject(results);
                        String result =jsonb.getString("result");
                        if (result.contains("1")){
                            /**
                             *  评论成功
                             * */
                            Log.i("----->评论成功","");

                            Message msg = handler.obtainMessage();
                            msg.what=0;
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



    class  ViewHolder{
        TextView pinglun_name;
        TextView wu_context;
        TextView bei_pinglun_name;
        TextView beidong_name;
        TextView you_huifu_context;
        LinearLayout item_HuiFu;
        LinearLayout item_PeiHuiFu;
        Button msg_item_huifu;
        EditText et_text_context;
    }

}
