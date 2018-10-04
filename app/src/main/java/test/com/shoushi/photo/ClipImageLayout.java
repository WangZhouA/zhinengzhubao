package test.com.shoushi.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * 剪切视图
 */
public class ClipImageLayout extends RelativeLayout {

    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
//    private int type = 0;//0 16:9  1 其他

    private Context mContext;
    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding = 1;
    private int mVerticalPadding = (getHeight() - (getWidth() - 2 * mHorizontalPadding)) / 2;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        /**
         * 这里，直接写死了图片，
         */
        // mZoomImageView.setImageDrawable(getResources().getDrawable(
        // R.drawable.a));

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
        mZoomImageView.setVerticalPadding(mVerticalPadding);
        mClipImageView.setVerticalPadding(mVerticalPadding);

    }

    public void setImageDrawable(Drawable drawable) {
        mZoomImageView.setImageDrawable(drawable);
    }

    public void setImageDrawable(String path) {
    	//显示本地图片  设置本地图片
//        String showPath = ImageDownloader.Scheme.FILE.wrap(path);
    	BitmapUtils bitmapUtils = new BitmapUtils(mContext);
    	Bitmap bitmap = bitmapUtils.decodeFile(path);
    	mZoomImageView.setImageBitmap(bitmap);
//        ImageLoader.getInstance().displayImage(showPath, mZoomImageView);
//		mZoomImageView.setImageDrawable(drawable);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

	/**
	 * 设置宽高比例
	 * @param widthProportion
	 * @param heightProportion
	 */
	public void setProportion(int widthProportion,int heightProportion) {
		mClipImageView.setProportion(widthProportion, heightProportion);
		mZoomImageView.setProportion(widthProportion, heightProportion);
	}
    public void setVerticalPadding(int mVerticalPadding) {
    	this.mVerticalPadding = mVerticalPadding;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }

}
