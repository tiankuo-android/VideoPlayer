package com.atguigu.tiankuo.videoplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class LyricShowView extends TextView {
    private Paint paint;

    public LyricShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置颜色
        paint.setColor(Color.GREEN);
        //设置文字大小
        paint.setTextSize(16);
        //设置对齐
        paint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("没有找到歌词...",getWidth()/2,getHeight()/2,paint);

    }
}
