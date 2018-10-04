package test.com.shoushi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by 陈姣姣 on 2017/8/14.
 */

public class BitmapUtil {


    /**
     * 图片转bitmap
     * */
    public  static Bitmap getDiskBitmap(String pathString){

        Bitmap bitmap=null;
        try {

            File file=new File(pathString);
            if (file.exists()){
                bitmap= BitmapFactory.decodeFile(pathString);
            }

        }catch (Exception e){

        }
        return  bitmap;
    }



    public  static  String bitmaptoString(Bitmap bitmap){

        //吧bitmap转成base64字符串
        String string =null;
        ByteArrayOutputStream  bstream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bstream);
        byte [] bytes=bstream.toByteArray();
        string= Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }

}
