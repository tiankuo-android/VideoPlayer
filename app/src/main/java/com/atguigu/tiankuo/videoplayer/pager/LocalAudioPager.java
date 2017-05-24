package com.atguigu.tiankuo.videoplayer.pager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.tiankuo.videoplayer.R;
import com.atguigu.tiankuo.videoplayer.activity.SystemVideoPlayerActivity;
import com.atguigu.tiankuo.videoplayer.adapter.LocalVideoAdapter;
import com.atguigu.tiankuo.videoplayer.domain.MediaItem;
import com.atguigu.tiankuo.videoplayer.fragment.BaseFragment;

import java.util.ArrayList;

public class LocalAudioPager extends BaseFragment {

    private ListView lv;
    private TextView tv_nodata;
    private ArrayList<MediaItem> mediaItems;
    private LocalVideoAdapter adapter;

    @Override
    public View initView() {
        Log.e("TAG","LocalAudioPager-initView");
        View  view = View.inflate(context, R.layout.fragment_local_vidio,null);
        lv = (ListView) view.findViewById(R.id.lv_vidio);
        tv_nodata = (TextView) view.findViewById(R.id.tv_content);
        //设置item的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SystemVideoPlayerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist",mediaItems);
                intent.putExtra("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","LocalAudioPager-initData");
        //加载本地所有的音频
        getData();


    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(mediaItems != null && mediaItems.size() >0){
                //有数据
                tv_nodata.setVisibility(View.GONE);
                //设置适配器
                adapter = new LocalVideoAdapter(context,mediaItems,true);
                lv.setAdapter(adapter);
            }else{
                //没有数据
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    private void getData() {
        new Thread(){
            public void run(){
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//音频在sdcard上的名称
                        MediaStore.Audio.Media.DURATION,//音频时长
                        MediaStore.Audio.Media.SIZE,//音频文件的大小
                        MediaStore.Audio.Media.DATA//音频播放地址
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if(cursor != null ){
                    while (cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        Log.e("TAG","name=="+name+",duration=="+duration+",data==="+data);

                        mediaItems.add(new MediaItem(name,duration,size,data));
                    }
                    cursor.close();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
