package test.com.shoushi.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import test.com.shoushi.R;
import test.com.shoushi.http.StringUtils;

/**
 * Created by 陈姣姣 on 2017/10/27.
 */

public class GridViewAdapter extends BaseAdapter {

    Activity context;
    List<String> list;
    private int wh;
    public GridViewAdapter(Activity context, List<String> data) {
        this.context=context;
        this.list=data;
//        this.wh=(SysUtils.getScreenWidth(context)-SysUtils.Dp2Px(context, 99))/3;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        Holder holder;
        if (view==null) {
            view= LayoutInflater.from(context).inflate(R.layout.item_gridview, null);
            holder=new Holder();
            holder.imageView=(ImageView) view.findViewById(R.id.imageViews);
            ViewGroup.LayoutParams p = holder.imageView.getLayoutParams();
            p.width = p.height = mGetScreenWidth() / 3 - 20;
            view.setTag(holder);
        }else {
            holder= (Holder) view.getTag();
        }

        StringUtils.showImage(context,list.get(position),R.mipmap.img_yonghumr,R.mipmap.img_yonghumr, holder.imageView);

        if (position==0){
           notifyDataSetChanged();
        }



        return view;
    }

    class Holder{
        ImageView imageView;
    }
    private int mGetScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}