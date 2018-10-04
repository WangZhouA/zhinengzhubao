package test.com.shoushi.img_tuku;

import android.content.Intent;
import android.util.Log;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.photo_tuku.ImagePagerActivity;

/**
 * Created by 陈姣姣 on 2017/11/13.
 */

public class ImageViewActivity extends BaseActivity {
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    private List<String> mImageList = new ArrayList<>();
    private String [] mImages  ;
    Intent intent;

    private List<String>listsimg =new ArrayList<>( ) ;

    @Override
    protected int getContentView() {
        return R.layout.image_layout;
    }

    @Override
    protected void init() {
        intent=getIntent();
        if (intent.getStringExtra(ImagePagerActivity.EXTRA_IMAGE_URLS)!=null){

            toClick(intent.getStringExtra(ImagePagerActivity.EXTRA_IMAGE_URLS));
        }

        initDatas();
        initListener();


    }


    private void initListener() {

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

              finish();

            }
        });
    }


    private void initDatas() {

        for (int i = 0; i < mImages.length; i++) {
            mImageList.add(mImages[i]);
            Log.i("---->mImages[i]", Arrays.toString(mImages));
        }
        convenientBanner.setPages(new CBViewHolderCreator<ImageViewHolder>() {
            @Override
            public ImageViewHolder createHolder() {
                return new ImageViewHolder();
            }
        },mImageList)
                .setPageIndicator(new int[]{R.mipmap.ponit_normal,R.mipmap.point_select}) //设置两个点作为指示器
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL); //设置指示器的方向水平居中

        convenientBanner.setCanLoop(true);
    }

    private void  toClick( String imageList){

        if (!imageList.contains(",")){
            listsimg.add(StringUtils.HTTP_SERVICE+imageList);
            mImages= listsimg.toArray(new String [listsimg.size()]);
            listsimg.clear();
        }else {

            String [] str =imageList.split(",");
            for (int i =0;i<str.length;i++){
                listsimg.add(StringUtils.HTTP_SERVICE+str[i]);
            }
            mImages= new String[listsimg.size()];
            mImages= listsimg.toArray(new String [listsimg.size()]);
            listsimg.clear();
        }
    }
}
