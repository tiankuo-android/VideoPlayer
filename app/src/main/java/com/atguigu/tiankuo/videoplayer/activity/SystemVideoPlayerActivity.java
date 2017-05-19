package com.atguigu.tiankuo.videoplayer.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.atguigu.tiankuo.videoplayer.R;

public class SystemVideoPlayerActivity extends AppCompatActivity {

    private VideoView vv_video;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        vv_video = (VideoView)findViewById(R.id.vv_video);

        uri = getIntent().getData();
        //设置监听
        //播放准备好的监听
        vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
             vv_video.start();
            }
        });
        //播放报错的监听
        vv_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayerActivity.this, "播放出错", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //播放结束的监听
        vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
           public void onCompletion(MediaPlayer mp) {
            Toast.makeText(SystemVideoPlayerActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //获取播放地址
        vv_video.setVideoURI(uri);
        //设置控制面板
        vv_video.setMediaController(new MediaController(this));
    }
}
