package com.atguigu.tiankuo.videoplayer.pager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.fragment.BaseFragment;

public class LocalAudioPager extends BaseFragment {
    private TextView textView;
    //重写视图
    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }
    @Override
    public void initData() {
        super.initData();
        textView.setText("本地音乐的内容");
    }
}
