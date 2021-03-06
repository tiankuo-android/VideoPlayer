package com.atguigu.tiankuo.videoplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.IMusicPlayService;
import com.atguigu.tiankuo.videoplayer.R;
import com.atguigu.tiankuo.videoplayer.domain.Lyric;
import com.atguigu.tiankuo.videoplayer.domain.MediaItem;
import com.atguigu.tiankuo.videoplayer.service.MusicPlayService;
import com.atguigu.tiankuo.videoplayer.utils.LyricsUtils;
import com.atguigu.tiankuo.videoplayer.utils.Utils;
import com.atguigu.tiankuo.videoplayer.view.BaseVisualizerView;
import com.atguigu.tiankuo.videoplayer.view.LyricShowView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

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
    private MyReceiver receiver;
    private Utils utils;
    private boolean notification;
    private LyricShowView lyric_show_view;
    private BaseVisualizerView visualizerview;
    private Visualizer mVisualizer;

    private final static int PROGRESS = 0;
    private static final int SHOW_LYRIC = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LYRIC:
                    try {
                        int currentPosition = service.getCurrentPosition();

                        //调用歌词显示控件的setNextShowLyric
                        lyric_show_view.setNextShowLyric(currentPosition);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    removeMessages(SHOW_LYRIC);
                    sendEmptyMessage(SHOW_LYRIC);
                    break;
                case PROGRESS:
                    try {
                        int current = service.getCurrentPosition();
                        seekbarAudio.setProgress(current);

                        tvTime.setText(utils.stringForTime(current) + "/" + utils.stringForTime(service.getDuration()));

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };


    //连接好服务后的回调
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayService.Stub.asInterface(iBinder);
            if (service != null) {
                try {
                    if (notification) {
                        setViewData(null);
                    } else {
                        service.openAudio(position);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setViewData(MediaItem mediaItem) {
        try {
            setButtonImage();
            tvArtist.setText(service.getArtistName());
            Log.e("TAG", "service.getArtistName()--" + service.getArtistName());
            tvAudioname.setText(service.getAudioName());
            Log.e("TAG", "service.getAudioName()--" + service.getAudioName());
            int duration = service.getDuration();
            seekbarAudio.setMax(duration);
            //解析歌词
            //1.得到歌词所在路径
            String audioPath = service.getAudioPath();//mnt/sdcard/audio/beijingbeijing.mp3

            String lyricPath = audioPath.substring(0, audioPath.lastIndexOf("."));//mnt/sdcard/audio/beijingbeijing
            File file = new File(lyricPath + ".lrc");
            if (!file.exists()) {
                file = new File(lyricPath + ".txt");
            }
            LyricsUtils lyricsUtils = new LyricsUtils();
            lyricsUtils.readFile(file);

            //2.传入解析歌词的工具类
            ArrayList<Lyric> lyrics = lyricsUtils.getLyrics();
            lyric_show_view.setLyrics(lyrics);

            //3.如果有歌词，就歌词同步

            if (lyricsUtils.isLyric()) {
                handler.sendEmptyMessage(SHOW_LYRIC);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(PROGRESS);
        setupVisualizerFxAndUi();
//        handler.sendEmptyMessage(SHOW_LYRIC);
    }

    private void setupVisualizerFxAndUi() {
        int audioSessionid = 0;
        try {
            audioSessionid = service.getAudioSessionId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("audioSessionid==" + audioSessionid);
        mVisualizer = new Visualizer(audioSessionid);
        // 参数内必须是2的位数
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 设置允许波形表示，并且捕获它
        visualizerview.setVisualizer(mVisualizer);
        mVisualizer.setEnabled(true);
    }

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
        lyric_show_view = (LyricShowView) findViewById(R.id.lyric_show_view);
        visualizerview = (BaseVisualizerView) findViewById(R.id.base_visualizer);


        btnPlaymode.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnLyric.setOnClickListener(this);

        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();

        startAndBindService();
    }

    private void initData() {
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayService.OPEN_COMPLETE);
        registerReceiver(receiver, intentFilter);
        utils = new Utils();
        EventBus.getDefault().register(this);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setViewData(null);
        }


    }

    private void getData() {
        notification = getIntent().getBooleanExtra("notification", false);
        if (!notification) {
            position = getIntent().getIntExtra("position", 0);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == btnPlaymode) {
            setPlaymode();
        } else if (v == btnPre) {
            try {
                service.pre();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v == btnStartPause) {
            try {
                if (service.isPlaying()) {
                    service.pause();
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                } else {
                    service.start();
                    btnStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                }
            } catch (RemoteException e) {
                e.printStackTrace();

            }

        } else if (v == btnNext) {
            try {
                service.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (v == btnLyric) {

        }
    }

    private void setPlaymode() {
        try {
            int playmode = service.getPlaymode();
            if (playmode == MusicPlayService.REPEAT_NORMAL) {
                playmode = MusicPlayService.REPEAT_SINGLE;
            } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
                playmode = MusicPlayService.REPEAT_ALL;
            } else if (playmode == MusicPlayService.REPEAT_ALL) {
                playmode = MusicPlayService.REPEAT_NORMAL;
            }
            //保存到服务里面
            service.setPlaymode(playmode);

            setButtonImage();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void setButtonImage() {
        try {
            int playmode = service.getPlaymode();
            if (playmode == MusicPlayService.REPEAT_NORMAL) {
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_normal_selector);
            } else if (playmode == MusicPlayService.REPEAT_ALL) {
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_all_selector);
            } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_single_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void startAndBindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            mVisualizer.release();
        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
            connection = null;
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }


    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();

                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
