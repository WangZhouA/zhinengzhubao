package test.com.shoushi.img_tuku;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import test.com.shoushi.R;
import test.com.shoushi.http.StringUtils;

/**
 * Created by zlc on 2016/10/10.
 */

public class ImageViewHolder implements Holder<String>{

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data ) {
        StringUtils.showImage(context, data, R.mipmap.img_yonghumr, R.mipmap.img_yonghumr, imageView);
    }
}
