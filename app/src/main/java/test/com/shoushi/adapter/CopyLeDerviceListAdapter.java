package test.com.shoushi.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import test.com.shoushi.Entity.Device;
import test.com.shoushi.R;
import test.com.shoushi.sharedPreferences.UserInfo;

/**
 * Created by 陈姣姣 on 2017/9/12.
 */

public class CopyLeDerviceListAdapter extends BaseAdapter{
    // Adapter for holding devices found through scanning.
    private UserInfo userInfo ;
    private List<Device> mLeDevices;
    private LayoutInflater mInflator;
    private Activity mContext;


    public CopyLeDerviceListAdapter(List<Device> mLeDevices, Activity mContext) {
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
            view = mInflator.inflate(R.layout.copy_item_search_dervice , null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) view.findViewById(R.id.dervice_name);
            viewHolder.dervice_back = (ImageView) view.findViewById(R.id.img_back_dervice);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }



         if (TextUtils.isEmpty(mLeDevices.get(i).getZdyDerviceName()) || mLeDevices.get(i).getZdyDerviceName().equals("null")) {
             viewHolder.deviceName.setText(mLeDevices.get(i).getDname());
         }else {
             viewHolder.deviceName.setText(mLeDevices.get(i).getZdyDerviceName());
         }
        return view;
    }


    class ViewHolder {
        TextView deviceName;
        ImageView dervice_back;

    }

}
