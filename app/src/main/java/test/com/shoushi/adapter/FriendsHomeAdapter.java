package test.com.shoushi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
import test.com.shoushi.img_tuku.ImageViewActivity;
import test.com.shoushi.interfaces.IUpadterDateListener;
import test.com.shoushi.photo_tuku.ImagePagerActivity;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.CircleImageView;
import test.com.shoushi.view.InputManager;
import test.com.shoushi.view.ListViewForScrollView;
import test.com.shoushi.view.MyDialog;
import test.com.shoushi.view.NoScrollGridView;

import static test.com.shoushi.R.id.item_time;

/**
 * Created by 陈姣姣 on 2017/10/26.
 */

public class FriendsHomeAdapter extends BaseAdapter  {



    /**
     * handler
     * */

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //评论成功
                case 0:
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    builder.dismiss();
                    iUpadterDateListener.updateDate();

                    break;
                //点赞成功
                case  1:

                    /**
                     *  去刷新排行榜
                     * */
                    iUpadterDateListener.updateDate();


                    break;

                case 2:

                    /**
                     *  去刷新排行榜
                     * */
                    iUpadterDateListener.updateDate();

                    break;


            }
        }
    };



    private List<FriendsHome.CommentBean> lists;
    private Activity mContext;
    private LayoutInflater mInflator;
    private UserInfo userInfo;
    private boolean ischeck;
    IUpadterDateListener iUpadterDateListener;
    private List<FriendsHome.CommentBean.MomentsBean> listMes;


    private List<String>listimg ;

    private List<String>tuKulist;

    /**
     * 图片和 评论的适配器
     * */
    GridViewAdapter gridViewAdapter;
    PingLunAdapter pingLunAdapter;


    /**
     * dialog 布局
     * */
    MyDialog builder;
    View view;
    EditText editText;
    LinearLayout linear_layout;
    Button item_bt;

    //点赞的标识符

    private int  flag;




    private List<Integer>listsInt =new ArrayList<>();



    public void setiUpadterDateListener(IUpadterDateListener iUpadterDateListener) {
        this.iUpadterDateListener = iUpadterDateListener;
    }

    public FriendsHomeAdapter() {
    }

    public FriendsHomeAdapter(List<FriendsHome.CommentBean> lists, Activity mContext) {
        this.lists = lists;
        this.mContext = mContext;
        this.mInflator = mInflator.from(mContext);
        this.userInfo = new UserInfo(mContext);
        listMes = new ArrayList<>();

        listimg =new ArrayList<>();
        tuKulist=new ArrayList<>();
        /**
         *  dialog
         * */
        view =LayoutInflater.from(mContext).inflate(R.layout.pinglun,null);
        builder = new MyDialog(mContext,  view, R.style.MyDialog,0);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        System.out.println("position<span></span>:" + position);
        Log.i("--->position<span>:",""+position);
        ViewHolder viewHolder ;
        ViewHolder holder1 = null;

        ViewHolder holder2 = null;

        ViewHolder  holder3 = null;

        int type = getItemViewType(position);

        if (convertView == null) {

            convertView = mInflator.inflate(R.layout.item_pengyou, null);
            viewHolder = new ViewHolder();

            viewHolder.circleImageView = (CircleImageView) convertView.findViewById(R.id.item_yonghu);
            viewHolder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.item_text = (TextView) convertView.findViewById(R.id.item_text);
            viewHolder.gridView = (NoScrollGridView) convertView.findViewById(R.id.Scrollgridview);
            viewHolder.item_time = (TextView) convertView.findViewById(item_time);
            viewHolder.item_sum = (TextView) convertView.findViewById(R.id.item_sum);
            viewHolder.item_dianzang = (ImageView) convertView.findViewById(R.id.item_dianzang);
            viewHolder.item_message = (ImageView) convertView.findViewById(R.id.item_message);
            viewHolder.item_list_item = (ListViewForScrollView) convertView.findViewById(R.id.item_list_item);
            viewHolder.tx_view = (TextView) convertView.findViewById(R.id.tx_view);


            /**
             * 测试使用
             * */

            viewHolder.layout_one= (LinearLayout) convertView.findViewById(R.id.layout_one);
            viewHolder.layout_two= (LinearLayout) convertView.findViewById(R.id.layout_two);
            viewHolder.layout_three= (LinearLayout) convertView.findViewById(R.id.layout_three);

            viewHolder.im_one= (ImageView) convertView.findViewById(R.id.im_one);
            viewHolder.im_two= (ImageView) convertView.findViewById(R.id.im_two);
            viewHolder.im_three= (ImageView) convertView.findViewById(R.id.im_three);
            viewHolder.im_four= (ImageView) convertView.findViewById(R.id.im_four);
            viewHolder.im_five= (ImageView) convertView.findViewById(R.id.im_five);
            viewHolder.im_six= (ImageView) convertView.findViewById(R.id.im_six);
            viewHolder.im_seven= (ImageView) convertView.findViewById(R.id.im_seven);
            viewHolder.im_eight= (ImageView) convertView.findViewById(R.id.im_eight);
            viewHolder.im_nine= (ImageView) convertView.findViewById(R.id.im_nine);


            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }



        /**
         * 设置adapter
         * */

        if ( lists.get(position).getMoments().size()!=0    &&   lists.get(position).getMoments().size()>0   ) {
            pingLunAdapter = new PingLunAdapter(lists.get(position).getMoments(), mContext);
            viewHolder.item_list_item.setAdapter(pingLunAdapter);
        }else {

        }


        /**
         *
         *  动态获取Listview 的子itme高度进行显示
         *
         * **/
        Utility.setListViewHeightBasedOnChildren(viewHolder.item_list_item);
        /**
         *  名字判断
         * */
        if (lists.get(position).getName() != null) {

            viewHolder.item_name.setText(lists.get(position).getName());
        }
        /**
         *  头像判断
         * */

        if (lists.get(position).getHeadimg() != null) {

            StringUtils.showImage(mContext, StringUtils.HTTP_SERVICE + lists.get(position).getHeadimg(), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.circleImageView);
        }

        /**
         *    心情文件判断
         * */

        if (lists.get(position).getContext() != null) {

            viewHolder.item_text.setText(lists.get(position).getContext());
        }


        /**
         *   点赞数量
         * */

        viewHolder.item_sum.setText(lists.get(position).getSum());

        /**
         *  是否点赞
         *
         * */

        if (lists.get(position).getFabulous() == 0) {

            viewHolder.item_dianzang.setImageResource(R.mipmap.btn_z_wdian);

        } else {
            viewHolder.item_dianzang.setImageResource(R.mipmap.btn_z_ydian);

        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.item_dianzang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ischeck == false) {
                    flag=0;
                    okHttpDianZang(StringUtils.DONGTAI_DIANZANG + userInfo.getStringInfo("id") + "/" + lists.get(position).getMid());
                    finalViewHolder.item_dianzang.setImageResource(R.mipmap.btn_z_ydian);
                    ischeck = true;
                    finalViewHolder.item_sum.setText(String.valueOf(Integer.parseInt(lists.get(position).getSum())+1));
                } else {
                    flag=1;
                    okHttpDianZang(StringUtils.QUXIAO_DONGTAI_DIANZANG + userInfo.getStringInfo("id") + "/" + lists.get(position).getMid());
                    finalViewHolder.item_dianzang.setImageResource(R.mipmap.btn_z_wdian);
                    ischeck = false;
                    finalViewHolder.item_sum.setText(String.valueOf(Integer.parseInt(lists.get(position).getSum())-1));
                }

            }
        });

        /**
         * 判断动态发表的时间
         * */
        String[] s = lists.get(position).getCreatetime().split("//.");
        viewHolder.item_time.setText(s[0]);


        /**
         * 判断有没有图片
         * */
        if (lists.get(position).getImg() != null && lists.get(position).getImg().contains(",")) {
            listimg.clear();
            String[] str = lists.get(position).getImg().split(",");
            List<String> listStr = new ArrayList<>();
            for (int i = 0; i < str.length; i++) {
                listStr.add(StringUtils.HTTP_SERVICE + str[i]);
            }
            /**
             * 给我的list赋值
             * */
//            listimg=listStr;
            gridViewAdapter = new GridViewAdapter((Activity) mContext, listStr);
            viewHolder.gridView.setAdapter(gridViewAdapter);

        } else if (lists.get(position).getImg() != null && !lists.get(position).getImg().contains(",")) {
            listimg.clear();
            List<String> listStr = new ArrayList<>();
            listStr.add(StringUtils.HTTP_SERVICE + lists.get(position).getImg());
            /**
             * 给我的list赋值
             * */
//            listimg=listStr;
            gridViewAdapter = new GridViewAdapter((Activity) mContext, listStr);
            viewHolder.gridView.setAdapter(gridViewAdapter);
        }


        // TODO: 2017/11/10       此为Gridview的备用方案，暂时使用 一定会被推翻，因bug无法解决 , 待解决Bug推翻次逻辑

        /**
         * 判断图片
         * */


        //如果只有一张图片
        if (lists.get(position).getImg() != null && !lists.get(position).getImg().contains(",")) {

            viewHolder.layout_one.setVisibility(View.VISIBLE);
            viewHolder.layout_two.setVisibility(View.GONE);
            viewHolder.layout_three.setVisibility(View.GONE);
            viewHolder.im_one.setVisibility(View.VISIBLE);
            viewHolder.im_two.setVisibility(View.GONE);
            viewHolder.im_three.setVisibility(View.GONE);
            List<String> listStr = new ArrayList<>();
            listStr.add(StringUtils.HTTP_SERVICE + lists.get(position).getImg());
            //加载一张图片

            StringUtils.showImage(mContext, StringUtils.HTTP_SERVICE + lists.get(position).getImg(), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);

        } else if (lists.get(position).getImg() != null && lists.get(position).getImg().contains(",")) {
            listimg.clear();
            //第一步去切割字符串判断有几张图片
            String[] str = lists.get(position).getImg().split(",");
            // 用于吧切割后的字符串放进list集合
            List<String> listStr = new ArrayList<>();
            //第二步 去循环 数组 目的是为了去判断有几张图片
            for (int i = 0; i < str.length; i++) {
                listStr.add(StringUtils.HTTP_SERVICE + str[i]);
            }

            // 有2张图片
            if (listStr.size() == 2) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.GONE);
                viewHolder.layout_three.setVisibility(View.GONE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.GONE);
                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                //有三张图片

            } else if (listStr.size() == 3) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.GONE);
                viewHolder.layout_three.setVisibility(View.GONE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.VISIBLE);
                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                StringUtils.showImage(mContext, listStr.get(2), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_three);
            } else if (listStr.size() == 4) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.VISIBLE);
                viewHolder.layout_three.setVisibility(View.GONE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.VISIBLE);
                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                StringUtils.showImage(mContext, listStr.get(2), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_three);
                StringUtils.showImage(mContext, listStr.get(3), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_four);
            } else if (listStr.size() == 5) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.VISIBLE);
                viewHolder.layout_three.setVisibility(View.GONE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.VISIBLE);
                viewHolder.im_five.setVisibility(View.VISIBLE);
                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                StringUtils.showImage(mContext, listStr.get(2), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_three);
                StringUtils.showImage(mContext, listStr.get(3), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_four);
                StringUtils.showImage(mContext, listStr.get(4), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_five);
            } else if (listStr.size() == 6) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.VISIBLE);
                viewHolder.layout_three.setVisibility(View.GONE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.VISIBLE);
                viewHolder.im_five.setVisibility(View.VISIBLE);
                viewHolder.im_six.setVisibility(View.VISIBLE);
                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                StringUtils.showImage(mContext, listStr.get(2), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_three);
                StringUtils.showImage(mContext, listStr.get(3), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_four);
                StringUtils.showImage(mContext, listStr.get(4), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_five);
                StringUtils.showImage(mContext, listStr.get(5), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_six);
            } else if (listStr.size() == 7) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.VISIBLE);
                viewHolder.layout_three.setVisibility(View.VISIBLE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.VISIBLE);
                viewHolder.im_five.setVisibility(View.VISIBLE);
                viewHolder.im_six.setVisibility(View.VISIBLE);

                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                StringUtils.showImage(mContext, listStr.get(2), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_three);
                StringUtils.showImage(mContext, listStr.get(3), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_four);
                StringUtils.showImage(mContext, listStr.get(4), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_five);
                StringUtils.showImage(mContext, listStr.get(5), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_six);
                StringUtils.showImage(mContext, listStr.get(6), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_seven);
            } else if (listStr.size() == 8) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.VISIBLE);
                viewHolder.layout_three.setVisibility(View.VISIBLE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.VISIBLE);
                viewHolder.im_five.setVisibility(View.VISIBLE);
                viewHolder.im_six.setVisibility(View.VISIBLE);
                viewHolder.im_eight.setVisibility(View.VISIBLE);

                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                StringUtils.showImage(mContext, listStr.get(2), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_three);
                StringUtils.showImage(mContext, listStr.get(3), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_four);
                StringUtils.showImage(mContext, listStr.get(4), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_five);
                StringUtils.showImage(mContext, listStr.get(5), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_six);
                StringUtils.showImage(mContext, listStr.get(6), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_seven);
                StringUtils.showImage(mContext, listStr.get(7), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_eight);
            } else if (listStr.size() == 9) {
                viewHolder.layout_one.setVisibility(View.VISIBLE);
                viewHolder.layout_two.setVisibility(View.VISIBLE);
                viewHolder.layout_three.setVisibility(View.VISIBLE);
                viewHolder.im_one.setVisibility(View.VISIBLE);
                viewHolder.im_two.setVisibility(View.VISIBLE);
                viewHolder.im_three.setVisibility(View.VISIBLE);
                viewHolder.im_five.setVisibility(View.VISIBLE);
                viewHolder.im_six.setVisibility(View.VISIBLE);
                viewHolder.im_eight.setVisibility(View.VISIBLE);
                viewHolder.im_nine.setVisibility(View.VISIBLE);
                StringUtils.showImage(mContext, listStr.get(0), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_one);
                StringUtils.showImage(mContext, listStr.get(1), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_two);
                StringUtils.showImage(mContext, listStr.get(2), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_three);
                StringUtils.showImage(mContext, listStr.get(3), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_four);
                StringUtils.showImage(mContext, listStr.get(4), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_five);
                StringUtils.showImage(mContext, listStr.get(5), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_six);
                StringUtils.showImage(mContext, listStr.get(6), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_seven);
                StringUtils.showImage(mContext, listStr.get(7), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_eight);
                StringUtils.showImage(mContext, listStr.get(8), R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, viewHolder.im_nine);
            }

        }else {
                viewHolder.layout_one.setVisibility(View.GONE);
                viewHolder.layout_two.setVisibility(View.GONE);
                viewHolder.layout_three.setVisibility(View.GONE);
        }

        viewHolder.im_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());

            }
        });


            viewHolder.im_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageBrower(lists.get(position).getImg());

                }
            });

        viewHolder.im_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());

            }
        });
        viewHolder.im_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());

            }
        });
        viewHolder.im_five.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());
            }
        });
        viewHolder.im_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());
            }
        });
        viewHolder.im_seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());
            }
        });
        viewHolder.im_eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());
            }
        });
        viewHolder.im_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(lists.get(position).getImg());
            }
        });


        /**
         *
         * 用来嵌套listview
         * */

        if (lists.get(position).getMoments().size() != 0) {
            viewHolder.item_list_item.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tx_view.setVisibility(View.GONE);
        }

//
//
//
//
//
//        viewHolder.item_list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//
//
//                Toast.makeText(mContext, ""+lists.get(position).getMoments().get(i).getName(), Toast.LENGTH_SHORT).show();
//
//
//
//
//                iUpadterDateListener.updateDate();
//
//            }
//        });

//
//
//
//


        /**
         * 回复
         * **/
        viewHolder.item_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText.setCursorVisible(true);
                builder.show();
                InputManager.getInstances(mContext).totleShowSoftInput();

                /***
                 *
                 *   提交评论
                 * */
                item_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                            try {
                                okHttpPinglun(position,lists.get(position).getMid());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });

        /**
         *   点击GridView 显示大图
         *
         * */










        ViewGroup.LayoutParams p = viewHolder.im_one.getLayoutParams();
        p.width =  mGetScreenWidth() / 4;
        ViewGroup.LayoutParams p2 = viewHolder.im_two.getLayoutParams();
        p2.width = mGetScreenWidth() / 4;

        ViewGroup.LayoutParams p3 = viewHolder.im_three.getLayoutParams();
        p3.width =  mGetScreenWidth() / 4;

        ViewGroup.LayoutParams p4 = viewHolder.im_four.getLayoutParams();
        p4.width =  mGetScreenWidth() / 4;

        ViewGroup.LayoutParams p5 = viewHolder.im_five.getLayoutParams();
        p5.width =  mGetScreenWidth() / 4;

        ViewGroup.LayoutParams p6 = viewHolder.im_six.getLayoutParams();
        p6.width =  mGetScreenWidth() / 4;

        ViewGroup.LayoutParams p7 = viewHolder.im_seven.getLayoutParams();
        p7.width =  mGetScreenWidth() / 4;

        ViewGroup.LayoutParams p8 = viewHolder.im_eight.getLayoutParams();
        p8.width =  mGetScreenWidth() / 4;

        ViewGroup.LayoutParams p9 = viewHolder.im_nine.getLayoutParams();
        p9.width =  mGetScreenWidth() / 4;



        return convertView;
    }


    //=================================================

    /**
     * 打开图片查看器
     *
     * @param
     * @param
     */
    protected void imageBrower(String str) {
        Intent intent = new Intent(mContext, ImageViewActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, str);
        mContext.startActivity(intent);

    }

    //============================================================================
    /**
     *  评论动态
     * */
    private void okHttpPinglun(final  int position,int mid) throws JSONException {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",editText.getText().toString());
        jsonObject.put("mid",mid);
        jsonObject.put("uid",userInfo.getStringInfo("id"));

        Log.i("-------------->",""+mid);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(StringUtils.PINGLUN_DONGTAI)
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


    /**
     * ViewHolder
     * */

    class ViewHolder {

        CircleImageView circleImageView;
        TextView item_name;
        TextView item_text;
        NoScrollGridView gridView;
        TextView item_time;
        TextView item_sum;
        ImageView item_dianzang;
        ImageView item_message;
        ListViewForScrollView item_list_item;
        TextView tx_view;


        /**
         *   测试使用
         * */

        LinearLayout layout_one;  // 第一行
        LinearLayout layout_two;  // 第二行
        LinearLayout layout_three;  // 第三行
        ImageView  im_one;
        ImageView  im_two;
        ImageView  im_three;
        ImageView  im_four;
        ImageView  im_five;
        ImageView  im_six;
        ImageView  im_seven;
        ImageView  im_eight;
        ImageView  im_nine;

    }

    /**
     * 点赞
     * */
    private void okHttpDianZang(String url) {

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(url)
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
                    if (flag==0) {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);

                    }else {
                        Message msg = handler.obtainMessage();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     * @param
     */


    /**
     *
     *  动态获取Listview 的子itme高度进行显示
     *
     * **/
    static class Utility {
        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }


    /**
     * 此方法动态的计算出 view的 宽高
     * */


    private int mGetScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }




}

