package com.atguigu.tiankuo.videoplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.atguigu.tiankuo.videoplayer.fragment.BaseFragment;
import com.atguigu.tiankuo.videoplayer.pager.LocalAudioPager;
import com.atguigu.tiankuo.videoplayer.pager.LocalVideoPager;
import com.atguigu.tiankuo.videoplayer.pager.NetAudioPager;
import com.atguigu.tiankuo.videoplayer.pager.NetVideoPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        rg_main = (RadioGroup)findViewById(R.id.rg_main);
        initFragment();
        //设置监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_main.check(R.id.rb_local_video);
    }

    private void initFragment() {
        //把各个页面实例化放入集合中
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetAudioPager());
        fragments.add(new NetVideoPager());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
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
                    position  = 3;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment currentFragment = fragments.get(position);
            addFragment(currentFragment);


        }
    }

    private void addFragment(BaseFragment currentFragment) {
        if(tempFragment != currentFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!currentFragment.isAdded()) {
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }
                ft.add(R.id.fl_content, currentFragment);
            } else {
                if(tempFragment != null) {
                    ft.hide(tempFragment);
                }
                ft.show(currentFragment);
            }
            ft.commit();
        }
        tempFragment = currentFragment;
    }
}
