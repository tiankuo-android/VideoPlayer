package com.atguigu.tiankuo.videoplayer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.atguigu.tiankuo.videoplayer.fragment.BaseFragment;
import com.atguigu.tiankuo.videoplayer.pager.LocalAudioPager;
import com.atguigu.tiankuo.videoplayer.pager.LocalVideoPager;
import com.atguigu.tiankuo.videoplayer.pager.NetAudioPager;
import com.atguigu.tiankuo.videoplayer.pager.NetVideoPager;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity {

    private ArrayList<BaseFragment> fragments;
    private Fragment tempFragment;
    private boolean isExit = false;
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> baseFragments;
    private int position = 0;
    private Fragment mContent;
    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainActivity", "onCreate");
        setContentView(R.layout.activity_main);
        isGrantExternalRW(this);
        //初始化控件
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        initFragment();
        //设置监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选择本地视频
        rg_main.check(R.id.rb_local_video);

//        initView();
        initFragment();
//        setListener();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

    }


    private void initFragment() {
        //把各个页面实例化放入集合中
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetAudioPager());
        fragments.add(new NetVideoPager());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_local_video:
                    position = 0;
                    break;
                case R.id.rb_local_audio:
                    position = 1;
                    break;

                case R.id.rb_net_audio:
                    position = 2;
                    break;

                case R.id.rb_net_video:
                    position = 3;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment currentFragment = fragments.get(position);
            addFragment(currentFragment);


        }
    }

    private void addFragment(Fragment currentFragment) {
        if (tempFragment != currentFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!currentFragment.isAdded()) {
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }
                ft.add(R.id.fl_content, currentFragment);
            } else {
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }
                ft.show(currentFragment);
            }
            ft.commit();
            tempFragment = currentFragment;
        }
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (position != 0) {
//                rg_main.check(R.id.rb_local_video);
//                return true;
//            } else if (!isExit) {
//                Toast.makeText(MainActivity.this, "再按一次退出软件", Toast.LENGTH_SHORT).show();
//                isExit = true;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isExit = false;
//                    }
//                }, 2000);
//
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MainActivity", "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity", "onResume");
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "onDestroy");
    }
}
