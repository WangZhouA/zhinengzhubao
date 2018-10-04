package test.com.shoushi.JarPhoto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.com.shoushi.R;
import test.com.shoushi.bas.BaseActivity;
import test.com.shoushi.http.StringUtils;
import test.com.shoushi.interfaces.IUpadterDateListener;
import test.com.shoushi.sharedPreferences.UserInfo;

import static java.lang.String.valueOf;
import static test.com.shoushi.JarPhoto.Bimp.drr;

public class PublishedActivity extends BaseActivity implements OnClickListener{

    @BindView(R.id.header_left)
    ImageButton headerLeft;
    @BindView(R.id.header_text)
    TextView headerText;
    @BindView(R.id.header_right_msg)
    TextView headerRightMsg;
    private NoScrollGridView noScrollgridview;
    private GridAdapter adapter;
    private TextView activity_selectimg_send;

    @BindView(R.id.ed_Published)
    EditText ed_Published;

   private IUpadterDateListener iUpadterDateListener;


    private UserInfo userInfo;

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    closeLoadDialog();
                    showToast("上传成功");
                    finish();
                    break;
                case 1:
                    closeLoadDialog();
                    showToast("上传失败");
                    finish();
                    break;
            }
        }
    };

    public void setiUpadterDateListener(IUpadterDateListener iUpadterDateListener) {
        this.iUpadterDateListener = iUpadterDateListener;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_selectimg;
    }

    @Override
    protected void init() {
        Init();
    }

    public void Init() {
        headerRightMsg.setText("发送");
        headerText.setText("好友圈");
        headerRightMsg.setVisibility(View.VISIBLE);


        userInfo=new UserInfo(this);
        noScrollgridview = (NoScrollGridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(PublishedActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(PublishedActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);

                }
            }
        });
        activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
        activity_selectimg_send.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

            }
        });
    }
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();

    List<String> list;

    String location =Environment.getExternalStorageDirectory()+"wang_pic_loc";
    @OnClick({R.id.header_left,R.id.header_right_msg})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_left:
                finish();
//              showLoadDialog("Loading...",true);

                break;
            case R.id.header_right_msg:

                if (!TextUtils.isEmpty(ed_Published.getText().toString())) {
                    showLoadDialog("Loading...", true);
                    list = new ArrayList<String>();
                    for (int i = 0; i < drr.size(); i++) {
//
                        String str = Bimp.drr.get(i);
//                        try {
//                            Bitmap bm = Bimp.revitionImageSize(str);
//                            String newStr = str.substring(
//                                    str.lastIndexOf("/") + 1,
//                                    str.lastIndexOf("."));
//                            FileUtils.saveBitmap(bm, "" + newStr);
                            list.add(str);

//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                    }

//                 /**
//                  * 上传服务器
////                  * */
                    Map<String, Object> map = new HashMap<>();
                    map.put("context", ed_Published.getText().toString());
                    map.put("uid", userInfo.getStringInfo("id"));
                    okHttpForPicture(map);
                    //删除文件
                    FileUtils.deleteDir();

                }else {

                    showToast("动态不能为空！");
                }
                break;

        }
    }


    private void okHttpForPicture(final Map<String, Object> map) {
        final String strurl=StringUtils.SEND_DYNAMICS;

        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <list.size() ; i++) {
            File f = new File(list.get(i));
            if (f != null) {
                requestBody.addFormDataPart("files", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(strurl).post(requestBody.build()).tag(PublishedActivity.this).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq" ,"onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("--->成功上传图片", response.message() + " , body " + str);

                    Message message = new Message();
                    message.what=0;
                    handler.sendMessage(message);

                } else {
                    Log.i("--->上传图片失败" ,response.message() + " error : body " + response.body().string());

                    Message message = new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
    }


    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PublishedActivity.this,
                            TestPicActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    Uri photoUri;


    String strPhotoName = System.currentTimeMillis()+".jpg";
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/MyPhoto/";
    String filePath  = savePath + strPhotoName; //图片路径

    public void photo() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            photoUri = patrUri("file2");
            startCamera(TAKE_PICTURE,photoUri);
        }else{

            // TODO: 2017/10/25  没有相机权限
        }
    }
    //相机
    private void startCamera(int requestCode, Uri photoUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent, requestCode);

    }
    private Uri patrUri(String fileName) {
        // TODO Auto-generated method stub
        String strPhotoName = fileName+System.currentTimeMillis()+".jpg";
        String savePath = Environment.getExternalStorageDirectory().getPath()
                + "/MyPhoto/";
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filePath = savePath + strPhotoName;
        return Uri.fromFile(new File(dir, strPhotoName));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (drr.size() < 9 && resultCode == -1) {
                        drr.add(filePath);
                    }

                }
                break;
        }
    }

    boolean toback;
    @Override
    public void onBackPressed() {
        if (toback == false) {
                finish();
        }

        else  if (toback==true){
            super.onBackPressed();
        }
    }
}
