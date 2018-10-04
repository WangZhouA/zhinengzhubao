package test.com.shoushi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import test.com.shoushi.R;


public class AdvertisingActivity extends Activity {
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(AdvertisingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);

    }
}
