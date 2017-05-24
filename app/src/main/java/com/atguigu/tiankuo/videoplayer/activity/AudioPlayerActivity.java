package com.atguigu.tiankuo.videoplayer.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.atguigu.tiankuo.videoplayer.R;
import com.atguigu.tiankuo.videoplayer.service.MusicPlayService;


public class AudioPlayerActivity extends AppCompatActivity {

    private ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);

        iv_icon.setBackgroundResource(R.drawable.animation_bg);
        AnimationDrawable background = (AnimationDrawable) iv_icon.getBackground();
        background.start();

        Intent intent = new Intent(this, MusicPlayService.class);
        startService(intent);
    }
}
