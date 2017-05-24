package com.atguigu.tiankuo.videoplayer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.IMusicPlayService;
import com.atguigu.tiankuo.videoplayer.R;
import com.atguigu.tiankuo.videoplayer.service.MusicPlayService;

import static com.atguigu.tiankuo.videoplayer.R.id.iv_icon;


public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlTop;
    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvAudioname;
    private LinearLayout llBottom;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnPlaymode;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnLyric;
    private IMusicPlayService service;
    private int position;
    //连接好服务后的回调
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayService.Stub.asInterface(iBinder);
            if (service != null) {
                try {
                    service.openAudio(position);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Object data;

    private void findViews() {
        setContentView(R.layout.activity_audio_player);
        //初始化控件
        ivIcon = (ImageView) findViewById(iv_icon);
        ivIcon.setBackgroundResource(R.drawable.animation_bg);
        AnimationDrawable background = (AnimationDrawable) ivIcon.getBackground();
        background.start();
        rlTop = (RelativeLayout) findViewById(R.id.rl_top);
        ivIcon = (ImageView) findViewById(iv_icon);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvAudioname = (TextView) findViewById(R.id.tv_audioname);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvTime = (TextView) findViewById(R.id.tv_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        btnPlaymode = (Button) findViewById(R.id.btn_playmode);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnStartPause = (Button) findViewById(R.id.btn_start_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnLyric = (Button) findViewById(R.id.btn_lyric);

        btnPlaymode.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnLyric.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();

        getData();

        startAndBindService();
    }

    private void startAndBindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == btnPlaymode) {

        }else if(v == btnPre) {
            
        }else if(v ==btnStartPause) {
            try {
                if(service.isPlaying()) {
                    service.pause();
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                }else{
                    service.start();
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();

            }

        }else if(v == btnNext) {
            
        }else if(v == btnLyric) {
            
        }
    }

    private void getData() {
        position = getIntent().getIntExtra("position",0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connection != null) {
            unbindService(connection);
            connection = null;
        }
    }
}
