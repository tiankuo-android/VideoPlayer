package com.atguigu.tiankuo.videoplayer.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.atguigu.tiankuo.videoplayer.IMusicPlayService;
import com.atguigu.tiankuo.videoplayer.domain.MediaItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class MusicPlayService extends Service {

    private IMusicPlayService.Stub stub = new IMusicPlayService.Stub() {
        MusicPlayService service = MusicPlayService.this;
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getArtisName() throws RemoteException {
            return service.getArtisName();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return service.getAudioName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }
    };

    private ArrayList<MediaItem> mediaItems;

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }

    private void getData() {
        new Thread(){
            public void run(){
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//音频在sdcard上的名称
                        MediaStore.Audio.Media.DURATION,//音频时长
                        MediaStore.Audio.Media.SIZE,//音频文件的大小
                        MediaStore.Audio.Media.DATA,//音频播放地址
                        MediaStore.Audio.Media.ARTIST//艺术家
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
            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;

    }
    //根据位置播放音频
    private void openAudio(int position){

    }
    //开始播放音频
    private void start(){

    }
    //暂停播放音频
    private void pause(){

    }
    //得到演唱者的名字
    private String getArtisName(){
        return "";
    }
    //得到歌曲名
    private String getAudioName(){
        return "";
    }
    //得到歌曲路径
    private String getAudioPath(){
        return "";
    }
    //得到总时长
    private int getDuration(){
        return 0;
    }
    //得到当前播放进度
    private int getCurrentPosition(){
        return 0;
    }
    //音频拖动
    private void seekTo(int position){

    }
    //播放下一个
    private void next(){

    }
    //播放上一个
    private void pre(){

    }

}