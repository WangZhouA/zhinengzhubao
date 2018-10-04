package test.com.shoushi.photo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import test.com.shoushi.R;


/**
 * 相机 或者相册选取类
 * @author liuml
 *
 */
public class PhotoUtil implements View.OnClickListener{

	private Context context;
	// 创建一个以当前时间为名称的文件
	public static final int CAMRA_SETRESULT_CODE = 0;// 相册返回码
	public static final int PHOTO_SETRESULT_CODE = 1;// 拍照返回码
	public static final int PHOTO_CORPRESULT_CODE = 2;// 裁剪返回码


	private TextView choosePhoto;
	private TextView takePhoto;

	Dialog dialog_new;

	public PhotoUtil(Context context) {
		this.context = context;

	}
	public void showDialog(){
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			dialog_new = new Dialog(context, R.style.ActionSheetDialogStyle);
			//填充对话框的布局
			View inflate = LayoutInflater.from(context).inflate(R.layout.bottom_dialog, null);
			//初始化控件
			choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
			takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
			choosePhoto.setOnClickListener(this);
			takePhoto.setOnClickListener(this);
			//将布局设置给Dialog
			dialog_new.setContentView(inflate);
			//获取当前Activity所在的窗体
			Window dialogWindow = dialog_new.getWindow();
			//设置Dialog从窗体底部弹出
			dialogWindow.setGravity(Gravity.BOTTOM);
			//获得窗体的属性
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.y = 20;//设置Dialog距离底部的距离
			//       将属性设置给窗体
			dialogWindow.setAttributes(lp);
			dialog_new.show();//显示对话框

		} else {
			Toast.makeText(context, context.getResources().getString(R.string.Please_insert_memory_card), Toast.LENGTH_SHORT).show();
		}
	}



	// 拍照使用系统当前日期加以调整作为照片的名称
	private static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// 拍照路径
	public String getPhotoPath() {
		File file = new File(Environment.getExternalStorageDirectory(), "/imgs");
		if (!file.exists()) {
			file.mkdirs();
		}
		String path = file.getPath() + "photo.jpg";
		return path;
	}

	// file转换成BitMap
	public static Bitmap readBitmapAutoSize(String filePath) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		Bitmap bm = null;
		try {

			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
			BitmapFactory.decodeFile(filePath, opt);
			opt.inDither = false;
			opt.inPreferredConfig = Bitmap.Config.RGB_565;

			// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
			// opt.inSampleSize = 1;
			opt.inSampleSize = computeSampleSize(opt, -1, 900 * 900);
			opt.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(filePath, opt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// bitmap转换成字节流
	public static String bitmaptoString(Bitmap bitmap) {
		// 将Bitmap转换成字符串
		String result = "";
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		byte[] bb = Base64.encode(bytes, Base64.DEFAULT);
		try {
			result = new String(bb, "UTF-8").replace("+", "%2B");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return result;
	}

	// 得到相册路径
	public String getCameraPath(Intent data) {
		Uri originalUri = data.getData();
		String[] proj = { MediaStore.Images.Media.DATA };

		// 好像是android多媒体数据库的封装接口，具体的看Android文档     数据库

		Cursor cursor = ((Activity) context).managedQuery(originalUri, proj,
				null, null, null);

		// 获取游标

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		// 将光标移至开头 ，这个很重要，不小心很容易引起越界

		cursor.moveToFirst();

		// 最后根据索引值获取图片路径

		String path = cursor.getString(column_index);
		return path;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.takePhoto:

				Intent intent_choose = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent_choose.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(getPhotoPath())));
				((Activity) context).startActivityForResult(intent_choose,
						PHOTO_SETRESULT_CODE);
				dialog_new.dismiss();
				break;

			case R.id.choosePhoto:
				Intent intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				((Activity) context).startActivityForResult(intent,
						CAMRA_SETRESULT_CODE);
				dialog_new.dismiss();

				break;



		}
	}
}