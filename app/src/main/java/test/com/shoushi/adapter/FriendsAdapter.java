package test.com.shoushi.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import test.com.shoushi.Entity.Friends;
import test.com.shoushi.R;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.sharedPreferences.UserInfo;
import test.com.shoushi.view.CircleImageView;

/**
 * Created by 陈姣姣 on 2017/9/13.
 */

public class FriendsAdapter extends BaseAdapter {
    // Adapter for holding devices found through scanning.
    private UserInfo userInfo ;
    private List<Friends> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;


    public FriendsAdapter(List<Friends> mLeDevices, Activity mContext) {
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
            view = mInflator.inflate(R.layout.item_activity_friend , null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) view.findViewById(R.id.item_haoyou_tv);
            viewHolder.dervice_back = (CircleImageView) view.findViewById(R.id.item_haoyou_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (mLeDevices.get(i).getfName().contains("null")) {
            if (mLeDevices.get(i).getName().contains("null")) {
                viewHolder.deviceName.setText(mLeDevices.get(i).getPhone());
            }else {
                viewHolder.deviceName.setText(mLeDevices.get(i).getName());
            }

        }else {
            viewHolder.deviceName.setText(mLeDevices.get(i).getfName());

        }



            /**
             * 不名原因暂时屏蔽
             * */


//
            if (mLeDevices.get(i).getHeadimg().contains("null")){
                viewHolder.dervice_back.setImageResource(R.mipmap.img_yonghumr);
            }else {

            Log.i("---->图片头像","158"+mLeDevices.get(i).getHeadimg());

                StringUtils.showImage(mContext,StringUtils.HTTP_SERVICE+mLeDevices.get(i).getHeadimg(),R.mipmap.img_yonghumr,R.mipmap.img_yonghumr, viewHolder.dervice_back);
            }


        return view;
    }


    class ViewHolder {
        TextView deviceName;
        CircleImageView dervice_back;

    }
}
