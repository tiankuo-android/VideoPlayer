package com.atguigu.tiankuo.videoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView textView;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imageView = (ImageView)findViewById( R.id.iv_login );
        progressBar = (ProgressBar)findViewById(R.id.progress );
        textView = (TextView)findViewById( R.id.tv_sousuo );

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        },2000);
    }
    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
    }

    //只要触摸屏幕就直接进入主页面
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //douw ,up
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //把延迟进入主页面的消息给移除
        handler.removeCallbacksAndMessages(null);
    }
}