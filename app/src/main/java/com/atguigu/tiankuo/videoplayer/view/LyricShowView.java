package com.atguigu.tiankuo.videoplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.domain.Lyric;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class LyricShowView extends TextView {
    private Paint paintGreen;
    private Paint paintWhite;
    private int width;
    private int height;
    private ArrayList<Lyric> lyrics;

    private int index = 0;
    private float textHeight = 20;

    public LyricShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {
        paintGreen = new Paint();
        //设置抗锯齿
        paintGreen.setAntiAlias(true);
        //设置颜色
        paintGreen.setColor(Color.GREEN);
        //设置文字大小
        paintGreen.setTextSize(16);
        //设置对齐
        paintGreen.setTextAlign(Paint.Align.CENTER);

        paintWhite = new Paint();
        //设置抗锯齿
        paintWhite.setAntiAlias(true);
        //设置颜色
        paintWhite.setColor(Color.WHITE);
        //设置文字大小
        paintWhite.setTextSize(16);
        //设置对齐
        paintWhite.setTextAlign(Paint.Align.CENTER);

        lyrics = new ArrayList<Lyric>();
        Lyric lyric = new Lyric();

        for (int i = 0; i < 1000; i++) {
            lyric.setContent("aaaaaaaaaaaa_" + i);
            lyric.setSleepTime(2000);
            lyric.setTimePoint(2000 * i);
            //添加到集合
            lyrics.add(lyric);
            //重新创建新对象
            lyric = new Lyric();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (lyrics != null && lyrics.size() > 0) {
            String currentContent = lyrics.get(index).getContent();
            canvas.drawText(currentContent, width / 2, height / 2, paintGreen);
            float tempY = height / 2;

            //绘制前面部分
            for (int i = index - 1; i >= 0; i--) {
                //得到前一部分多月的歌词内容
                String preContent = lyrics.get(i).getContent();
                tempY = tempY - textHeight;
                if (tempY < 0) {
                    break;
                }
                canvas.drawText(preContent, width / 2, tempY, paintWhite);
            }
            tempY = height / 2;
            //绘制后面部分
            for (int i = index + 1; i < lyrics.size(); i++) {//得到后一部分多月的歌词内容
                String nextContent = lyrics.get(i).getContent();
                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }
                //绘制内容
                canvas.drawText(nextContent, width / 2, tempY, paintWhite);
            }
        } else {
            canvas.drawText("没有找到歌词...", getWidth() / 2, getHeight() / 2, paintGreen);
        }
    }
}
