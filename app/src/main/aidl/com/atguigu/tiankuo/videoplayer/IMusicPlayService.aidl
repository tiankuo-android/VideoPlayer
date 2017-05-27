// IMusicPlayService.aidl
package com.atguigu.tiankuo.videoplayer;

// Declare any non-default types here with import statements

interface IMusicPlayService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
            
             //根据位置播放音频
                void openAudio(int position);

                //开始播放音频
                void start();

                //暂停播放音频
                void pause();

                //得到演唱者的名字
                String getArtistName();

                //得到歌曲名
                String getAudioName();

                //得到歌曲路径
                String getAudioPath();

                //得到总时长
                int getDuration();

                //得到当前播放进度
                int getCurrentPosition();

                //音频拖动
                void seekTo(int position);

                //播放下一个
                void next();

                //播放上一个
                void pre();

                //是否正在播放
                boolean isPlaying();

                //设置播放模式代码
                int getPlaymode();

                void setPlaymode(int playmode);

                int getAudioSessionId();


}
