package com.atguigu.tiankuo.startallvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startAllVideoPlayer(View v) {
        //把系统的播放器调起来
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse("http://vf1.mtime.cn/Video/2017/05/17/mp4/170517102706759383.mp4"),"video/*");
        startActivity(intent);

    }
}
